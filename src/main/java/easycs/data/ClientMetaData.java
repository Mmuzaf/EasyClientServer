package easycs.data;

import com.google.common.base.MoreObjects;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author Mmuzafarov
 */
public class ClientMetaData implements Serializable {

    private final String clientName;

    public ClientMetaData(String name) {
        clientName = name;
    }

    public String getClientName() {
        return clientName;
    }

    public static ClientMetaData getRandomClientMetaData() {
        return new ClientMetaData(UUID.randomUUID().toString());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("clientName", clientName)
                .toString();
    }
}
