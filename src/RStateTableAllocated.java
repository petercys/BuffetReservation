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
        StringBuilder outputStrBuilder = new StringBuilder();
        outputStrBuilder.append("Table assigned: ");

        if (allocatedTables == null) {
            outputStrBuilder.append("[None]");
        } else {
            for (Table table : allocatedTables)
                outputStrBuilder.append(table.getCode()).append(" ");
        }

        return outputStrBuilder.toString();
    }
}
