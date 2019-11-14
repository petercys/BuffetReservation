public class Day implements Cloneable {
    private static final String MonthNames = "JanFebMarAprMayJunJulAugSepOctNovDec";
    private int year;
    private int month;
    private int day;

    public Day(String sDay) {
        set(sDay);
    }

    public Day(int y, int m, int d) {
        this.year = y;
        this.month = m;
        this.day = d;
    }

    /**
     * Set the date
     *
     * @param sDay date in dd-MMM-yyyy format
     */
    public void set(String sDay) {
        // P.S.: It would be better if we use DateFormat classes in the Java SDK to parse the date string

        String[] sDayParts = sDay.split("-");
        this.day = Integer.parseInt(sDayParts[0]);
        this.year = Integer.parseInt(sDayParts[2]);
        this.month = MonthNames.indexOf(sDayParts[1]) / 3 + 1;
    }

    @Override
    public String toString() {
        return day + "-" + MonthNames.substring((month - 1) * 3, month * 3) + "-" + year;
    }

    @Override
    public Day clone() {
        Day copy = null;
        try {
            copy = (Day) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return copy;
    }
}