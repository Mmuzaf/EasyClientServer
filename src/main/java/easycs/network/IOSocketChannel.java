package easycs.network;

import com.google.common.base.MoreObjects;
import easycs.data.ClientMetaData;
import easycs.data.Message;
import easycs.exception.ChannelClosedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * @author Mmuzafarov
 */
public class IOSocketChannel implements Closeable {
    private final static Log logger = LogFactory.getLog(IOSocketChannel.class);

    private final Socket socket;
    private final ObjectOutputStream oBuffer;
    private final ObjectInputStream iBuffer;

    public IOSocketChannel(Socket socket) throws IOException {
        this.socket = socket;
        this.oBuffer = new ObjectOutputStream(socket.getOutputStream());
        this.iBuffer = new ObjectInputStream(socket.getInputStream());
    }

    public synchronized void writeObject(Message message) throws IOException {
        closeChannelIfSocketClosed();
        oBuffer.writeObject(message);
    }

    public Message readObject() throws IOException {
        closeChannelIfSocketClosed();
        Message message;
        try {
            message = (Message) iBuffer.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("Lost connection", e);
        }
        return message;
    }


    public void sendUser(ClientMetaData clientMetaData) throws IOException {
        closeChannelIfSocketClosed();
        oBuffer.writeObject(clientMetaData);
    }

    public ClientMetaData getUser() throws IOException {
        ClientMetaData clientMetaData = new ClientMetaData("Unknown");
        closeChannelIfSocketClosed();
        try {
            clientMetaData = (ClientMetaData) iBuffer.readObject();
        } catch (ClassNotFoundException e) {
            logger.error("Error class ClientMetaData not found " + e.getMessage());
        }
        return clientMetaData;
    }

    public void closeChannelIfSocketClosed() throws ChannelClosedException {
        if (socket.isClosed()) {
            close();
            throw new ChannelClosedException("Socket " + socket.toString() + " is closed.");
        }
    }

    @Override
    public void close() {
        try {
            if (iBuffer != null)
                iBuffer.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        try {
            if (oBuffer != null)
                oBuffer.close();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("socket", socket)
                .add("oBuffer", oBuffer)
                .add("iBuffer", iBuffer)
                .toString();
    }
}
