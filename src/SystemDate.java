public class SystemDate extends Day {
    private static SystemDate instance;

    private SystemDate(String sDay) {
        super(sDay);
    }

    /**
     * Get the instance of SystemDate (Singleton)
     *
     * @return instance of itself
     */
    public static SystemDate getInstance() {
        return instance;
    }

    /**
     * Create a instance of SystemDate (Singleton)
     *
     * @param sDay system start date
     */
    public static void createTheInstance(String sDay) {
        if (instance == null)
            instance = new SystemDate(sDay);
        else
            System.out.println("Cannot create one more system date instance.");
    }
}