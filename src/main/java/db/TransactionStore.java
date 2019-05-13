package db;

import config.Global;
import core.transaction.Transaction;
import org.json.JSONObject;
import util.ByteUtil;
import util.ObjectParser;

public class TransactionStore {

    public static void save(byte[] hash, JSONObject transaction) {
        String filepath = Global.ROOT_DIR + ByteUtil.toHexString(hash) + ".json";
        FileStore.store(filepath, transaction.toString());
    }


    public static Transaction getByHash(byte[] hash) {
        String filepath = Global.ROOT_DIR + ByteUtil.toHexString(hash) + ".json";
        JSONObject jsonTx = new JSONObject(FileStore.read(filepath));
        jsonTx.getJSONArray("inputs");
        jsonTx.getJSONArray("outputs");
        jsonTx.get("pub_key");
        jsonTx.getString("signature");
        jsonTx.getString("hash");
        jsonTx.getLong("timestamp");
        return ObjectParser.parseJsonTransaction(jsonTx);
    }

}
