import java.util.List;

public class CmdListTableAllocations implements Command {
    @Override
    public void execute(String[] cmdParts) throws ExInsufficientArgs {
        if (cmdParts.length < 2)
            throw new ExInsufficientArgs();

        String dateDine = cmdParts[1];

        TableManager.getInstance().listTableAllocations(dateDine);

        System.out.println("\n");

        List<Reservation> pendingReservations =
                BookingOffice.getInstance().getPendingReservations(dateDine);

        int totalNumOfPersons = 0;
        for (Reservation reservation : pendingReservations)
            totalNumOfPersons += reservation.getTotPersons();

        System.out.println(String.format("Total number of pending requests = %d " +
                        "(Total number of persons = %d)",
                pendingReservations.size(), totalNumOfPersons));
    }
}
