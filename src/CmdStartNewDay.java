public class CmdStartNewDay extends RecordedCommand {
    private String originalDate;
    private String newDate;

    @Override
    public void execute(String[] cmdParts) throws ExInsufficientArgs {
        if (cmdParts.length < 2)
            throw new ExInsufficientArgs();

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
