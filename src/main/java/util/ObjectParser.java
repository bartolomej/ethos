package util;

import core.block.Block;
import core.transaction.Transaction;
import core.transaction.TxInput;
import core.transaction.TxOutput;
import core.transaction.TxRootIndex;
import org.json.JSONArray;
import org.json.JSONObject;

import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;

public class ObjectParser {

    public static TxInput parseJsonInput(JSONObject input) {
        byte[] sig = ByteUtil.toByteArray(input.getString("sig"));
        byte[] txHash = ByteUtil.toByteArray(input.getString("tx_hash"));
        byte[] prevTxHash = ByteUtil.toByteArray(input.getString("prev_tx_hash"));
        long value = input.getLong("value");
        int outputIndex = input.getInt("output_index");
        return new TxInput(sig, txHash, prevTxHash, outputIndex, value);
    }

    public static TxOutput parseJsonOutput(JSONObject output) {
        byte[] recipientPubKey = ByteUtil.toByteArray(output.getString("recipient_pub_key"));
        int outputIndex = output.getInt("output_index");
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
        byte[] blockHash = ByteUtil.toByteArray(index.getString("block_hash"));
        JSONArray jsonArray = index.getJSONArray("transactions");
        byte[][] transactions = new byte[jsonArray.length()][];
        for (int i = 0; i < jsonArray.length(); i++) {
            transactions[i] = ByteUtil.toByteArray(jsonArray.getString(i));
        }
        return new TxRootIndex(blockHash, transactions);
    }

    public static Transaction parseJsonTransaction(JSONObject transaction) {
        ArrayList<TxInput> inputs = parseJsonInputArray(transaction.getJSONArray("inputs"));
        ArrayList<TxOutput> outputs = parseJsonOutputArray(transaction.getJSONArray("outputs"));
        byte[] signature = ByteUtil.toByteArray(transaction.getString("signature"));
        byte[] hash = ByteUtil.toByteArray(transaction.getString("hash"));
        byte[] pubKey = ByteUtil.toByteArray(transaction.getString("pub_key"));
        long timestamp = transaction.getLong("timestamp");
        Transaction tx = null;
        try {
            tx = new Transaction(inputs, outputs, pubKey, signature, hash, timestamp);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return tx;
    }

    public static Block parseJsonBlock(JSONObject block) {
        byte[] hash = ByteUtil.toByteArray(block.getString("hash"));
        byte[] miner = ByteUtil.toByteArray(block.getString("miner"));
        byte[] txRoot = ByteUtil.toByteArray(block.getString("tx_root"));
        byte[] prevBlockHash = ByteUtil.toByteArray(block.getString("prev_block_hash"));
        int difficulty = block.getInt("difficulty");
        long timestamp = block.getLong("timestamp");
        int index = block.getInt("index");
        return new Block(hash, prevBlockHash, txRoot, miner, timestamp, difficulty, index);
    }
}
