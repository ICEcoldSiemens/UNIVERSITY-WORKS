import java.util.HashMap;
import java.util.Map;

public class Flight {

    // Holds relevant attributes for a flight from flight number to departure date
    protected String flightNumber;
    protected String departureAPCode;
    protected String departureAPName;
    protected String arrivalAPCode;
    protected String arrivalAPName;
    protected String departureDate;
    Map<String, Seat> classTypes; // A hashmap that holds key-value pairs of the class type and seating range


    Flight(String flightNumber, String departureAPCode, String departureAPName,
           String arrivalAPCode, String arrivalAPName, String departureDate){
        this.flightNumber = flightNumber;
        this.departureAPCode = departureAPCode;
        this.departureAPName = departureAPName;
        this.arrivalAPCode = arrivalAPCode;
        this.arrivalAPName = arrivalAPName;
        this.departureDate = departureDate;
        this.classTypes = new HashMap<>();

        classTypes.put("First Class", new Seat(1,5));
        classTypes.put("Business", new Seat(6,15));
        classTypes.put("Economy", new Seat(16,35));
    }

    // Returns the specified class type key-value pair
    public Seat getClassType(String classType){
        return classTypes.get(classType);
    }

    // Organises flight into this string format when imported from "flight.csv"
     public String toString(){
        return flightNumber + " | " + departureAPCode + " | " + departureAPName + " | " +
                arrivalAPCode + " | " + arrivalAPName + " | " + departureDate;
     }
}
