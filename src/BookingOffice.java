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
                                      String sDateDine)
            throws ExBookingAlreadyExists, ExDateHasAlreadyPassed {
        Day dateDine = new Day(sDateDine);
        if (dateDine.hasPassed(SystemDate.getInstance().clone()))
            throw new ExDateHasAlreadyPassed();

        Reservation r = new Reservation(guestName, phoneNumber, totPersons, dateDine);

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
    public Reservation addReservation(Reservation r) throws ExBookingAlreadyExists {
        if (hasReservation(r))
            throw new ExBookingAlreadyExists();

        allReservations.add(r);
        ticketCodeMap.put(r.getDateDine().toString(), r.getTicketCode());

        Collections.sort(allReservations);

        return r;
    }

    /**
     * Undo a reservation (not the same as cancel)
     * @param r the reservation to be undone
     */
    public void undoReservation(Reservation r) {
        // We need to revert the ticketCode before removing the reservation from the booking system.
        String sDateDine = r.getDateDine().toString();
        int currentTicketCode = -1;

        if (ticketCodeMap.containsKey(sDateDine))
            currentTicketCode = ticketCodeMap.get(sDateDine);

        if (currentTicketCode <= 1)
            ticketCodeMap.remove(sDateDine);
        else
            ticketCodeMap.put(sDateDine, currentTicketCode - 1);

        removeReservation(r);
    }

    public void cancelReservation(Reservation r) {
        // We don't need to revert the ticketCode here, unlike what "undoReservation" does.
        // Reason:
        // For example, even if one cancels his/her booking,
        // the ticket code that was already given to that booking will not be reused.

        removeReservation(r);
    }

    /**
     * Remove a reservation
     *
     * @param r reservation to be removed
     */
    public void removeReservation(Reservation r) {
        allReservations.remove(r);
    }

    public boolean hasReservation(Reservation r) {
        return allReservations.contains(r);
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
