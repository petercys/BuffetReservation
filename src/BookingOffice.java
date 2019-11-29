import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class BookingOffice {
    private static volatile BookingOffice mInstance;
    private List<Reservation> allReservations;
    private HashMap<String, Integer> ticketCodeMap; // <Date String, Ticket Code>

    private BookingOffice() {
        allReservations = new ArrayList<>();
        ticketCodeMap = new HashMap<>();
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

        int ticketCode = 1;
        if (ticketCodeMap.containsKey(sDateDine))
            ticketCode = ticketCodeMap.get(sDateDine) + 1;

        r.setTicketCode(ticketCode);

        return addReservation(r);
    }

    /**
     * Add a reservation
     *
     * @param r reservation to be added
     */
    public Reservation addReservation(Reservation r) {
        allReservations.add(r);
        ticketCodeMap.put(r.getDateDine().toString(), r.getTicketCode());

        Collections.sort(allReservations);

        return r;
    }

    /**
     * Remove a reservation
     *
     * @param r reservation to be removed
     */
    public void removeReservation(Reservation r) {
        allReservations.remove(r);

        String sDateDine = r.getDateDine().toString();
        int currentTicketCode = -1;

        if (ticketCodeMap.containsKey(sDateDine))
            currentTicketCode = ticketCodeMap.get(sDateDine);

        if (currentTicketCode <= 1)
            ticketCodeMap.remove(sDateDine);
        else
            ticketCodeMap.put(sDateDine, currentTicketCode - 1);
    }

    /**
     * List all reservations
     */
    public void listReservations() {
        System.out.println(Reservation.getListingHeader());

        for (Reservation r : allReservations)
            System.out.println(r.toString());
    }
}
