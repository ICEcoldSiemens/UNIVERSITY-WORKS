import java.util.*;

public class Seat {

    int firstseat, lastseat; // Minimum and maximum range of seats
    List<Passenger> bookedPassengers = new ArrayList<>(); // ArrayList of success booked passengers
    Queue<Passenger> waitlist = new LinkedList<>(); // Queue for the waitlist


    Seat(int firstseat, int lastseat) {
        this.firstseat = firstseat;
        this.lastseat = lastseat;
    }

    // Returns the amount of available seats as the ArrayList "bookedPassenger" increases in size
    boolean availableSeats(){
        return bookedPassengers.size() < (lastseat - firstseat + 1);
    }

    // Increments a booked passenger's seat number as available seats decrease
    int incrementAvailableSeat(){
        return firstseat + bookedPassengers.size();
    }

    /* Removes a passenger from the "bookedPassengers" ArrayList using name
    + removes passengers in waitlist queue (FIFO) and adds them to "bookedPassengers" ArrayList*/
    void cancelPassenger(String passengerName){
        bookedPassengers.removeIf(passenger -> passenger.passengerName.equals(passengerName));
        if(!waitlist.isEmpty()){
            Passenger passenger = waitlist.poll();
            passenger.seatNumber = incrementAvailableSeat();
            passenger.bookStatus = "Booked";
            bookedPassengers.add(passenger);
        }
    }


}
