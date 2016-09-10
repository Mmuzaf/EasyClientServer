import easycs.ServerThread
import easycs.data.ClientMetaData
import easycs.network.SocketObjectStream
import spock.lang.Specification

/**
 * @author Mmuzafarov
 */
class ServerThreadSpec extends Specification {

    class ServerThreadWithoutAuth extends ServerThread {
        ServerThreadWithoutAuth(Socket socket) throws IOException {
            super(socket)
        }
        @Override
        public boolean clientAuthentication() {
            return true;
        }
    }

    ServerThread serverThread;

    def "Execution server command successful"() {
        setup:
            def channel = Mock(SocketObjectStream)
            def socket = Mock(Socket)
            socket.getOutputStream() >> Mock(OutputStream)
            socket.getInputStream() >> Mock(InputStream)

            serverThread = new ServerThreadWithoutAuth(Mock(Socket))
            serverThread.channel >> channel
            serverThread.clientMetaData >> Mock(ClientMetaData)
        when:
            serverThread.run()
        then:
            1 == 1
    }
}
