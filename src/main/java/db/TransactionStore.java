package db;

import config.Constants;
import core.transaction.*;
import org.json.JSONArray;
import org.json.JSONObject;
import util.ByteUtil;
import util.ObjectParser;

import java.io.File;

public class TransactionStore {

    public static void saveTxRootIndex(byte[] blockHash, JSONObject txRootIndex) {
        String path = Constants.INDEX_STORE_DIR + ByteUtil.toHexString(blockHash) + ".json";
        FileSystemStore.store(path, txRootIndex.toString());
    }

    public static void saveTx(byte[] hash, JSONObject transaction) {
        String path = Constants.TX_STORE_DIR + ByteUtil.toHexString(hash) + ".json";
        FileSystemStore.store(path, transaction.toString());
    }

    public static void saveInputs(byte[] txHash, JSONArray txInputs) {
        String folderPath = Constants.INPUTS_STORE_DIR + ByteUtil.toHexString(txHash) + "/";
        FileSystemStore.makeDirectory(folderPath);
        for (int i = 0; i < txInputs.length(); i++) {
            String path = folderPath + i + ".json";
            FileSystemStore.store(path, txInputs.getJSONObject(i).toString());
        }
    }

    public static void saveOutputs(byte[] txHash, JSONArray txOutputs) {
        String folderPath = Constants.OUTPUTS_STORE_DIR + ByteUtil.toHexString(txHash) + "/";
        FileSystemStore.makeDirectory(folderPath);
        for (int i = 0; i < txOutputs.length(); i++) {
            String path = folderPath + i + ".json";
            FileSystemStore.store(path, txOutputs.getJSONObject(i).toString());
        }
    }

    public static TxInput getTxInput(byte[] txHash, int index) throws Exception {
        String path = Constants.INPUTS_STORE_DIR + ByteUtil.toHexString(txHash) + "/" + index + ".json";
        String fileInput = FileSystemStore.read(path);
        if (fileInput == null) throw new Exception("Tx input not found: " + path);
        JSONObject jsonTxInput = new JSONObject(fileInput);
        return ObjectParser.parseJsonInput(jsonTxInput);
    }

    public static TxOutput getTxOutput(byte[] txHash, int index) throws Exception {
        String path = Constants.OUTPUTS_STORE_DIR + ByteUtil.toHexString(txHash) + "/" + index + ".json";
        String fileInput = FileSystemStore.read(path);
        if (fileInput == null) throw new Exception("Tx output not found: " + path);
        JSONObject jsonTxOutput = new JSONObject(fileInput);
        return ObjectParser.parseJsonOutput(jsonTxOutput);
    }

    public static TxRootIndex getTxRoot(byte[] blockHash) throws Exception {
        String path = Constants.INDEX_STORE_DIR + ByteUtil.toHexString(blockHash) + ".json";
        String fileInput = FileSystemStore.read(path);
        if (fileInput == null) throw new Exception("Tx root not found: " + path);
        JSONObject jsonTxRoot = new JSONObject(fileInput);
        return ObjectParser.parseTxRootIndex(jsonTxRoot);
    }

    public static AbstractTransaction getTransaction(byte[] hash) throws Exception {
        String path = Constants.TX_STORE_DIR + ByteUtil.toHexString(hash) + ".json";
        String fileInput = FileSystemStore.read(path);
        if (fileInput == null) throw new Exception("Tx not found: " + path);
        JSONObject jsonTx = new JSONObject(fileInput);
        return ObjectParser.parseJsonTransaction(jsonTx);
    }

}
