package easycs.io;

/**
 * @author Mmuzafarov
 */
public enum Command {
    ONLINE,
    EXPAND,
    EXIT,
    HELP;

    public static Command getByName(String name) {
        for (Command command : Command.values()) {
            if(command.toString().equalsIgnoreCase(name)) {
                return command;
            }
        }
        return HELP;
    }

}
