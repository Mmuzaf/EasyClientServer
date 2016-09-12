import easycs.ServerThread
import easycs.config.Constant
import easycs.data.Message
import easycs.io.Command
import easycs.network.SocketChannelClosable
import spock.lang.Specification

/**
 * @author Mmuzafarov
 */
class ServerThreadSpec extends Specification {

    def serverThread

    class ServerThreadWithoutAuth extends ServerThread {
        ServerThreadWithoutAuth(SocketChannelClosable channel) throws IOException {
            super(channel)
        }

        protected void clientAuthentication() {};

        public void setAuthSuccess() { authSuccess = true; }
    }

    def "Execution server command /ONLINE successful, close channel when IOException thrown"() {
        setup:
            def channel = Mock(SocketChannelClosable) {
                readObject() >>>
                        [Message.getNewInstance(Constant.SERVER_NAME,
                                Character.toString(Constant.COMMAND_START_WITH_CHAR) + Command.ONLINE.toString()),
                         { throw new IOException() }]
            }
            serverThread = new ServerThreadWithoutAuth(channel)
            serverThread.setAuthSuccess()
        when:
            serverThread.run()
        then:
            1 * channel.writeObject({ Message m -> m.body == String.format(Constant.ONLINE_COMMAND_MSG, 1) } as Message)
            1 * channel.close()
    }
}
