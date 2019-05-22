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

    private byte[] recipientPubKey;
    private long value;
    private int outputIndex;

    public TxOutput(byte[] recipientPubKey, long value, int outputIndex) {
        this.recipientPubKey = recipientPubKey;
        this.outputIndex = outputIndex;
        this.value = value;
    }

    /** @deprecated **/
    public byte[] getHashValue() {
        String valueSum = (
                this.getOutputIndex() + "" +
                this.getValue()
        );
        return HashUtil.sha256(valueSum.getBytes());
    }

    public byte[] getRecipientPubKey() {
        return this.recipientPubKey;
    }

    public int getOutputIndex() {
        return this.outputIndex;
    }

    public long getValue() {
        return this.value;
    }

    public boolean valid() {
        try {
            KeyUtil.parsePublicKey(this.recipientPubKey);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean equals(TxOutput output) {
        return Arrays.equals(this.recipientPubKey, output.recipientPubKey) & this.value == output.value;
    }

    public static long sum(ArrayList<TxOutput> inputs) {
        int sum = 0;
        for (TxOutput input : inputs) {
            sum += input.value;
        }
        return sum;
    }

    public static JSONArray arrayToJson(ArrayList<TxOutput> outputs) {
        JSONArray jsonArray = new JSONArray();
        for (TxOutput output : outputs) {
            jsonArray.put(output.toJson());
        }
        return jsonArray;
    }

    public JSONObject toJson() {
        String json = String.format("{recipient_pub_key: %s, output_index: %s, value: %s}",
                ByteUtil.toHexString(this.recipientPubKey), this.outputIndex, this.value
        );
        return new JSONObject(json);
    }

    @Override
    public String toString() {
        return this.toStringWithSuffix(", ");
    }

    public String toStringWithSuffix(String suffix) {
        String encoded = "TxOutputData {";
        encoded += "recipient_pub_key=" + ByteUtil.toHexString(this.recipientPubKey) + suffix;
        encoded += "output_index=" + this.outputIndex + suffix;
        encoded += "value=" + this.value;
        encoded += "}";
        return encoded;
    }

    public String toRawStringWithSuffix(String suffix) {
        return (ByteUtil.toHexString(this.recipientPubKey) + suffix + this.value);
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
}
