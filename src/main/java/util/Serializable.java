package util;

import org.json.JSONObject;

public interface Serializable {
    JSONObject toJson();
    String toString();
    String toStringWithSuffix(String suffix);
    String toRawStringWithSuffix(String suffix);
}
