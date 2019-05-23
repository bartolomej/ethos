package util;

import core.block.Block;
import core.transaction.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

public class ObjectParser {

    public static TxInput parseJsonInput(JSONObject input) {
        byte[] sig = ByteUtil.decodeFromBase64(input.getString("sig"));
        byte[] prevTxHash = ByteUtil.decodeFromBase64(input.getString("prev_tx"));
        long value = input.getLong("value");
        int outputIndex = input.getInt("output_index");
        return new TxInput(sig, prevTxHash, outputIndex, value);
    }

    public static TxOutput parseJsonOutput(JSONObject output) {
        byte[] recipientPubKey = ByteUtil.decodeFromBase64(output.getString("address"));
        int outputIndex = output.getInt("index");
        long value = output.getLong("value");
        return new TxOutput(recipientPubKey, value, outputIndex);
    }

    public static ArrayList<TxInput> parseJsonInputArray(JSONArray inputArray) {
        ArrayList<TxInput> inputs = new ArrayList<>();
        for (int i = 0; i < inputArray.length(); i++) {
            inputs.add(parseJsonInput(inputArray.getJSONObject(i)));
        }
        return inputs;
    }

    public static ArrayList<TxOutput> parseJsonOutputArray(JSONArray outputArray) {
        ArrayList<TxOutput> outputs = new ArrayList<>();
        for (int i = 0; i < outputArray.length(); i++) {
            outputs.add(parseJsonOutput(outputArray.getJSONObject(i)));
        }
        return outputs;
    }

    public static TxRootIndex parseTxRootIndex(JSONObject index) {
        byte[] jsonBlockHash = ByteUtil.decodeFromBase64(index.getString("block_hash"));
        JSONArray jsonArray = index.getJSONArray("transactions");
        byte[][] transactions = new byte[jsonArray.length()][];
        for (int i = 0; i < jsonArray.length(); i++) {
            transactions[i] = ByteUtil.decodeFromBase64(jsonArray.getString(i));
        }
        return new TxRootIndex(jsonBlockHash, transactions);
    }

    public static AbstractTransaction parseJsonTransaction(JSONObject transaction) {
        if (!transaction.has("inputs")) return parseJsonCoinbaseTransaction(transaction); // replace with isCoinbase property ?
        ArrayList<TxInput> inputs = parseJsonInputArray(transaction.getJSONArray("inputs"));
        ArrayList<TxOutput> outputs = parseJsonOutputArray(transaction.getJSONArray("outputs"));
        byte[] signature = ByteUtil.decodeFromBase64(transaction.getString("signature"));
        byte[] hash = ByteUtil.decodeFromBase64(transaction.getString("hash"));
        byte[] pubKey = ByteUtil.decodeFromBase64(transaction.getString("address"));
        long timestamp = transaction.getLong("timestamp");
        Transaction tx = null;
        try {
            tx = new Transaction(inputs, outputs, pubKey, signature, hash, timestamp);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return tx;
    }

    public static CoinbaseTransaction parseJsonCoinbaseTransaction(JSONObject transaction) {
        ArrayList<TxOutput> outputs = parseJsonOutputArray(transaction.getJSONArray("outputs"));
        byte[] hash = ByteUtil.decodeFromBase64(transaction.getString("hash"));
        byte[] pubKey = ByteUtil.decodeFromBase64(transaction.getString("address"));
        long timestamp = transaction.getLong("timestamp");
        CoinbaseTransaction tx = null;
        try {
            tx = new CoinbaseTransaction(pubKey, hash,null, timestamp, outputs);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return tx;
    }

    public static Block parseJsonBlock(JSONObject jsonBlock) {
        byte[] hash = ByteUtil.decodeFromBase64(jsonBlock.getString("hash"));
        byte[] miner = ByteUtil.decodeFromBase64(jsonBlock.getString("miner"));
        byte[] txRoot = ByteUtil.decodeFromBase64(jsonBlock.getString("tx_root"));
        byte[] prevBlockHash = ByteUtil.decodeFromBase64(jsonBlock.getString("prev_block"));
        int difficulty = jsonBlock.getInt("difficulty");
        long timestamp = jsonBlock.getLong("timestamp");
        int index = jsonBlock.getInt("index");
        return new Block(hash, prevBlockHash, txRoot, miner, timestamp, difficulty, index);
    }
}
