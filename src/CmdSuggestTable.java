import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CmdSuggestTable implements Command {
    @Override
    public void execute(String[] cmdParts) throws ExInsufficientArgs, ExDateHasAlreadyPassed,
            ExBookingNotFound, ExAssignedTableAlready {
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

        Reservation reservation = BookingOffice.getInstance().searchReservation(sDateDine, ticketCode);
        if (reservation == null)
            throw new ExBookingNotFound();

        RState status = reservation.getStatus();
        if (status instanceof RStateTableAllocated)
            throw new ExAssignedTableAlready();

        int totalPersons = reservation.getTotPersons();

        List<Table> suggestedTables = new ArrayList<>();
        List<Table> tablesForEight = new ArrayList<>();
        List<Table> tablesForFour = new ArrayList<>();
        List<Table> tablesForTwo = new ArrayList<>();
        List<Table> availableTables = getAllAvailableTablesForDate(sDateDine);
        availableTables.sort(new Comparator<Table>() {
            @Override
            public int compare(Table o1, Table o2) {
                int c = Integer.compare(o1.getCapacity(), o2.getCapacity());

                if (c == 0)
                    c = Integer.compare(o1.getCodeNum(), o2.getCodeNum());

                return c;
            }
        });

        int totalSeatsAvailable = 0;
        for (Table availableTable : availableTables)
            totalSeatsAvailable += availableTable.getCapacity();

        if (totalSeatsAvailable < totalPersons) {
            System.out.println(String.format("Suggestion for %d persons: Not enough tables",
                    totalPersons));
            return;
        }

        for (Table table : availableTables) {
            switch (table.getCapacity()) {
                case 8:
                    tablesForEight.add(table);
                    break;
                case 4:
                    tablesForFour.add(table);
                    break;
                case 2:
                    tablesForTwo.add(table);
                    break;
                default:
                    break;
            }
        }

        if (!availableTables.isEmpty()) {
            int remainingPersons = totalPersons;

            if (remainingPersons % 2 != 0)
                remainingPersons++;

            if (remainingPersons >= 8) {
                int x = remainingPersons / 8;
                int tableForEightNeeded = Math.min(x, tablesForEight.size());
                //System.out.println("tableForEightNeeded = " + tableForEightNeeded);

                for (int i = 0; i < tableForEightNeeded; i++)
                    suggestedTables.add(tablesForEight.get(i));

                remainingPersons -= tableForEightNeeded * 8;
            }


            if (remainingPersons >= 4) {
                int x = remainingPersons / 4;
                int tableForFourNeeded = Math.min(x, tablesForFour.size());
                //System.out.println("tableForFourNeeded = " + tableForFourNeeded);

                for (int i = 0; i < tableForFourNeeded; i++)
                    suggestedTables.add(tablesForFour.get(i));

                remainingPersons -= tableForFourNeeded * 4;
            }

            if (remainingPersons >= 2) {
                int x = remainingPersons / 2;
                int tableForTwoNeeded = Math.min(x, tablesForTwo.size());
                //System.out.println("tableForTwoNeeded = " + tableForTwoNeeded);

                for (int i = 0; i < tableForTwoNeeded; i++)
                    suggestedTables.add(tablesForTwo.get(i));

                remainingPersons -= tableForTwoNeeded * 2;
            }
        }

        suggestedTables.sort(new Comparator<Table>() {
            @Override
            public int compare(Table o1, Table o2) {
                // "For listing of suggested tables for a booking (command suggestTable), the tables
                // should be sorted by table size in descending order, then by table code in
                // ascending order."

                // Sort by table size in descending order first
                int c = Integer.compare(o2.getCapacity(), o1.getCapacity());

                if (c == 0)
                    c = Integer.compare(o1.getCodeNum(), o2.getCodeNum());

                return c;
            }
        });

        StringBuilder stringBuilder = new StringBuilder();
        if (suggestedTables.isEmpty()) {
            stringBuilder.append("[None]");
        } else {
            for (Table t : suggestedTables)
                stringBuilder.append(t.getCode()).append(" ");
        }

        System.out.println(String.format("Suggestion for %d persons: %s", totalPersons, stringBuilder.toString()));
    }

    private List<Table> getAllAvailableTablesForDate(String date) {
        List<Table> availableTables = new ArrayList<>();
        List<Table> allTablesForDate = TableManager.getInstance().getAllTablesForDate(date);

        if (allTablesForDate != null) {
            for (Table table : allTablesForDate) {
                if (!table.isAssigned())
                    availableTables.add(table);
            }
        }

        return availableTables;
    }
}
