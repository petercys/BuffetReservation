import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookingOffice {
    private static volatile BookingOffice mInstance;
    private List<Reservation> allReservations;

    private BookingOffice() {
        allReservations = new ArrayList<>();
    }

    /**
     * Get the instance of BookingOffice
     *
     * @return the instance of BookingOffice
     */
    public static BookingOffice getInstance() {
        // Double-Checked Locking is being used instead of Initialization-on-demand holder idiom
        BookingOffice instance = mInstance;
        if (instance == null) {
            synchronized (BookingOffice.class) {
                instance = mInstance;
                if (instance == null) {
                    mInstance = instance = new BookingOffice();
                }
            }
        }
        return instance;
    }

    /**
     * Add a reservation to the system
     *
     * @param guestName   name of the guest
     * @param phoneNumber phone number
     * @param totPersons  total persons for the booking
     * @param sDateDine   dining date
     * @return the reservation made
     */
    public Reservation addReservation(String guestName, String phoneNumber, int totPersons,
                                      String sDateDine) {
        Reservation r = new Reservation(guestName, phoneNumber, totPersons, sDateDine);
        allReservations.add(r);
        Collections.sort(allReservations); // allReservations
        return r; //Why return?  Ans: You'll see that it is useful in CmdRequest.java in Q2.
    }

    /**
     * List all reservations
     */
    public void listReservations() {
        System.out.println(Reservation.getListingHeader()); // Reservation
        for (Reservation r : allReservations)
            System.out.println(r.toString()); // r or r.toString()
    }

    /**
     * Remove a reservation
     *
     * @param r reservation to be removed
     */
    public void removeReservation(Reservation r) {
        allReservations.remove(r);
    }

    /**
     * Add a reservation
     *
     * @param r reservation to be added
     */
    public void addReservation(Reservation r) {
        allReservations.add(r);
        Collections.sort(allReservations);
    }
}
