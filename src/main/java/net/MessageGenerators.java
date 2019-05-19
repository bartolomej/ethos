package net;

import errors.NetworkException;
import org.json.JSONObject;

public class MessageGenerators {

    public static byte[] invalidRequest(String message) {
        NetworkException exception = new NetworkException(message);
        JSONObject parsedException = ParseUtil.parseException(exception);
        return new PeerMessage(MessageTypes.ERROR, parsedException).encode();
    }

    public static byte[] invalidRequest(String message, Exception e) {
        JSONObject parsedException = ParseUtil.parseException(e);
        return new PeerMessage(MessageTypes.ERROR, message, parsedException).encode();
    }

    public static byte[] pingRequest() {
        return new PeerMessage(MessageTypes.PING, null).encode();
    }

}
