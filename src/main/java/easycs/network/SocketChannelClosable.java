package easycs.network;

import easycs.data.ClientMetaData;
import easycs.data.Message;

import java.io.Closeable;
import java.io.IOException;

/**
 * Add comment here
 * <p/>
 * By @author Mmuzaf
 * Created at 10.09.2016
 */
public interface SocketChannelClosable extends Closeable {

    void writeObject(Message message) throws IOException;

    Message readObject() throws IOException;

    void sendUser(ClientMetaData clientMetaData) throws IOException;

    ClientMetaData getUser() throws IOException;
}
