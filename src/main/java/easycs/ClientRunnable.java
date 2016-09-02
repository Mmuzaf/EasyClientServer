package easycs;

import easycs.config.Constant;
import easycs.data.Message;
import easycs.network.IOSocketChannel;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;

/**
 * @author Mmuzafarov
 */
public class ClientRunnable implements Runnable {
    private final static Log logger = LogFactory.getLog(ClientRunnable.class);
    private final IOSocketChannel channel;

    public ClientRunnable(IOSocketChannel socket) {
        channel = socket;
    }

    @Override
    public void run() {
        boolean waiting = true;
        Message message;
        while (waiting) {
            try {
                message = channel.readObject();
                if (Constant.SERVER_NAME.equals(message.getSender())
                        && Constant.BYE_MESSAGE.equals(message.getBody())) {
                    waiting = false;
                }
                System.out.println(message.toString());
            } catch (IOException e) {
                logger.error("Could not establish connection. easycs.Server might have been disconnected.");
                waiting = false;
            }
        }
    }
}
