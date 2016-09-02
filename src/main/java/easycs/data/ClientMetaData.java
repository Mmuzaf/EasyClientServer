package easycs.data;

import com.google.common.base.MoreObjects;

import java.io.Serializable;

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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("clientName", clientName)
                .toString();
    }
}
