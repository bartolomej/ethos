package core.transaction;

import crypto.HashUtil;
import crypto.KeyUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import util.ByteUtil;

import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;

public class TxOutput {

    /* For a P2PKH-style output, Bob’s signature script will contain the following two pieces of data:
     * His full (un-hashed) public key, so the pubKey script can check that it hashes to the same value as the pubKey hash provided by Alice.
     * An secp256k1 signature made by using the ECDSA cryptographic formula to combine certain transaction data (described below) with Bob’s private key.
     * This lets the pubKey script verify that Bob owns the private key which created the public key.
     */

    private byte[] recipientAddress;
    private long value;
    private int outputIndex;

    public TxOutput(byte[] recipientAddress, long value, int outputIndex) {
        this.recipientAddress = recipientAddress;
        this.outputIndex = outputIndex;
        this.value = value;
    }

    public TxOutput(byte[] pubKeySig, byte[] recipientAddress, long value, int outputIndex) {
        this.recipientAddress = recipientAddress;
        this.outputIndex = outputIndex;
        this.value = value;
    }

    public byte[] getRecipientPubKey() {
        return this.recipientAddress;
    }

    public int getOutputIndex() {
        return this.outputIndex;
    }

    public long getValue() {
        return this.value;
    }

    public boolean valid() {
        try {
            KeyUtil.parsePublicKey(this.recipientAddress);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean equals(TxOutput output) {
        return Arrays.equals(this.recipientAddress, output.recipientAddress) & this.value == output.value;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("address", ByteUtil.encodeToBase64(this.recipientAddress));
        jsonObject.put("index", this.outputIndex);
        jsonObject.put("value", this.value);
        return jsonObject;
    }

    @Override
    public String toString() {
        return this.toStringWithSuffix(", ");
    }

    public String toStringWithSuffix(String suffix) {
        String encoded = "TxOutputData {";
        encoded += "address=" + ByteUtil.encodeToBase64(this.recipientAddress) + suffix;
        encoded += "index=" + this.outputIndex + suffix;
        encoded += "value=" + this.value;
        encoded += "}";
        return encoded;
    }

    public String toRawStringWithSuffix(String suffix) {
        return (ByteUtil.encodeToBase64(this.recipientAddress) + suffix + this.value);
    }

    public static String arrayToString(ArrayList<TxOutput> array) {
        return arrayToStringWithSuffix(array, ", ");
    }

    public static String arrayToStringWithSuffix(ArrayList<TxOutput> array, String suffix) {
        String out = "TxOutputArrayData [";
        for (TxOutput output : array) {
            out += output.toStringWithSuffix(", ") + suffix;
        }
        out += "]";
        return out;
    }

    public static int sum(ArrayList<TxOutput> inputs) {
        int sum = 0;
        for (TxOutput input : inputs) {
            sum += input.getValue();
        }
        return sum;
    }

    public static JSONArray arrayToJson(ArrayList<TxOutput> inputs) {
        JSONArray jsonArray = new JSONArray();
        for (TxOutput input : inputs) {
            jsonArray.put(input.toJson());
        }
        return jsonArray;
    }
}
