import java.util.ArrayList;
import java.util.List;

public class CmdAssignTable extends RecordedCommand {
    private Reservation reservation;
    private List<Table> foundTables; // should NOT be sorted

    @Override
    public void execute(String[] cmdParts) throws ExInsufficientArgs, ExBookingNotFound,
            ExDateHasAlreadyPassed, ExTableCodeNotFound, ExNotEnoughSeats, ExTableIsReserved,
            ExAssignedTableAlready {
        if (cmdParts.length < 4)
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
            throw new ExAssignedTableAlready();

        int totalTableCapacity = 0;

        foundTables = new ArrayList<>();
        for (int i = 3; i < cmdParts.length; i++) {
            String sTableCode = cmdParts[i];

            Table table = TableManager.getInstance().findTable(sDateDine, sTableCode);
            if (table == null)
                throw new ExTableCodeNotFound(sTableCode); // Abort!!

            if (table.isAssigned())
                throw new ExTableIsReserved(sTableCode); // Abort!!

            totalTableCapacity += table.getCapacity();

            foundTables.add(table);
        }

        if (totalTableCapacity < reservation.getTotPersons())
            throw new ExNotEnoughSeats();

        // Assign the tables found to the reservation
        for (Table table : foundTables) {
            table.setAssigned(true);
            table.setTicketCode(reservation.getTicketCode());
        }

        // Set the status of the reservation as allocated
        RStateTableAllocated stateTableAllocated = new RStateTableAllocated(foundTables);
        reservation.setStatus(stateTableAllocated);

        addUndoCommand(this);
        clearRedoList();

        System.out.println("Done.");
    }

    @Override
    public void undoMe() {
        // Remove table assignments
        for (Table table : foundTables)
            table.release();

        reservation.setStatus(new RStatePending());

        addRedoCommand(this);
    }

    @Override
    public void redoMe() {
        // Assign the tables found to the reservation
        for (Table table : foundTables) {
            table.setAssigned(true);
            table.setTicketCode(reservation.getTicketCode());
        }

        // Set the status of the reservation as allocated
        RStateTableAllocated stateTableAllocated = new RStateTableAllocated(foundTables);
        reservation.setStatus(stateTableAllocated);

        addUndoCommand(this);
    }
}
