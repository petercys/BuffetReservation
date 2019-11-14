public class CmdListReservations implements Command {
    @Override
    public void execute(String[] cmdParts) {
        BookingOffice.getInstance().listReservations();
    }
}
