package easycs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author Mmuzafarov
 */
public class Server {
    private final static Log logger = LogFactory.getLog(Server.class);

    private final Integer port;

    public Server(Integer port) {
        this.port = port;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            new Thread(new ServerThread(serverSocket.accept())).start();
        } catch (IOException e) {
            logger.error("Could not listen on port [" + port + "] .Check port availability and internet connection");
            System.exit(-1);
        }

    }

}
