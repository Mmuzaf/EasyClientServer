package easycs;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import easycs.config.Constant;
import easycs.data.ClientMetaData;
import easycs.data.Message;
import easycs.io.Keyboard;
import easycs.network.IOSocketChannel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.UUID;

/**
 * @author Mmuzafarov
 */
public class Client {
    private final static Log logger = LogFactory.getLog(Client.class);

    private IOSocketChannel channel;
    private final Integer port;
    private final String host;
    private final ClientMetaData clientMetaData;

    public Client(String host, Integer port) {
        this.host = host;
        this.port = port;
        clientMetaData = new ClientMetaData(UUID.randomUUID().toString());
    }

    public void start() {
        try (Socket socket = new Socket();) {
            socket.connect(
                    new InetSocketAddress(
                            host == null ? Constant.DEFAULT_HOST : host,
                            port == null ? Constant.DEFAULT_PORT : port
                    ),
                    2000
            );
            channel = new IOSocketChannel(socket);

            // Authorization
            channel.sendUser(clientMetaData);
            System.out.println(channel.readObject().toString());
            channel.isClosed();

            showInstructions();

            Thread rx = new Thread(new ClientRunnable(channel));
            rx.start();
            while (rx.isAlive()) {
                String in = Keyboard.readLine();
                if (!Strings.isNullOrEmpty(in)) {
                    Message message = new Message(clientMetaData.getClientName(), in);
                    channel.writeObject(message);
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            channel.close();
        }

    }

    private void showInstructions() {
        System.out.println("\nClient = [" + this.toString() + "]; connected to connect " + host + ":" + port + ";");
        System.out.println("\nUsage: /expand [your_number]");
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("port", port)
                .add("host", host)
                .add("clientMetaData", clientMetaData)
                .toString();
    }
}
