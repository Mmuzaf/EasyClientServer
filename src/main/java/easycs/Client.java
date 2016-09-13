package easycs;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import easycs.data.ClientMetaData;
import easycs.data.Message;
import easycs.io.Keyboard;
import easycs.network.SocketChannelClosable;
import easycs.network.SocketChannelFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.UUID;

/**
 * @author Mmuzafarov
 */
public class Client {
    private final static Log logger = LogFactory.getLog(Client.class);

    private final Integer port;
    private final String host;
    private final ClientMetaData clientMetaData;

    public Client(String host, Integer port) {
        this.host = host;
        this.port = port;
        clientMetaData = ClientMetaData.getRandomClientMetaData();
    }

    public void start() {
        try (SocketChannelClosable channel =
                     SocketChannelFactory.getClientSocketChannelInstance(host, port)
        ) {
            // Authorization
            System.out.println(channel.toString());
            channel.sendUser(clientMetaData);
            System.out.println(channel.readObject().toString());

            showInstructions();

            Thread rx = new Thread(new ClientThread(channel));
            rx.start();
            while (rx.isAlive()) {
                String in = Keyboard.readLine();
                logger.debug("Read keyboard input: " + in);
                if (!Strings.isNullOrEmpty(in)) {
                    channel.writeObject(Message.getNewInstance(clientMetaData.getClientName(), in));
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
            logger.error("Exception in Client: " + e.getMessage());
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
