import random
from math import sqrt
import matplotlib.pyplot as plt
import networkx as nx

# Node positions on the graph
position_of_nodes = {
    1: (18,5), 2: (1,17), 3:(3,1), 4: (20,8),
    5:(15,13), 6: (13,5), 7:(8,14), 8:(13,17),
    9: (11,16), 10: (18,2), 11: (7,2), 12:(15,2)
}

# Directed graph with edge (u,v): (congestion, accident_probability, road safety)
traffic_graph = {
    (1,8): (3,0,0), (2,1): (3,0,1),
    (3,7): (1,0,0), (3,11): (2,1,1),
    (4,1): (1,1,1), (4,2): (1,0,1), (4,10): (1,0,0), (4,9): (2,0,1),
    (5,2): (2,1,1), (6,3): (2,1,0),
    (7,4): (3,0,1), (7,10): (2,0,0),
    (8,4): (3,1,0), (8,9): (2,1,1),
    (9,7): (1,1,1), (10,2): (1,1,0), (10,3): (3,1,1), (10,5): (3,0,0), (10,11): (1,0,0),
    (11,5): (2,1,0), (11,6): (1,0,1)
}


pheromone = {(u,v): 1.0 for (u,v) in traffic_graph}

# Calculates the Euclidean distance between nodes
def euclidean_distance(node1, node2):
    x1, y1 = position_of_nodes[node1]
    x2, y2 = position_of_nodes[node2]
    return sqrt((x2 - x1)**2 + (y2 - y1)**2)

# Adds the calculated Euclidean Distance along with the congestion, accidental probability and road safety values
def cost(node1, node2):
    if (node1, node2) not in traffic_graph:
        return float('inf')
    congestion, accident_probability, safety = traffic_graph[(node1, node2)]
    return euclidean_distance(node1, node2) + congestion + accident_probability + safety

# This function determines whether a pathway is attractive, higher value = increased probability to use pathway
def heuristic_function(node1, node2):
    return 1 / (cost(node1, node2) + 1e-6)

# Search algorithm that gradually improves overtime to find local/global optima
def hill_climbing_algorithm(start_node, target_node):
    current_node = start_node
    pathway = [current_node]
    total_cost = 0

    while current_node != target_node: # Current state
        neighbors = [v for (u, v) in traffic_graph if u == current_node]
        if not neighbors:
            break

# Loops and compares current state cost with neighbor edge cost to determine whether edge is optimal to use
        next_node = None
        min_cost = float('inf')
        for neighbor in neighbors:
            c = cost(current_node, neighbor)
            if c < min_cost and neighbor not in pathway:
                min_cost = c
                next_node = neighbor

# If none are optimal, the loop is broken and total cost is added + pathway as a list is shown
        if next_node is None:
            break

        total_cost += cost(current_node, next_node)
        pathway.append(next_node)
        current_node = next_node

    return pathway, total_cost

# Colony of ant agents with parameters are implemented to mitigate cost level
def ant_colony_optimisation(start_node, target_node, ants=20, iterations=50, alpha=1.0,
                            beta=2.0, evaporation=0.5, Q=100):
    global pheromone
    optimal_pathway = None
    optimal_cost = float('inf')

    for _ in range(iterations):
        all_paths = []
        all_cost = []

# Loop collects set of visited nodes + cost of those nodes
        for _ in range(ants):
            pathway = [start_node]
            current_node = start_node
            visited = set()
            visited.add(current_node)
            cost_of_pathway = 0

            while current_node != target_node:
                neighbors = [v for (u, v) in traffic_graph if u == current_node and v not in visited]
                if not neighbors:
                    break

# Main ant agent logic to dictate optimal pathway using pheromones and heuristic function
                total = 0
                probs = []
                for neighbor in neighbors:
                    tau = pheromone.get((current_node, neighbor), 1.0) ** alpha
                    eta = heuristic_function(current_node, neighbor) ** beta
                    score = tau * eta
                    total += score
                    probs.append((neighbor, score))

                if total == 0:
                    break

                r = random.uniform(0, total)
                cumulative = 0
                next_node = None
                for neighbor, prob in probs:
                    cumulative += prob
                    if r <= cumulative:
                        next_node = neighbor
                        break

                if next_node is None:
                    break

                cost_of_pathway += cost(current_node, next_node)
                pathway.append(next_node)
                visited.add(next_node)
                current_node = next_node

            if pathway[-1] == target_node:
                all_paths.append(pathway)
                all_cost.append(cost_of_pathway)
                if cost_of_pathway < optimal_cost:
                    optimal_cost = cost_of_pathway
                    optimal_pathway = pathway

        for key in pheromone:
            pheromone[key] *= (1 - evaporation)

# Ant agents desposit pheromone depending on parameters + optimal pathway and cost is returned
        for path, cost_val in zip(all_paths, all_cost):
            deposit = Q / cost_val
            for i in range(len(path) - 1):
                u, v = path[i], path[i + 1]
                if (u, v) in pheromone:
                    pheromone[(u, v)] += deposit

    return optimal_pathway, optimal_cost

# Implementing random restart features along with ant colony optimization + hill climbing algorithm
def random_restart_with_ACO(target_node, restarts=10, ants=10, iterations=50):
    global pheromone
    optimal_pathway = None
    optimal_cost = float('inf')

    random_start_nodes = [node for node in position_of_nodes if node != target_node]

    for i in range(restarts):
        start_node = random.choice(random_start_nodes)
        print(f"Attempt {i + 1}: Start at node {start_node}")

        pheromone = {(u, v): 1.0 for (u, v) in traffic_graph}  # Reset pheromone
        pathway, cost_val = ant_colony_optimisation(start_node, target_node, ants=ants, iterations=iterations)

        if pathway and pathway[-1] == target_node and cost_val < optimal_cost:
            optimal_cost = cost_val
            optimal_pathway = pathway

    return optimal_pathway, optimal_cost

# Visualisation provided to see the graph with nodes and highlighted optimal pathway
def visual_graph(pathway):
    D = nx.DiGraph()
    for (u, v), (congestion, accident_prob, safety) in traffic_graph.items():
        label = f"C:{congestion}, A:{accident_prob}, S:{safety}"
        D.add_edge(u, v, label=label)

    pos = position_of_nodes
    nx.draw(D, pos, with_labels=True, node_color="skyblue", node_size=1000, font_size=10)
    edge_labels = nx.get_edge_attributes(D, "label")
    nx.draw_networkx_edge_labels(D, pos, edge_labels=edge_labels)

    if pathway:
        edge_list = [(pathway[i], pathway[i + 1]) for i in range(len(pathway) - 1)]
        nx.draw_networkx_edges(D, pos, edgelist=edge_list, edge_color='red', width=3)

    plt.title("Optimal Pathway Using Hill Climbing and ACO")
    plt.show()


# Main function to run traffic management
if __name__ == "__main__":
    target_node = int(input("Enter the target node: "))
    optimal_path, total_cost = random_restart_with_ACO(target_node)

    if optimal_path:
        print("\nOptimal Pathway:", optimal_path)
        print("Total Cost:", total_cost)
        visual_graph(optimal_path)
    else:
        print("No valid pathway could be found.")
