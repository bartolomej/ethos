package db;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.json.JSONObject;

import config.*;

import java.io.FileWriter;
import java.io.IOException;

public class AccountStore {

    public static void saveAccount(JSONObject account) {
        try {
            String filename = "keystore" + System.currentTimeMillis() + ".json";
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(account.toString());
            String prettyJSON = gson.toJson(jsonElement);
            FileWriter fileWriter = new FileWriter(Constants.ROOT_DIR + filename);
            fileWriter.write(prettyJSON);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO: add getters etc.

}
