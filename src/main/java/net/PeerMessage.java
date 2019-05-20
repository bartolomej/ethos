package net;

import config.SystemValues;
import errors.NetworkException;
import org.json.JSONObject;

public class PeerMessage {

    private MessageTypes type;
    private String address;
    private long timestamp;
    private String version;
    private String message;
    private JSONObject messageBody;

    public PeerMessage(MessageTypes type, JSONObject body) {
        this.timestamp = System.currentTimeMillis();
        this.version = SystemValues.PROTOCOL_VERSION;
        this.address = SystemValues.NODE_ADDRESS;
        this.messageBody = body;
        this.type = type;
    }

    public PeerMessage(MessageTypes type, String message, JSONObject body) {
        this.timestamp = System.currentTimeMillis();
        this.version = SystemValues.PROTOCOL_VERSION;
        this.address = SystemValues.NODE_ADDRESS;
        this.messageBody = body;
        this.message = message;
        this.type = type;
    }

    public PeerMessage(MessageTypes type, JSONObject body, long timestamp, String version) {
        this.timestamp = timestamp;
        this.version = version;
        this.messageBody = body;
        this.address = SystemValues.NODE_ADDRESS;
        this.type = type;
    }

    public PeerMessage(MessageTypes type, String address, JSONObject body, long timestamp, String version, String message) {
        this.timestamp = timestamp;
        this.version = version;
        this.messageBody = body;
        this.address = address;
        this.message = message;
        this.type = type;
    }

    public MessageTypes getType() {
        return this.type;
    }

    public String getProtocolVersion() {
        return this.version;
    }

    public JSONObject getBody() {
        return this.messageBody;
    }

    public String getAddress() {
        return this.address;
    }

    private JSONObject getJsonEncoded() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", type);
        jsonObject.put("address", address);
        jsonObject.put("message", message);
        jsonObject.put("timestamp", timestamp);
        jsonObject.put("version", version);
        jsonObject.put("body", messageBody);
        return jsonObject;
    }

    public static PeerMessage decode(JSONObject jsonObject) throws NetworkException {
        return new PeerMessage(
                parseMsgType(jsonObject.getString("type")),
                jsonObject.getString("address"),
                jsonObject.getJSONObject("body"),
                jsonObject.getLong("timestamp"),
                jsonObject.getString("version"),
                jsonObject.getString("message")
        );
    }

    public static MessageTypes parseMsgType(String type) throws NetworkException {
        switch (type) {
            case "PING": return MessageTypes.PING;
            case "PONG": return MessageTypes.PONG;
            case "PEERS": return MessageTypes.PEERS;
            case "GET_PEERS": return MessageTypes.GET_PEERS;
            case "ERROR": return MessageTypes.ERROR;
            case "BLOCK": return MessageTypes.BLOCK;
            case "TRANSACTION": return MessageTypes.TRANSACTION;
            case "SYNC": return MessageTypes.SYNC;
            case "INFO": return MessageTypes.INFO;
            default: throw new NetworkException("Unknown message type");
        }
    }

    public byte[] encode() {
        return this.getJsonEncoded().toString().getBytes();
    }

    public JSONObject getMessageBody() {
        return this.messageBody;
    }
}