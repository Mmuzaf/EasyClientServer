package easycs.data;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Mmuzafarov
 */
public class Message implements Serializable {

    private final String time;
    private final String user;
    private final String body;

    public Message(String user, String body) {
        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        this.time = df.format(new Date());
        this.user = user;
        this.body = body;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("time", time)
                .add("user", user)
                .add("body", body)
                .toString();
    }

    public String getBody() {
        return body;
    }

    public String getSender() {
        return user;
    }

    public static Message getNewInstance(String user, String body) {
        return new Message(user, body);
    }
}
