public class Reservation implements Comparable<Reservation> {
    private String guestName;
    private String phoneNumber;
    private int totPersons;
    private Day dateDine;
    private Day dateRequest;

    public Reservation(String guestName, String phoneNumber, int totPersons, String sDateDine) {
        this.guestName = guestName;
        this.phoneNumber = phoneNumber;
        this.totPersons = totPersons;
        this.dateDine = new Day(sDateDine);
        this.dateRequest = SystemDate.getInstance().clone();
    }

    public static String getListingHeader() {
        return String.format("%-13s%-11s%-14s%-14s%s", "Guest Name", "Phone", "Request Date",
                "Dining Date", "#Persons");
    }

    @Override
    public String toString() {
        //Learn: "-" means left-aligned
        return String.format("%-13s%-11s%-14s%-14s%5d", guestName, phoneNumber, dateRequest,
                dateDine, totPersons);
    }

    @Override
    public int compareTo(Reservation o) {
        return this.guestName.compareTo(o.guestName);
    }
}
