package core.transaction;

import crypto.HashUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import util.ByteUtil;

import java.util.ArrayList;
import java.util.Arrays;

public class TxOutput {

    private byte[] recipientPubKey;
    private long value;
    private int outputIndex;

    public TxOutput(byte[] recipientPubKey, long value, int outputIndex) {
        this.recipientPubKey = recipientPubKey;
        this.outputIndex = outputIndex;
        this.value = value;
    }

    public byte[] getHashValue() {
        String pubKey = ByteUtil.toHexString(this.getRecipientPubKey());
        String valueSum = (
                pubKey +
                this.getOutputIndex() +
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
        return this.recipientPubKey != null;
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
