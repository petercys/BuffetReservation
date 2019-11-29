public interface Command {
    void execute(String[] cmdParts) throws ExInsufficientArgs;
}
