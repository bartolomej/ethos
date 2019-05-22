package net;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class PeerNode {
    // node desc implementation
    public String address;

    public PeerNode(String address) {
        this.address = address;
    }

    public JSONObject toJson() {
        String json = String.format("{address: %s}", this.address);
        return new JSONObject(json);
    }

    public static JSONArray toJsonArray(ArrayList<PeerNode> peers) {
        JSONArray jsonArray = new JSONArray();
        for (PeerNode peer : peers) {
            jsonArray.put(peer.toJson());
        }
        return jsonArray;
    }
}
