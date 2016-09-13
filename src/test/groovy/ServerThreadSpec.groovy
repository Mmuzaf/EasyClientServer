import easycs.ServerThread
import easycs.config.Constant
import easycs.data.ClientMetaData
import easycs.data.Message
import easycs.io.Command
import easycs.network.SocketChannelClosable
import org.apache.commons.logging.Log
import spock.lang.Specification
import spock.lang.Unroll

/**
 * @author Mmuzafarov
 */
class ServerThreadSpec extends Specification {

    def serverThread

    def "Execution server command /ONLINE successful, close channel when IOException thrown"() {
        setup:
            def channel = Mock(SocketChannelClosable) {
                readObject() >>>
                        [Message.getNewInstance(Constant.SERVER_NAME,
                                Character.toString(Constant.COMMAND_START_WITH_CHAR) + Command.ONLINE.toString()),
                         { throw new IOException() }]
            }
            serverThread = Spy(ServerThread, constructorArgs: [channel]) {
                clientAuthentication() >> true
            }
            def logMock = Mock(Log)
            serverThread.getLogger() >> logMock
        when:
            serverThread.run()
        then:
            1 * channel.writeObject({ Message m -> m.body == String.format(Constant.ONLINE_COMMAND_MSG, 1) } as Message)
            1 * logMock.error(_ as String)
            1 * channel.close()
    }

    @Unroll
    def "Execution client authentication method with (#expectedAuth)"() {
        setup:
            def channel = Mock(SocketChannelClosable) {
                getUser() >> clientMetaData
            }
            serverThread = new ServerThread(channel)
        expect:
            serverThread.clientAuthentication() == expectedAuth
        where:
            clientMetaData                           | expectedAuth
            new ClientMetaData(Constant.SERVER_NAME) | false
            ClientMetaData.getRandomClientMetaData() | true
    }
}
