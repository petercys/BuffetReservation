public class CmdRequest extends RecordedCommand {
    private Reservation reservation;

    @Override
    public void execute(String[] cmdParts) {
        // No need to create a new subclass of "Exception".
        // Also no need to throw an exception.
        // Just print the error message and return is ok la.
        if (cmdParts.length < 5) {
            System.out.println("Insufficient command arguments!");
            return;
        }

        int totalPersons;
        try {
            totalPersons = Integer.parseInt(cmdParts[3]);
        } catch (NumberFormatException e) {
            System.out.println("Wrong number format!");
            return;
        }

        try {
            reservation = BookingOffice.getInstance().addReservation(cmdParts[1], cmdParts[2],
                    totalPersons, cmdParts[4]);
        } catch (ExBookingAlreadyExists | ExDateHasAlreadyPassed e) {
            System.out.println(e.getMessage());
            return;
        }

        addUndoCommand(this);
        clearRedoList();

        System.out.println(String.format("Done. Ticket code for %s: %d\n",
                cmdParts[4],
                reservation.getTicketCode()));
    }

    @Override
    public void undoMe() {
        BookingOffice.getInstance().undoReservation(reservation);
        addRedoCommand(this);
    }

    @Override
    public void redoMe() {
        try {
            BookingOffice.getInstance().addReservation(reservation);
        } catch (ExBookingAlreadyExists e) {
            System.out.println(e.getMessage());
            return;
        }

        addUndoCommand(this);
    }
}
