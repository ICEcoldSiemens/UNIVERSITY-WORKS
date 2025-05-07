import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class AirlinesApplication {

    // Main function to run flight scheduling application
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        // Holds and loads all flights within "flight.csv" file
        FlightScheduleManager flightScheduleManager = new FlightScheduleManager();
        List<Flight> flight_list = flightScheduleManager.loadFlights();
        String choice;

        // Prints "Main Menu" options to book and cancel flights, print all passenger details and exit program
        System.out.println("""
                WELCOME TO AIRLINES FSP [FLIGHT SCHEDULING PROGRAM]\

                Choose any of the following option to begin:\s

                1. Book A Flight [Type "1"]\s
                2. Cancel A Flight [Type "2"]\

                3. Show Passengers Details [Type "3"]\s
                4. Exit Program [Type "4"]""");


        // Loops the application until user selects OPTION 4 + runs switch cases for each option selected
        while(true) {
            System.out.print("\n");
            choice = scanner.nextLine();

            switch (choice) {

                // If user selects OPTION 1, name input is prompt and saved in capitals + name validation
                case "1":
                    System.out.println("\nWhat is your name: ");
                    String passengerName = "";

                    while (passengerName.length() < 4) {
                        passengerName = scanner.nextLine().toUpperCase();
                        if (passengerName.length() < 4) {
                            System.out.println("\nPlease enter at least 4 characters long.");
                        }
                    }

                    // Loops and prints over the list of flights stored in the List "flight_list"
                    System.out.println("\nHERE ARE THE LIST OF AVAILABLE FLIGHTS: ");
                    for (Flight flight : flight_list) {
                        System.out.println(flight);
                    }

                    // Prompts user to input flight number and flight number validation
                    System.out.println("\nInput the flight number:");
                    String flight_number = "";

                    while (flightScheduleManager.getFlight(flight_number) == null) {
                        flight_number = scanner.nextLine().toUpperCase();
                        if (flightScheduleManager.getFlight(flight_number) == null) {
                            System.out.println("This flight number is invalid. Please try again.");
                        }
                    }
                    // A valid flight number returns and prints the specified flight
                    System.out.println("\nThis flight has been selected -> "
                            + flightScheduleManager.getFlight(flight_number));

                    // Prompts user to input class type + methods to book a flight and save details in "passenger.csv"
                    System.out.println("\nWhich class do you wish to book [First Class/Business/Economy]: ");
                    String className = scanner.nextLine();
                    flightScheduleManager.bookPassengers(passengerName, flight_number, className);
                    flightScheduleManager.savePassengers();
                    break;

                /* If user selects OPTION 2, name input prompted and used to find passenger to remove
                details from "passenger.csv" + save the change afterward */
                case "2":
                    System.out.println("What is your name: ");
                    flightScheduleManager.cancelBooking(scanner.nextLine().toUpperCase());
                    flightScheduleManager.savePassengers();
                    break;

                // If user selects OPTION 3, all passenger details in "passenger.csv" are printed
                case "3":
                    flightScheduleManager.showPassengers();
                    break;

                // If user selects OPTION 4, the program terminates
                case "4":
                    System.out.println("THANK YOU FOR USING AIRLINES FSP APPLICATION. GOODBYE! ");
                    return;

                // Loops back to main menu after a case is executed + can type exit to leave program
                default:
                    System.out.println("Please input a number between 1 and 4 [Type exit to end program]: ");
                    System.out.println("""

                            1. Book A Flight [Type "1"]\s
                            2. Cancel A Flight [Type "2"]\

                            3. Show Passenger Details [Type "3"]\s
                            4. Exit Program [Type "4"]""");

            }
        }
    }
}




