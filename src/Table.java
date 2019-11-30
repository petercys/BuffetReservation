import java.util.Objects;

public class Table {
    private String tableDate;
    private int capacity;
    private char codeInitial;
    private int codeNum;
    private boolean isAssigned;
    private int ticketCode;

    public Table(String tableDate, int capacity, char codeInitial, int codeNum) {
        this.tableDate = tableDate;
        this.capacity = capacity;
        this.codeInitial = codeInitial;
        this.codeNum = codeNum;
        this.isAssigned = false;
        this.ticketCode = -1;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCodeNum() {
        return codeNum;
    }

    public boolean isAssigned() {
        return isAssigned;
    }

    public void setAssigned(boolean isAssigned) {
        this.isAssigned = isAssigned;
    }

    public int getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(int ticketCode) {
        this.ticketCode = ticketCode;
    }

    /**
     * Return the table code
     * Format: YXX, where Y is the code initial and XX is the code number
     *
     * @return the table code
     */
    public String getCode() {
        return codeInitial + String.format("%02d", codeNum);
    }

    /**
     * Make the table available and reset its ticketCode
     */
    public void release() {
        this.isAssigned = false;
        this.ticketCode = -1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Table table = (Table) o;
        return capacity == table.capacity &&
                codeInitial == table.codeInitial &&
                codeNum == table.codeNum &&
                Objects.equals(tableDate, table.tableDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableDate, capacity, codeInitial, codeNum);
    }

    @Override
    public String toString() {
        return "Table{" +
                "tableDate='" + tableDate + '\'' +
                ", capacity=" + capacity +
                ", codeInitial=" + codeInitial +
                ", codeNum=" + codeNum +
                ", isAssigned=" + isAssigned +
                ", ticketCode=" + ticketCode +
                '}';
    }
}
