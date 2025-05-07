import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class FlightScheduleManager {

    // Contains CSV files
    static final String FLIGHT_CSV = "flight.csv";
    static final String PASSENGER_CSV = "passenger.csv";

    // Organised arraylists containing flights and passengers
    static List<Flight> flights = new ArrayList<>();
    List<Passenger> passengers = new ArrayList<>();

    // Method to generate all flights from "flight.csv" and storing them in arraylists for flights
    List<Flight> loadFlights() throws IOException {
        BufferedReader flight_reader = new BufferedReader(new FileReader(FLIGHT_CSV));
        flight_reader.readLine();
        String row;
        while ((row = flight_reader.readLine()) != null) {
            String[] flight_row = row.split(",");
            String flight_number = flight_row[0];
            String flight_departure_code = flight_row[1];
            String flight_departure_name = flight_row[2];
            String flight_arrival_code = flight_row[3];
            String flight_arrival_name = flight_row[4];
            String flight_departure_date = flight_row[5];

            flights.add(new Flight(flight_number, flight_departure_code, flight_departure_name,
                    flight_arrival_code, flight_arrival_name, flight_departure_date));

        }
        // Sorts the flights in alphabetical order (using sorting algorithm)
        flights.sort(Comparator.comparing(flight -> flight.flightNumber));
        return flights;
    }

    // Method to return a certain flight using flight number using Binary Search
    Flight getFlight(String flight_number) {
        int low = 0, high = flights.size() - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            Flight midFlight = flights.get(mid);
            int comparison = midFlight.flightNumber.compareToIgnoreCase(flight_number);
            if (comparison == 0) return midFlight;
            else if (comparison < 0) low = mid + 1;
            else high = mid - 1;
        }
        return null;
    }


    // Adds passenger details to "passenger.csv" into a new row after booking
    void savePassengers() throws IOException {
        List<String> rows = new ArrayList<>();
        for (Passenger passenger : passengers) {
            rows.add(passenger.passengerToCSV());
        }
        Files.write(Paths.get(PASSENGER_CSV), rows, StandardOpenOption.APPEND);
    }

    // Checks for duplicate bookings in "passenger.csv" using passenger name and flight number
    boolean duplicateBooking(String passengerName, String flightNumber) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(PASSENGER_CSV));
        bufferedReader.readLine();
        String row;

        // Gathers passenger name and flight number + returns true if duplicate is found
        while ((row = bufferedReader.readLine()) != null) {
            String[] passenger_row = row.split(",");
            if(passenger_row.length == 5){
                String name = passenger_row[0];
                String flightnumber = passenger_row[1];
                if(name.equalsIgnoreCase(passengerName) && flightnumber.equalsIgnoreCase(flightNumber)) {
                    return true;
                }
            }
        }
        return false;

    }

    // Books flight using passenger name, flight number and class input
    void bookPassengers(String passengerName, String flightNumber, String classname) throws IOException {

        // If "passenger.csv" contains a duplicate passenger name and assigned flight number, this prompt is printed
        if(duplicateBooking(passengerName, flightNumber)){
            System.out.println("\nThis passenger already exists in this flight");
            return;
        }

        // Checks user classname input to assign user to specified class type
        Flight flight = getFlight(flightNumber);
        Seat seatClass = flight.getClassType(classname);
        if (seatClass == null) {
            System.out.println("\nClass " + "\"" + classname + "\"" + " cannot be found." +
                    " Press enter to return to menu");
            return;
        }

        /*Successfully books a new passenger into "passenger.csv"
        if there is available seats in the range*/
        Passenger passenger;
        if (seatClass.availableSeats()) {
            passenger = new Passenger(passengerName, flightNumber, classname,
                    seatClass.incrementAvailableSeat(), "Booked");
            seatClass.bookedPassengers.add(passenger);

        // Passenger placed in the waitlist
        } else {
            passenger = new Passenger(passengerName, flightNumber, classname, -1, "In Waitlist");
            seatClass.waitlist.add(passenger);
        }

        // New booked passenger added in general arraylist of passengers
        passengers.add(passenger);
        System.out.println("\nPassenger " + passengerName + " has been booked. Press enter to return to main menu");
    }

    // Method to remove a passenger from "passenger.csv" using passenger's name
    void cancelBooking(String passengerName) throws IOException {
        Passenger passenger = passengers.stream().filter(p -> p.passengerName.
                equals(passengerName)).findFirst().orElse(null);
        if(passenger == null) {
            System.out.println("Passenger " + passengerName + " not found in bookings");
        }

        // Removes passenger within the program
        Flight f = getFlight(passenger.assignedFlightNumber);
        f.getClassType(passenger.assignedClass).cancelPassenger(passengerName);
        passengers.remove(passenger);
        System.out.println("Passenger " + passengerName + " has been cancelled. Press enter to return to main menu");
    }


    // Prints all passengers and their details from the "passenger.csv" file
    void showPassengers() throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader(PASSENGER_CSV));
        bufferedReader.readLine();
        String row;
        System.out.println("LIST OF PASSENGERS: ");
        while ((row = bufferedReader.readLine()) != null) {
            String[] passenger_row = row.split(",");
            String passenger_name = passenger_row[0];
            String passenger_flightnumber = passenger_row[1];
            String passenger_class = passenger_row[2];
            String passenger_seatnumber = passenger_row[3];
            String passenger_status = passenger_row[4];
            System.out.println("Passenger Name: " + passenger_name + " | Flight Number: "
                    + passenger_flightnumber + " | Class: " + passenger_class + " | Seat Number: "
                    + passenger_seatnumber + " | Status: " + passenger_status);

        }
        System.out.println("Press enter to return to main menu");
    }
}
