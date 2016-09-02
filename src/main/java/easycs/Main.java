package easycs;

/**
 * @author Mmuzafarov
 */
public class Main {

    public static void main(String[] args) {
        int port;
        String host;

        if (args.length == 2) {
            if (args[0].equals("-l")) {
                try {
                    port = Integer.parseInt(args[1]);
                    new Server(port).start();
                } catch (NumberFormatException e) {
                    showInstructions();
                    System.exit(1);
                }
            }
        } else if (args.length == 3) {
            try {
                host = args[1];
                port = Integer.parseInt(args[2]);
                new Client(host, port).start();
            } catch (NumberFormatException e) {
                showInstructions();
                System.exit(1);
            }
        } else {
            showInstructions();
            System.exit(1);
        }


    }

    static void showInstructions() {
        System.out.println("\nCommand-line args examples:");
        System.out.println("\nCLIENT: -c host port");
        System.out.println("\nSERVER: -l port");
    }

}
