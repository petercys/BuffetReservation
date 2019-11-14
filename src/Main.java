import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner inConsole = new Scanner(System.in);

        System.out.print("Please input the file pathname: ");
        String filePathname = inConsole.nextLine();

        Scanner inFile = new Scanner(new File(filePathname));

        // The first command in the file must be to set the system date
        // (eg. "startNewDay 03-Jan-2018"); and it cannot be undone
        String firstCmdLine = inFile.nextLine();

        //Split by vertical bar character '|' (Regular expression: "\|")
        String[] firstCmdLineParts = firstCmdLine.split("\\|");
        System.out.println("\n> " + firstCmdLine);

        SystemDate.createTheInstance(firstCmdLineParts[1]);

        while (inFile.hasNext()) {
            String cmdLine = inFile.nextLine().trim();

            //Blank lines exist in data file as separators.  Skip them.
            if (cmdLine.trim().isEmpty())
                continue;

            System.out.println("\n> " + cmdLine);

            String[] cmdParts = cmdLine.split("\\|");
            String cmd = cmdParts[0];

            switch (cmd) {
                case "request":
                    (new CmdRequest()).execute(cmdParts);
                    break;
                case "listReservations":
                    (new CmdListReservations()).execute(cmdParts);
                    break;
                case "startNewDay":
                    (new CmdStartNewDay()).execute(cmdParts);
                    break;
                case "undo":
                    RecordedCommand.undoOneCommand();
                    break;
                case "redo":
                    RecordedCommand.redoOneCommand();
                    break;
                default:
                    break;
            }
        }

        inFile.close();
        inConsole.close();
    }
}
