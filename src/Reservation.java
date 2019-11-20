public class Reservation implements Comparable<Reservation> {
    private String guestName;
    private String phoneNumber;
    private int totPersons;
    private Day dateDine;
    private Day dateRequest;
    private int ticketCode;

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
    }

    public Day getDateDine() {
        return dateDine;
    }

    public void setDateDine(Day dateDine) {
        this.dateDine = dateDine;
    }

    public int getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(int ticketCode) {
        this.ticketCode = ticketCode;
    }

    public static String getListingHeader() {
        return String.format("%-13s%-11s%-14s%-14s%s", "Guest Name", "Phone", "Request Date",
                "Dining Date", "#Persons");
    }

    @Override
    public String toString() {
        return String.format("%-13s%-11s%-14s%-14s%5d", guestName, phoneNumber, dateRequest,
                dateDine, totPersons);
    }

    @Override
    public int compareTo(Reservation o) {
        return this.guestName.compareTo(o.guestName);
    }
}
