package core;

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
        byte[] toAddress = new byte[]{0,0,0,0,0,0};
        long value = 1000;
        int index = 0;

        TxInput input = new TxInput(toAddress, value, index);

        JSONObject expected = new JSONObject( String.format("{txid: %s, value: %s, output_index: %s}",
                ByteUtil.toHexString(toAddress), value, index
        ));

        assertEquals(expected.toString(), input.toJson().toString());
    };

    @Test
    public void serializeTxOutput() {
        byte[] toAddress = new byte[]{0,0,0,0,0,0};
        long value = 1000;

        TxOutput input = new TxOutput(toAddress, value);

        JSONObject expected = new JSONObject( String.format("{txid: %s, value: %s}",
                ByteUtil.toHexString(toAddress), value
        ));

        assertEquals(expected.toString(), input.toJson().toString());
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
        Transaction tx = HelperGenerator.generateTestValidTransaction();

        JSONObject expected = new JSONObject();
        expected.put("outputs", TxOutput.arrayToJson(tx.getOutputs()));
        expected.put("inputs", TxInput.arrayToJson(tx.getInputs()));
        expected.put("signature", ByteUtil.toHexString(tx.getSignature()));
        expected.put("hash", ByteUtil.toHexString(tx.getHash()));
        expected.put("timestamp", tx.getTimestamp());
        expected.put("pub_key", ByteUtil.toHexString(tx.getPublicKey().getEncoded()));

        assertEquals(tx.toJson().toString(), expected.toString());
    };

    @Test
    public void serializeBlock() {};

    @Test
    public void serializeAccount() {};

    private JSONArray generateTestInputsJsonArray(int length) {
        JSONArray jsonArray = new JSONArray();

        byte[] toAddress = new byte[]{0,0,0,0,0,0};
        long value = 1000;
        int index = 0;

        for (int i = 0; i < length; i++) {
            jsonArray.put(new TxInput(toAddress, value, index).toJson());
        }
        return jsonArray;
    }

    private ArrayList<TxInput> generateTestInputsArray(int length) {
        ArrayList<TxInput> arrayList = new ArrayList<>();

        byte[] toAddress = new byte[]{0,0,0,0,0,0};
        long value = 1000;
        int index = 0;

        for (int i = 0; i < length; i++) {
            arrayList.add(new TxInput(toAddress, value, index));
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
