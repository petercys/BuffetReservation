import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class TableManager {
    private static volatile TableManager mInstance;
    private HashMap<String, List<Table>> dateTableMap;

    public TableManager() {
        dateTableMap = new HashMap<>();
    }

    /**
     * Get the instance of TableManager
     *
     * @return the instance of TableManager
     */
    public static TableManager getInstance() {
        // Double-Checked Locking is being used instead of Initialization-on-demand holder idiom
        TableManager instance = mInstance;
        if (instance == null) {
            synchronized (TableManager.class) {
                instance = mInstance;
                if (instance == null) {
                    mInstance = instance = new TableManager();
                }
            }
        }
        return instance;
    }

    /**
     * Initialize tables for date
     *
     * @param dateString the target dining date
     */
    public void initAllTablesForDate(String dateString) {
        if (!dateTableMap.containsKey(dateString))
            dateTableMap.put(dateString, initAllTables(dateString));
    }

    private List<Table> initAllTables(String tableDate) {
        List<Table> tables = new ArrayList<>();

        // 10 tables for 2 persons
        for (int i = 0; i < 10; i++)
            tables.add(new Table(tableDate, 2, 'T', i + 1));

        // 4 tables for 4 persons
        for (int i = 0; i < 6; i++)
            tables.add(new Table(tableDate, 4, 'F', i + 1));

        // 3 tables for 8 persons
        for (int i = 0; i < 3; i++)
            tables.add(new Table(tableDate, 8, 'H', i + 1));

        return tables;
    }

    /**
     * Get all tables on a specific date
     *
     * @param dateString target dining date
     * @return all tables on a specific date, regardless of availability
     */
    public List<Table> getAllTablesForDate(String dateString) {
        return dateTableMap.get(dateString);
    }

    /**
     * Find a table with table code on the target dining date
     *
     * @param dateString the target dining date
     * @param code the table code
     * @return the table found, null if no table is found
     */
    public Table findTable(String dateString, String code) {
        if (dateTableMap.containsKey(dateString)) {
            for (Table table : dateTableMap.get(dateString)) {
                if (table.getCode().equals(code))
                    return table;
            }
        }

        return null;
    }

    /**
     * List allocated and available tables
     *
     * @param dateDine target dining date
     */
    public void listTableAllocations(String dateDine) {
        List<Table> tables = getAllTablesForDate(dateDine);
        List<Table> allocatedTables = new ArrayList<>();
        List<Table> availableTables = new ArrayList<>();

        if (tables != null) {
            for (Table t : tables) {
                if (t.isAssigned())
                    allocatedTables.add(t);
                else
                    availableTables.add(t);
            }

            // (iii) For listing of table allocations, the allocated tables and available tables should
            // be sorted by table size, and then by table code.
            Comparator<Table> sizeThenCodeTableComparator = new Comparator<Table>() {
                @Override
                public int compare(Table o1, Table o2) {
                    int c = Integer.compare(o1.getCapacity(), o2.getCapacity());

                    if (c == 0)
                        c = Integer.compare(o1.getCodeNum(), o2.getCodeNum());

                    return c;
                }
            };

            if (!allocatedTables.isEmpty())
                allocatedTables.sort(sizeThenCodeTableComparator);

            if (!availableTables.isEmpty())
                availableTables.sort(sizeThenCodeTableComparator);
        }

        System.out.println("Allocated tables: ");
        if (allocatedTables.isEmpty()) {
            System.out.println("[None]");
        } else {
            for (Table t : allocatedTables) {
                System.out.println(String.format("%s (Ticket %d)",
                        t.getCode(), t.getTicketCode()));
            }
        }

        System.out.println("\nAvailable tables: ");
        if (availableTables.isEmpty()) {
            System.out.println("[None]");
        } else {
            for (Table t : availableTables)
                System.out.print(t.getCode() + " ");
        }
    }
}
