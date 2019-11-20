import java.util.Objects;

public class Day implements Cloneable, Comparable<Day> {
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

    public boolean hasPassed(Day thatDay) {
        if (this.year < thatDay.year) {
            return true;
        } else if (this.year == thatDay.year) {
            if (this.month < thatDay.month) {
                return true;
            } else if (this.month == thatDay.month) {
                return this.day < thatDay.day;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return day + "-" + MonthNames.substring((month - 1) * 3, month * 3) + "-" + year;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Day day1 = (Day) o;
        return year == day1.year &&
                month == day1.month &&
                day == day1.day;
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month, day);
    }

    @Override
    public int compareTo(Day o) {
        int c = Integer.compare(this.year, o.year);
        if (c == 0)
            c = Integer.compare(this.month, o.month);
        if (c == 0)
            c = Integer.compare(this.day, o.day);

        return c;
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