package db;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import config.Global;
import core.transaction.Transaction;
import org.json.JSONObject;
import util.ByteUtil;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TransactionStore {

    public static void save(byte[] hash, JSONObject transaction) {
        try {
            String filename = Global.ROOT_DIR + ByteUtil.toHexString(hash) + ".json";
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonParser jsonParser = new JsonParser();
            JsonElement jsonElement = jsonParser.parse(transaction.toString());
            String prettyJSON = gson.toJson(jsonElement);
            FileWriter fileWriter = new FileWriter(filename);
            fileWriter.write(prettyJSON);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // TODO: separate storing transaction and inputs/outputs

    public static Transaction getByHash(byte[] hash) {
        String filename = Global.ROOT_DIR + ByteUtil.toHexString(hash) + ".json";
        JSONObject parser = new JSONObject(TransactionStore.readFile(filename));
        /*transaction = new Transaction(
                    parser.get("inputs"),
                    parser.get("outputs"),
                    parser.get("publicKey"),
                    parser.getString("signature"),
                    parser.getString("hash"),
                    parser.getLong("timestamp"));*/
        return null;
    }

    private static String readFile(String filename) {
        String input = "";
        try {
            FileReader fileReader = new FileReader(filename);
            while (fileReader.ready()) {
                input += fileReader.read();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return input;
    }
}
