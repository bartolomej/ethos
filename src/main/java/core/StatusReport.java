package core;

import config.Constants;
import config.SystemValues;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class StatusReport {

    private String module;
    private JSONObject stats;
    private long timestamp;
    private String node;

    public StatusReport(String module) {
        this.module = module;
        this.node = SystemValues.NODE_ADDRESS;
        this.timestamp = System.currentTimeMillis();
        stats = new JSONObject();
    }

    public void add(String key, String value) {
        this.stats.put(key, value);
    }

    public void add(String key, JSONObject value) {
        this.stats.put(key, value);
    }

    public String toString() {
        return toJson().toString();
    };

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("module", this.module);
        json.put("timestamp", this.timestamp);
        json.put("node", this.node);
        json.put("report", this.stats);
        return json;
    }

    public static JSONArray parseReportsArray(ArrayList<StatusReport> reports) {
        JSONArray jsonArray = new JSONArray();
        for (StatusReport report : reports) {
            jsonArray.put(report.toJson());
        }
        return jsonArray;
    }
}
