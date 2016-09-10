package easycs.network;

import easycs.config.Constant;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Unify creation client/server socket with io buffers
 * By @author Mmuzaf
 * Created at 10.09.2016
 */
public class SocketChannelFactory {

    private SocketChannelFactory() {
    }

    public static SocketChannelClosable getClientSocketChannelInstance(String host, Integer port) throws IOException {
        Socket socket =
                new Socket(
                        host == null ? Constant.DEFAULT_HOST : host,
                        port == null ? Constant.DEFAULT_PORT : port
                );
//        socket.connect(
//                new InetSocketAddress(
//                        host == null ? Constant.DEFAULT_HOST : host,
//                        port == null ? Constant.DEFAULT_PORT : port
//                )
//        );
        return getSocketChannelBySocket(socket);
    }

    public static SocketChannelClosable getServerSocketChannelInstance(ServerSocket serverSocket) throws IOException {
        return getSocketChannelBySocket(serverSocket.accept());
    }

    public static SocketChannelClosable getSocketChannelBySocket(Socket socket) throws IOException {
        ObjectOutputStream oBuffer = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream iBuffer = new ObjectInputStream(socket.getInputStream());
        return new SocketChannel(socket, oBuffer, iBuffer);
    }

}
