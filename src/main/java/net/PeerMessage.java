package net;

import org.json.JSONObject;

public class PeerMessage {

    private MessageTypes type;
    private String toAddress;
    private long timestamp;
    private String version; // header
    private JSONObject messageBody;

    public PeerMessage(MessageTypes type, String toAddress, JSONObject body) {
        this.messageBody = body;
        this.toAddress = toAddress;
        this.type = type;
    }

    public String getToAddress() {
        return this.toAddress;
    }

    public JSONObject encode() {
        return null; // serialize object
    }
}