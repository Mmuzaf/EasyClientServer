package easycs;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Queues;
import easycs.config.Constant;
import easycs.data.ClientMetaData;
import easycs.data.Message;
import easycs.io.Command;
import easycs.network.IOSocketChannel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.Queue;


/**
 * @author Mmuzafarov
 */
public class ServerRunnable implements Runnable {
    private final static Log logger = LogFactory.getLog(ServerRunnable.class);
    private final static Queue<ServerRunnable> handler = Queues.newConcurrentLinkedQueue();

    private final IOSocketChannel channel;
    private ClientMetaData clientMetaData;

    public ServerRunnable(Socket socket) throws IOException {
        this.channel = new IOSocketChannel(socket);
        handler.add(this);
    }

    @Override
    public void run() {
        Message message;
        boolean waiting = clientAuthentication();

        try {
            while (waiting) {
                message = channel.readObject();
                if (message.getBody().charAt(0) == '/') {
                    sendUnicast(getCommandResult(message.getBody().substring(1)));
                } else
                    broadcastMessage(message);
            }
        } catch (Exception e) {
            logger.error("Unknown " + e.getMessage());
        } finally {
            clientDisconnect(waiting);
        }
    }

    protected static void broadcastServerMessage(String message) {
        logger.debug("Send system broadcast message: " + message);
        broadcastMessage(getServerMessageInstance(message));
    }

    protected static void broadcastMessage(Message message) {
        logger.debug("Send broadcast message: " + message);
        for (ServerRunnable connection : handler) {
            try {
                connection.getChannel().writeObject(message);
            } catch (IOException e) {
                logger.warn("Exception on broadMessage()" + e.toString());
                connection.getChannel().close();
            }
        }
    }

    protected void sendUnicast(String body) {
        logger.debug("Send system unicast message: " + body);
        try {
            channel.writeObject(getServerMessageInstance(body));
        } catch (IOException ce) {
            logger.error(ce.getMessage());
            clientDisconnect(true);
        }

    }

    protected static Message getServerMessageInstance(String body) {
        return Message.getInstance(Constant.SERVER_NAME, body);
    }

    protected boolean clientAuthentication() {
        try {
            clientMetaData = channel.getUser();

            if (clientMetaData.getClientName().equalsIgnoreCase(Constant.SERVER_NAME))
                return false;

            for (ServerRunnable runnable : handler) {
                if (runnable.clientMetaData.getClientName().equalsIgnoreCase(clientMetaData.getClientName()) && runnable != this) {
                    sendUnicast(String.format("Client with given name = [%s] already exists", clientMetaData.getClientName()));
                    return false;
                }
            }

            sendUnicast(String.format(Constant.WELCOME_MESSAGE, clientMetaData.getClientName()));
            broadcastServerMessage(String.format(Constant.JOIN_MESSAGE, clientMetaData.getClientName()));

            return true;
        } catch (IOException e) {
            logger.info(e.getMessage());
            return false;
        }
    }

    protected void clientDisconnect(boolean waslogin) {
        try {
            if (waslogin)
                broadcastServerMessage(String.format(Constant.DISCONNECT_MESSAGE, clientMetaData.getClientName()));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        channel.close();
        handler.remove(this);
        Thread.currentThread().interrupt();
    }

    public static String getCommandResult(String command) {

        String result;
        Iterable<String> commandArgs = Splitter.on(" ").trimResults().split(command);
        switch (Command.getByName(Iterables.getFirst(commandArgs, Command.HELP.getName()))) {
            case ONLINE:
                result = "There are " + Integer.toString(handler.size()) + " clients online";
                break;
            case EXPAND:
                result = "Execute action";
                break;
            case EXIT:
                result = Constant.BYE_MESSAGE;
                break;
            case HELP:
                result = "\nAvailable commands:\n" +
                        "/online [number of connected clients]\n" +
                        "/expand [expand number into suppliers]\n" +
                        "/exit [disconnect normally from server]\n" +
                        "/help [this message]\n";
                break;
            default:
                result = null;
        }
        return result;
    }

    public IOSocketChannel getChannel() {
        return channel;
    }

    @Override
    public String toString() {
        return "ServerRunnable{" +
                "channel=" + channel +
                ", clientMetaData=" + clientMetaData +
                '}';
    }
}
