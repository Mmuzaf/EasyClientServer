package easycs.io;

/**
 * @author Mmuzafarov
 */
public enum Command {
    ONLINE("ONLINE"),
    EXPAND("EXPAND"),
    EXIT("EXIT"),
    HELP("HELP");

    private final String name;

    Command(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Command getByName(String name) {
        for (Command command : Command.values()) {
            if(command.name.equalsIgnoreCase(name)) {
                return command;
            }
        }
        return HELP;
    }

}
