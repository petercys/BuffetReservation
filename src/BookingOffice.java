import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class BookingOffice {
    private static volatile BookingOffice mInstance;
    private List<Reservation> allReservations;
    // One business day should have its corresponding ticket code
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
     * Add a reservation to the booking system
     *
     * @param guestName   name of the guest
     * @param phoneNumber phone number
     * @param totPersons  total persons for the booking
     * @param sDateDine   dining date
     * @return the reservation made
     * @throws ExBookingAlreadyExists if the reservation is existed in the system already
     * @throws ExDateHasAlreadyPassed if the target dining date is passed
     */
    public Reservation addReservation(String guestName, String phoneNumber, int totPersons,
                                      String sDateDine)
            throws ExBookingAlreadyExists, ExDateHasAlreadyPassed {
        Day dateDine = new Day(sDateDine);

        // Check if the target dining date has passed the system date
        if (dateDine.hasPassed(SystemDate.getInstance().clone()))
            throw new ExDateHasAlreadyPassed();

        Reservation r = new Reservation(guestName, phoneNumber, totPersons, dateDine);

        int ticketCode = 1;
        if (ticketCodeMap.containsKey(sDateDine))
            ticketCode = ticketCodeMap.get(sDateDine) + 1;

        r.setTicketCode(ticketCode);

        addReservation(r);

        return r;
    }

    /**
     * Add a reservation to the booking system
     *
     * @param r the reservation
     */
    public void addReservation(Reservation r) throws ExBookingAlreadyExists {
        if (hasReservation(r))
            throw new ExBookingAlreadyExists();

        allReservations.add(r);
        // Update the ticket code of the corresponding business day
        ticketCodeMap.put(r.getDateDine().toString(), r.getTicketCode());

        Collections.sort(allReservations);
    }

    /**
     * Undo adding a reservation (not the same as cancel)
     *
     * @param r the reservation to be undone
     */
    public void undoAddingReservation(Reservation r) {
        // We need to revert the ticketCode before removing the reservation from the booking system
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
        // "For example, even if one cancels his/her booking,
        // the ticket code that was already given to that booking will not be reused."

        removeReservation(r);
    }

    /**
     * Undo cancelling reservation (without updating ticket code)
     *
     * @param r the reservation
     */
    public void undoCancellingReservation(Reservation r) {
        if (!hasReservation(r))
            allReservations.add(r);

        Collections.sort(allReservations);
    }

    /**
     * Remove a reservation
     *
     * @param r the reservation to be removed
     */
    public void removeReservation(Reservation r) {
        allReservations.remove(r);
    }

    /**
     * Check if the booking system has the reservation
     * (which has the same guestName and dateDine)
     *
     * @param r the reservation
     * @return true if the booking system has the reservation
     */
    public boolean hasReservation(Reservation r) {
        return allReservations.contains(r);
    }

    /**
     * Search for a reservation in a specific date by ticket code
     *
     * @param dateDine   the dateDine of the reservation
     * @param ticketCode the ticket code of the reservation
     * @return the reservation, or null if there is no search result
     */
    public Reservation searchReservation(String dateDine, int ticketCode) {
        for (Reservation reservation : allReservations) {
            if (reservation.getDateDine().toString().equals(dateDine)
                    && reservation.getTicketCode() == ticketCode) {
                return reservation;
            }
        }

        return null;
    }

    /**
     * Get all reservations which the status/state is pending
     *
     * @param dateDine target dining date of the reservations
     * @return a list of reservations which the status/state is pending
     */
    public List<Reservation> getPendingReservations(String dateDine) {
        List<Reservation> pendingReservations = new ArrayList<>();

        for (Reservation reservation : allReservations) {
            if (reservation.getDateDine().toString().equals(dateDine)
                    && reservation.getStatus() instanceof RStatePending) {
                pendingReservations.add(reservation);
            }
        }

        return pendingReservations;
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
