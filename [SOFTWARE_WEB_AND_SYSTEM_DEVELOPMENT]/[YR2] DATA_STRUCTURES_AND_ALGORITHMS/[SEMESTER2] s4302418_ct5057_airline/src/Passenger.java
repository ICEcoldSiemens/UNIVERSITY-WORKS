public class Passenger {

    // // Holds relevant attributes for a passenger from their name to their assigned status
    protected String passengerName;
    protected String assignedFlightNumber;
    protected String assignedClass;
    protected int seatNumber;
    protected String bookStatus;


    Passenger(String passengerName, String assignedFlightNumber, String assignedClass,
              int seatNumber, String bookStatus) {
        this.passengerName = passengerName;
        this.assignedFlightNumber = assignedFlightNumber;
        this.assignedClass = assignedClass;
        this.seatNumber = seatNumber;
        this.bookStatus = bookStatus;
    }

    // Organises string format to "passenger.csv"
    String passengerToCSV(){
        return passengerName + "," + assignedFlightNumber + "," + assignedClass + "," + seatNumber + "," + bookStatus;
    }


}
