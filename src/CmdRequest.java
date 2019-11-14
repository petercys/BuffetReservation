public class CmdRequest extends RecordedCommand {
    private Reservation reservation;

    @Override
    public void execute(String[] cmdParts) {
        reservation = BookingOffice.getInstance().addReservation(cmdParts[1], cmdParts[2],
                Integer.parseInt(cmdParts[3]), cmdParts[4]);

        addUndoCommand(this);
        clearRedoList();

        System.out.println("Done.");
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
