public interface Command {
    void execute(String[] cmdParts) throws ExInsufficientArgs, ExBookingNotFound,
            ExDateHasAlreadyPassed, ExTableCodeNotFound, ExNotEnoughSeats, ExTableIsReserved,
            ExAssignedTableAlready;
}
