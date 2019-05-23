package core;

import core.block.Block;
import org.json.*;
import org.junit.Test;
import core.transaction.*;
import util.ByteUtil;

import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ObjectsJsonSerialization {

    @Test
    public void serializeTxInput() {
        byte[] recipientAddress = new byte[]{0,0,0,0,0,0};
        byte[] sig = new byte[]{2,2,2,2,2,2};
        byte[] txHash = new byte[]{1,1,1,1,1,1};
        long value = 1000;
        int index = 0;

        TxOutput output = new TxOutput(recipientAddress, value, index);
        TxInput input = new TxInput(sig, txHash, output);

        JSONObject expected = new JSONObject();
        expected.put("sig", ByteUtil.encodeToBase64(sig));
        expected.put("output_index", index);
        expected.put("value", value);
        expected.put("prev_tx", ByteUtil.encodeToBase64(txHash));

        assertEquals(expected.toString(), input.toJson().toString());
    };

    @Test
    public void serializeTxOutput() {
        byte[] recipientAddress = new byte[]{0,0,0,0,0,0};
        byte[] txHash = new byte[]{1,1,1,1,1,1};
        long value = 1000;
        int index = 0;

        TxOutput output = new TxOutput(recipientAddress, value, index);

        JSONObject expected = new JSONObject();
        expected.put("address", ByteUtil.encodeToBase64(recipientAddress));
        expected.put("index", index);
        expected.put("value", value);

        assertEquals(expected.toString(), output.toJson().toString());
    };

    @Test
    public void serializeTxInputArray() {
        JSONArray expectedJSONArray = generateTestInputsJsonArray(2);
        ArrayList<TxInput> actualArray = generateTestInputsArray(2);

        assertEquals(expectedJSONArray.toString(), TxInput.arrayToJson(actualArray).toString());
    };

    @Test
    public void serializeTxOutputArray() {
        JSONArray expectedJSONArray = generateTestOutputsJsonArray(2);
        ArrayList<TxOutput> actualArray = generateTestOutputsArray(2);

        assertEquals(expectedJSONArray.toString(), TxOutput.arrayToJson(actualArray).toString());
    };

    @Test
    public void serializeTransaction() throws InvalidKeySpecException, InvalidKeyException {
        Transaction tx = new HelperGenerator().getTransaction();

        JSONObject expected = new JSONObject();
        expected.put("timestamp", tx.getTimestamp());
        expected.put("hash", ByteUtil.encodeToBase64(tx.getHash()));
        expected.put("address", ByteUtil.encodeToBase64(tx.getPublicKey().getEncoded()));
        expected.put("signature", ByteUtil.encodeToBase64(tx.getSignature()));
        expected.put("inputs", TxInput.arrayToJson(tx.getInputs()));
        expected.put("outputs", TxOutput.arrayToJson(tx.getOutputs()));

        assertEquals(expected.toString(), tx.toJson().toString());
    };

    @Test
    public void serializeBlock() {
        byte[] hash = new byte[]{1,1,1,1,1,1};
        byte[] prevBlockHash = new byte[]{2,2,2,2,2,2};
        byte[] txRoot = new byte[]{3,3,3,3,3,3};
        byte[] miner = new byte[]{4,4,4,4,4,4};
        long timestamp = 1000000;
        int difficulty = 4;
        int index = 5;

        Block block = new Block(hash, prevBlockHash, txRoot, miner, timestamp, difficulty, index);

        JSONObject jsonBlock = new JSONObject();
        jsonBlock.put("difficulty", difficulty);
        jsonBlock.put("prev_block", ByteUtil.encodeToBase64(prevBlockHash));
        jsonBlock.put("tx_root", ByteUtil.encodeToBase64(txRoot));
        jsonBlock.put("index", index);
        jsonBlock.put("hash", ByteUtil.encodeToBase64(hash));
        jsonBlock.put("miner", ByteUtil.encodeToBase64(miner));
        jsonBlock.put("timestamp", timestamp);

        assertEquals(jsonBlock.toString(), block.toJson().toString());
    };

    @Test
    public void serializeAccount() {};

    private JSONArray generateTestInputsJsonArray(int length) {
        JSONArray jsonArray = new JSONArray();

        byte[] txHash = new byte[]{0,0,0,0,0,0};
        byte[] sig = new byte[]{1,1,1,1,1};
        byte[] prevOutHash = new byte[]{2,2,2,2,2};
        long value = 1000;
        int index = 0;

        for (int i = 0; i < length; i++) {
            jsonArray.put(new TxInput(sig, txHash, prevOutHash, index, value).toJson());
        }
        return jsonArray;
    }

    private ArrayList<TxInput> generateTestInputsArray(int length) {
        ArrayList<TxInput> arrayList = new ArrayList<>();

        byte[] txHash = new byte[]{0,0,0,0,0,0};
        byte[] sig = new byte[]{1,1,1,1,1};
        byte[] prevOutHash = new byte[]{2,2,2,2,2};
        long value = 1000;
        int index = 0;

        for (int i = 0; i < length; i++) {
            arrayList.add(new TxInput(sig, txHash, prevOutHash, index, value));
        }
        return arrayList;
    }

    private JSONArray generateTestOutputsJsonArray(int length) {
        JSONArray jsonArray = new JSONArray();

        byte[] toAddress = new byte[]{0,0,0,0,0,0};
        long value = 1000;
        int index = 0;

        for (int i = 0; i < length; i++) {
            jsonArray.put(new TxOutput(toAddress, value, index).toJson());
        }
        return jsonArray;
    }

    private ArrayList<TxOutput> generateTestOutputsArray(int length) {
        ArrayList<TxOutput> arrayList = new ArrayList<>();

        byte[] toAddress = new byte[]{0,0,0,0,0,0};
        long value = 1000;
        int index = 0;

        for (int i = 0; i < length; i++) {
            arrayList.add(new TxOutput(toAddress, value, index));
        }
        return arrayList;
    }
}
