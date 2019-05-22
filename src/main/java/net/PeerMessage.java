package net;

import config.SystemValues;
import core.block.Block;
import core.transaction.Transaction;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

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

    public PeerMessage(MessageTypes type, String address, JSONObject body, long timestamp, String version, String message) {
        this.timestamp = timestamp;
        this.version = version;
        this.messageBody = body;
        this.address = address;
        this.message = message;
        this.type = type;
    }

    public static PeerMessage pong() {
        return new PeerMessage(MessageTypes.PONG, null);
    }

    public static PeerMessage ping() {
        return new PeerMessage(MessageTypes.PING, null);
    }

    public static PeerMessage peers(ArrayList<PeerNode> peers) {
        JSONArray jsonArray = PeerNode.toJsonArray(peers);
        JSONObject msgBody = new JSONObject();
        msgBody.put("peers", jsonArray);
        return new PeerMessage(MessageTypes.PEERS, msgBody);
    }

    public static PeerMessage getPeers() {
        return new PeerMessage(MessageTypes.GET_PEERS, null);
    }

    public static PeerMessage block(Block block) {
        return new PeerMessage(MessageTypes.BLOCK, block.toJsonFull());
    }

    public static PeerMessage transaction(Transaction tx) {
        return new PeerMessage(MessageTypes.TRANSACTION, tx.toJson());
    }

    public static PeerMessage sync() {
        // TODO: to be defined
        return null;
    }

    public static PeerMessage error(Exception e) {
        JSONObject eBody = new JSONObject();
        JSONArray eArray = new JSONArray();
        eArray.put(ParseUtil.parseException(e));
        eBody.put("error", eArray);
        return new PeerMessage(MessageTypes.ERROR, eBody);
    }

    public static PeerMessage errors(ArrayList<Exception> exceptions) {
        JSONObject eBody = new JSONObject();
        JSONArray eArray = new JSONArray();
        for (Exception e : exceptions) {
            eArray.put(ParseUtil.parseException(e));
        }
        eBody.put("error", eArray);
        return new PeerMessage(MessageTypes.ERROR, eBody);
    }

    public static PeerMessage ok() {
        return new PeerMessage(MessageTypes.OK, null);
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

    public static PeerMessage decode(JSONObject jsonObject) throws Exception {
        return new PeerMessage(
                parseMsgType(jsonObject.getString("type")),
                jsonObject.getString("address"),
                jsonObject.getJSONObject("body"),
                jsonObject.getLong("timestamp"),
                jsonObject.getString("version"),
                jsonObject.getString("message")
        );
    }

    public static MessageTypes parseMsgType(String type) throws Exception {
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
            default: throw new Exception("Unknown message type");
        }
    }

    public byte[] encode() {
        return this.getJsonEncoded().toString().getBytes();
    }

    public JSONObject getMessageBody() {
        return this.messageBody;
    }
}