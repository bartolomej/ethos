package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import core.Account;
import org.json.JSONObject;

import java.io.*;

import java.io.PrintWriter;

public class KeyStoreUtil {

    public static void persistAccount(JSONObject account) {
        try {
            String dir = System.getProperty("user.dir") + "/";
            String filename = "keystore" + System.currentTimeMillis() + ".json";
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(account.toString());
            String prettyJSON = gson.toJson(jsonElement);
            FileWriter fileWriter = new FileWriter(dir + filename);
            fileWriter.write(prettyJSON);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
