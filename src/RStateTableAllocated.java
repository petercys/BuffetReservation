import java.util.List;

public class RStateTableAllocated implements RState {
    private List<Table> allocatedTables;

    public RStateTableAllocated(List<Table> allocatedTables) {
        this.allocatedTables = allocatedTables;
    }

    public List<Table> getAllocatedTables() {
        return allocatedTables;
    }

    @Override
    public String getNameAndDescription() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Table assigned: ");

        if (allocatedTables == null) {
            stringBuilder.append("[None]");
        } else {
            for (Table table : allocatedTables)
                stringBuilder.append(table.getCode()).append(" ");
        }

        return stringBuilder.toString();
    }
}
