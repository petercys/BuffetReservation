public class CmdRequest extends RecordedCommand {
    private Reservation reservation;

    @Override
    public void execute(String[] cmdParts) {
        int totalPersons = Integer.parseInt(cmdParts[3]);

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
