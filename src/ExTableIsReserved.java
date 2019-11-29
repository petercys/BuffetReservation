public class ExTableIsReserved extends Exception {
    public ExTableIsReserved(String tableCode) {
        super(String.format("Table %s is already reserved by another booking!", tableCode));
    }
}
