import java.util.Objects;

public class Reservation implements Comparable<Reservation> {
    private String guestName;
    private String phoneNumber;
    private int totPersons;
    private Day dateDine;
    private Day dateRequest;
    private int ticketCode;
    private RState status;

    public Reservation(String guestName, String phoneNumber, int totPersons, String sDateDine) {
        this(guestName, phoneNumber, totPersons, new Day(sDateDine));
    }

    public Reservation(String guestName, String phoneNumber, int totPersons, Day dateDine) {
        this.guestName = guestName;
        this.phoneNumber = phoneNumber;
        this.totPersons = totPersons;
        this.dateDine = dateDine;
        this.dateRequest = SystemDate.getInstance().clone();
        this.ticketCode = -1;
        this.status = new RStatePending();
    }

    public static String getListingHeader() {
        return String.format("%-13s%-11s%-14s%-25s%-11s%s", "Guest Name", "Phone",
                "Request Date", "Dining Date and Ticket", "#Persons", "Status");
    }

    public Day getDateDine() {
        return dateDine;
    }

    public int getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(int ticketCode) {
        this.ticketCode = ticketCode;
    }

    public void setStatus(RState status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("%-13s%-11s%-14s%-25s%4d       %s",
                guestName,
                phoneNumber,
                dateRequest,
                dateDine + String.format(" (Ticket %d)", ticketCode),
                totPersons,
                status.getName());
    }

    // The Assignment PDF file stated that 2 reservations having the same "guestName" and "dateDine"
    // are considered the same.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(guestName, that.guestName) &&
                Objects.equals(dateDine, that.dateDine);
    }

    // The Assignment PDF file stated that 2 reservations having the same "guestName" and "dateDine"
    // are considered the same.
    @Override
    public int hashCode() {
        return Objects.hash(guestName, dateDine);
    }

    @Override
    public int compareTo(Reservation o) {
        int c = this.guestName.compareTo(o.guestName); // Sort by "guestName" first
        if (c == 0)
            c = this.phoneNumber.compareTo(o.phoneNumber); // Then sort by "phoneNumber"
        if (c == 0)
            c = this.dateDine.compareTo(o.dateDine); // Then sort by "dateDine"

        return c;
    }
}
