import java.util.ArrayList;
import java.util.List;

public abstract class RecordedCommand implements Command {
    private static final List<RecordedCommand> undoList = new ArrayList<>();
    private static final List<RecordedCommand> redoList = new ArrayList<>();

    protected static void addUndoCommand(RecordedCommand cmd) {
        undoList.add(cmd);
    }

    protected static void addRedoCommand(RecordedCommand cmd) {
        redoList.add(cmd);
    }

    protected static void clearRedoList() {
        redoList.clear();
    }

    public static void undoOneCommand() {
        if (undoList.isEmpty())
            System.out.println("Nothing to undo.");
        else
            undoList.remove(undoList.size() - 1).undoMe();
    }

    public static void redoOneCommand() {
        if (redoList.isEmpty())
            System.out.println("Nothing to redo.");
        else
            redoList.remove(redoList.size() - 1).redoMe();
    }

    public abstract void undoMe();

    public abstract void redoMe();
}
