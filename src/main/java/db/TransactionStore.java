package db;

import config.Constants;
import core.transaction.Transaction;
import core.transaction.TxRootIndex;
import org.json.JSONArray;
import org.json.JSONObject;
import util.ByteUtil;
import util.ObjectParser;

import java.util.ArrayList;

public class TransactionStore {

    public static void saveTxRootIndex(byte[] blockHash, JSONObject txRootIndex) {
        String filepath = Constants.INDEX_STORE_DIR + ByteUtil.toHexString(blockHash) + ".json";
        FileSystemStore.store(filepath, txRootIndex.toString());
    }

    public static TxRootIndex getTxRoot(byte[] blockHash) {
        String filepath = Constants.INDEX_STORE_DIR + ByteUtil.toHexString(blockHash) + ".json";
        JSONObject jsonTxRoot = new JSONObject(FileSystemStore.read(filepath));
        return ObjectParser.parseTxRootIndex(jsonTxRoot);
    }

    public static void save(byte[] hash, JSONObject transaction) {
        String filepath = Constants.TX_STORE_DIR + ByteUtil.toHexString(hash) + ".json";
        FileSystemStore.store(filepath, transaction.toString());
    }


    public static Transaction getTransaction(byte[] hash) {
        String filepath = Constants.TX_STORE_DIR + ByteUtil.toHexString(hash) + ".json";
        JSONObject jsonTx = new JSONObject(FileSystemStore.read(filepath));
        return ObjectParser.parseJsonTransaction(jsonTx);
    }

}
