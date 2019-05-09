package util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ArrayUtil {

    public static String toString(ArrayList<Serializable> array) {
        return toStringWithSuffix(array, "");
    }

    public static String toStringWithSuffix(ArrayList<Serializable> array, String suffix) {
        String output = "";
        for (Serializable o1 : array) {
            output += o1.toString();
            output += suffix;
        }
        return output;
    }

    public static JSONArray toJson(ArrayList<Serializable> array) {
        String json = "[";
        for (Serializable o1 : array) {
            json += o1.toJson();
            json += ", ";
        }
        json += "]";
        return new JSONArray(json);
    }
}
