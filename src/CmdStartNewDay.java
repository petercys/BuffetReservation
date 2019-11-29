public class CmdStartNewDay extends RecordedCommand {
    private String originalDate;
    private String newDate;

    @Override
    public void execute(String[] cmdParts) {
        // No need to create a new subclass of "Exception".
        // Also no need to throw an exception.
        // Just print the error message and return is ok la.
        if (cmdParts.length < 2) {
            System.out.println("Insufficient command arguments!");
            return;
        }

        originalDate = SystemDate.getInstance().toString();
        newDate = cmdParts[1];

        SystemDate.getInstance().set(newDate);

        addUndoCommand(this);
        clearRedoList();

        System.out.println("Done.");
    }

    @Override
    public void undoMe() {
        SystemDate.getInstance().set(originalDate);
        addRedoCommand(this);
    }

    @Override
    public void redoMe() {
        SystemDate.getInstance().set(newDate);
        addUndoCommand(this);
    }
}
