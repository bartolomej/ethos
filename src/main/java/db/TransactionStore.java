package db;

import config.Constants;
import core.transaction.Transaction;
import org.json.JSONObject;
import util.ByteUtil;
import util.ObjectParser;

public class TransactionStore {

    public static void save(byte[] hash, JSONObject transaction) {
        String filepath = Constants.ROOT_DIR + ByteUtil.toHexString(hash) + ".json";
        FileSystemStore.store(filepath, transaction.toString());
    }


    public static Transaction getByHash(byte[] hash) {
        String filepath = Constants.ROOT_DIR + ByteUtil.toHexString(hash) + ".json";
        JSONObject jsonTx = new JSONObject(FileSystemStore.read(filepath));
        return ObjectParser.parseJsonTransaction(jsonTx);
    }

}
