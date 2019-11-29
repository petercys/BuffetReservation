public class CmdRequest extends RecordedCommand {
    private Reservation reservation;

    @Override
    public void execute(String[] cmdParts) {
        int totalPersons = Integer.parseInt(cmdParts[3]);
        // No need to create a new subclass of "Exception".
        // Also no need to throw an exception.
        // Just print the error message and return is ok la.
        if (cmdParts.length < 5) {
            System.out.println("Insufficient command arguments!");
            return;
        }

        reservation = BookingOffice.getInstance().addReservation(cmdParts[1], cmdParts[2],
                totalPersons, cmdParts[4]);

        addUndoCommand(this);
        clearRedoList();

        System.out.println(String.format("Done. Ticket code for %s: %d\n",
                cmdParts[4],
                reservation.getTicketCode()));
    }

    @Override
    public void undoMe() {
        BookingOffice.getInstance().removeReservation(reservation);
        addRedoCommand(this);
    }

    @Override
    public void redoMe() {
        BookingOffice.getInstance().addReservation(reservation);
        addUndoCommand(this);
    }
}
