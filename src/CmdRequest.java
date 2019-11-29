public class CmdRequest extends RecordedCommand {
    private Reservation reservation;

    @Override
    public void execute(String[] cmdParts) throws ExInsufficientArgs {
        if (cmdParts.length < 5)
            throw new ExInsufficientArgs();

        String sGuestName = cmdParts[1];
        String sPhoneNumber = cmdParts[2];
        String sTotalPersons = cmdParts[3];
        String sDateDine = cmdParts[4];

        int totalPersons;
        try {
            totalPersons = Integer.parseInt(sTotalPersons);
        } catch (NumberFormatException e) {
            // Just catch it and print an error message here.
            // Create a new Exception subclass and rethrow it is redundant.
            System.out.println("Wrong number format!");
            return;
        }
        
        try {
            reservation = BookingOffice.getInstance().addReservation(sGuestName, sPhoneNumber,
                    totalPersons, sDateDine);
        } catch (ExBookingAlreadyExists | ExDateHasAlreadyPassed e) {
            System.out.println(e.getMessage());
            return;
        }

        addUndoCommand(this);
        clearRedoList();

        System.out.println(String.format("Done. Ticket code for %s: %d\n",
                sDateDine, reservation.getTicketCode()));
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
