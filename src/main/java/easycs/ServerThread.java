package easycs;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Queues;
import easycs.config.Constant;
import easycs.data.ClientMetaData;
import easycs.data.Message;
import easycs.io.Command;
import easycs.network.SocketChannelClosable;
import easycs.network.SocketChannelFactory;
import easycs.task.AsyncWorkerThread;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.List;
import java.util.Queue;


/**
 * @author Mmuzafarov
 */
public class ServerThread implements Runnable, AsyncWorkerThread.CallBackServerThread<List<Integer>> {
    private final static Log logger = LogFactory.getLog(ServerThread.class);
    private final static Queue<ServerThread> handler = Queues.newConcurrentLinkedQueue();

    private final SocketChannelClosable channel;

    protected ClientMetaData clientMetaData;
    protected boolean authSuccess;

    public ServerThread(SocketChannelClosable channel) throws IOException {
        this.channel = channel;
        handler.add(this);
    }

    @Override
    public void run() {
        Message message;
        clientAuthentication();

        try {
            while (authSuccess) {
                message = channel.readObject();
                if (message.getBody().charAt(0) == Constant.COMMAND_START_WITH_CHAR) {
                    sendUnicast(getCommandResult(message.getBody().substring(1)));
                } else
                    broadcastMessage(message);
            }
        } catch (Exception e) {
            logger.error("Unknown " + e.getCause());
        } finally {
            clientDisconnect(authSuccess);
        }
    }

    protected static void broadcastServerMessage(String message) {
        logger.debug("Send system broadcast message: " + message);
        broadcastMessage(getServerMessageInstance(message));
    }

    protected static void broadcastMessage(Message message) {
        logger.debug("Send broadcast message: " + message);
        for (ServerThread connection : handler) {
            try {
                connection.getChannel().writeObject(message);
            } catch (IOException e) {
                logger.warn("Exception on broadMessage()" + e.toString());
                connection.close();
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
        return Message.getNewInstance(Constant.SERVER_NAME, body);
    }

    protected void clientAuthentication() {
        try {
            clientMetaData = channel.getUser();

            if (clientMetaData.getClientName().equalsIgnoreCase(Constant.SERVER_NAME))
                authSuccess = false;

            for (ServerThread runnable : handler) {
                if (runnable.clientMetaData.getClientName().equalsIgnoreCase(clientMetaData.getClientName()) && runnable != this) {
                    sendUnicast(String.format("Client with given name = [%s] already exists", clientMetaData.getClientName()));
                    authSuccess = false;
                }
            }

            sendUnicast(String.format(Constant.WELCOME_MESSAGE, clientMetaData.getClientName()));
            broadcastServerMessage(String.format(Constant.JOIN_MESSAGE, clientMetaData.getClientName()));

            authSuccess = true;
        } catch (IOException e) {
            logger.info(e.getMessage());
            authSuccess = false;
        }
    }

    protected void clientDisconnect(boolean waslogin) {
        try {
            if (waslogin)
                broadcastServerMessage(String.format(Constant.DISCONNECT_MESSAGE, clientMetaData.getClientName()));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        close();
        handler.remove(this);
        Thread.currentThread().interrupt();
    }

    public String getCommandResult(String command) {

        String result;
        final Iterable<String> commandArgs = Splitter.on(" ").trimResults().split(command);
        switch (Command.getByName(Iterables.getFirst(commandArgs, Command.HELP.toString()))) {
            case ONLINE:
                result = String.format(Constant.ONLINE_COMMAND_MSG, Integer.toString(handler.size()));
                break;
            case EXPAND:
                AsyncWorkerThread workerThread = new AsyncWorkerThread<Integer, List<Integer>>(this) {
                    private final List<Integer> threadResult = Lists.newArrayList();

                    void recursiveCalculation(int n, int k) {
                        // k - additional parameter with start-recursion value
                        if (k > n / 2) {
                            threadResult.add(n);
                            return;
                        }
                        // Step of recursion / recursive condition
                        if (n % k == 0) {
                            threadResult.add(k);
                            recursiveCalculation(n / k, k);
                        } else {
                            recursiveCalculation(n, ++k);
                        }
                    }

                    @Override
                    protected List<Integer> doInBackground(Integer... params) {
                        recursiveCalculation(Integer.valueOf(Lists.newArrayList(commandArgs).get(1)), 2);
                        return threadResult;
                    }
                }.execute();
                result = "Created AsyncWorkerThread for computing: " + workerThread;
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

    public SocketChannelClosable getChannel() {
        return channel;
    }

    protected void close() {
        try {
            if (channel != null)
                channel.close();
        } catch (IOException e) {
            logger.error(e.getCause());
        }
    }

    @Override
    public void pushBackResult(List<Integer> result) {
        try {
            channel.writeObject(getServerMessageInstance(result.toString()));
        } catch (IOException e) {
            logger.error(e.getCause());
        }
    }

    @Override
    public String toString() {
        return "ServerThread{" +
                "channel=" + channel +
                ", clientMetaData=" + clientMetaData +
                '}';
    }
}
