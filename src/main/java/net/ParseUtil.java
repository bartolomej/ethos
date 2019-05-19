package net;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class ParseUtil {

    public static JSONObject parseBody(InputStream stream) {
        String content = "";
        try {
            while (stream.available() > 0) {
                content += (char)stream.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String body = content
                .replace("\n", "")
                .replace("\t", "");
        return new JSONObject(body);
    }

    public static JSONObject parseException(Exception e) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", e.getClass());
        jsonObject.put("message", e.getMessage());
        jsonObject.put("cause", e.getCause());
        jsonObject.put("stack_trace", e.getStackTrace());
        return jsonObject;
    }

}
