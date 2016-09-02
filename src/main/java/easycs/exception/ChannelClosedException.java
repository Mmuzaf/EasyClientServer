package easycs.exception;

import java.io.IOException;

/**
 * @author Mmuzafarov
 */
public class ChannelClosedException extends IOException {
    public ChannelClosedException() {
    }

    public ChannelClosedException(String message) {
        super(message);
    }

    public ChannelClosedException(String message, Throwable cause) {
        super(message, cause);
    }
}
