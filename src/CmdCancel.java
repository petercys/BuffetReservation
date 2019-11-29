import java.util.List;

public class CmdCancel extends RecordedCommand {
    private Reservation reservation;
    private List<Table> tablesAllocated;

    @Override
    public void execute(String[] cmdParts) throws ExInsufficientArgs, ExBookingNotFound, ExDateHasAlreadyPassed {
        if (cmdParts.length < 3)
            throw new ExInsufficientArgs();

        String sDateDine = cmdParts[1];
        String sTicketCode = cmdParts[2];

        // Check if the target dining date has passed the system date
        if (new Day(sDateDine).hasPassed(SystemDate.getInstance().clone()))
            throw new ExDateHasAlreadyPassed();

        int ticketCode;
        try {
            ticketCode = Integer.parseInt(sTicketCode);
        } catch (NumberFormatException e) {
            // Just catch it and print an error message here.
            // Creating a new Exception subclass and rethrow it is redundant.
            System.out.println("Wrong number format!");
            return;
        }

        reservation = BookingOffice.getInstance().searchReservation(sDateDine, ticketCode);
        if (reservation == null)
            throw new ExBookingNotFound();

        RState status = reservation.getStatus();
        if (status instanceof RStateTableAllocated)
            tablesAllocated = ((RStateTableAllocated) status).getAllocatedTables();

        // Release the tables
        if (tablesAllocated != null) {
            for (Table table : tablesAllocated) {
                table.setAssigned(false);
                table.setTicketCode(-1);
            }
        }

        BookingOffice.getInstance().cancelReservation(reservation);

        addUndoCommand(this);
        clearRedoList();

        System.out.println("Done.");
    }

    @Override
    public void undoMe() {
        BookingOffice.getInstance().undoCancellingReservation(reservation);

        // Re-assign the tables
        if (tablesAllocated != null) {
            for (Table table : tablesAllocated) {
                table.setAssigned(true);
                table.setTicketCode(reservation.getTicketCode());
            }
        }

        addRedoCommand(this);
    }

    @Override
    public void redoMe() {
        // Release the tables
        if (tablesAllocated != null) {
            for (Table table : tablesAllocated) {
                table.setAssigned(false);
                table.setTicketCode(-1);
            }
        }

        BookingOffice.getInstance().cancelReservation(reservation);

        addUndoCommand(this);
    }
}
