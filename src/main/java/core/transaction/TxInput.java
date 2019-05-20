package core.transaction;

import crypto.HashUtil;
import crypto.KeyUtil;
import crypto.SigUtil;
import org.json.JSONArray;
import org.json.JSONObject;
import util.ByteUtil;

import java.security.InvalidKeyException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;

public class TxInput {

    private byte[] txHash; // pointing to transaction containing outputs -> used for retrieving transaction
    private byte[] prevTxHash;
    private int prevOutputIndex; // which output of that transaction is referenced
    /* SIGNATURE contains:
     * -> previous transaction hash
     * -> output index of previous transaction
     */
    private byte[] signature;
    public long value;
    private TxOutput prevOutput;

    public TxInput(byte[] signature, byte[] txHash, TxOutput prevOutput) {
        this.prevTxHash = prevOutput.getHashValue();
        this.txHash = txHash;
        this.signature = signature;
        this.prevOutputIndex = prevOutput.getOutputIndex();
        this.value = prevOutput.getValue();
        this.prevOutput = prevOutput;
    }

    public TxInput(byte[] signature, byte[] txHash, byte[] prevTxHash, TxOutput prevOutput) {
        this.prevTxHash = prevTxHash;
        this.txHash = txHash;
        this.signature = signature;
        this.prevOutputIndex = prevOutput.getOutputIndex();
        this.value = prevOutput.getValue();
        this.prevOutput = prevOutput;
    }

    public TxInput(byte[] signature, byte[] txHash, byte[] prevTxHash, int prevOutputIndex, long value) {
        this.prevTxHash = prevTxHash;
        this.txHash = txHash;
        this.signature = signature;
        this.prevOutputIndex = prevOutputIndex;
        this.value = value;
    }

    public byte[] getPrevOutputHash() {
        return this.prevTxHash;
    }

    public byte[] getTxHash() {
        return this.txHash;
    }

    public long getValue() {
        return this.value;
    }

    public int getOutputIndex() {
        return this.prevOutputIndex;
    }

    public boolean valid() {
        boolean valid;
        try {
            PublicKey publicKey = KeyUtil.parsePublicKey(this.prevOutput.getRecipientPubKey());
            valid = SigUtil.verify(publicKey, this.signature,
                    (ByteUtil.toHexString(prevTxHash) + prevOutputIndex).getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return valid;
    }

    public boolean equals(TxInput input) {
        return (
                Arrays.equals(this.txHash, input.getTxHash()) &
                this.prevOutputIndex == input.getOutputIndex() &
                this.value == input.getValue()
        );
    }

    public static int sum(ArrayList<TxInput> inputs) {
        int sum = 0;
        for (TxInput input : inputs) {
            sum += input.value;
        }
        return sum;
    }

    public static JSONArray arrayToJson(ArrayList<TxInput> inputs) {
        JSONArray jsonArray = new JSONArray();
        for (TxInput input : inputs) {
            jsonArray.put(input.toJson());
        }
        return jsonArray;
    }

    public JSONObject toJson() {
        String json = String.format("{tx_hash: %s, sig: %s, prev_output_hash: %s, value: %s, output_index: %s}",
                (this.txHash == null ? "" : ByteUtil.toHexString(this.txHash)),
                ByteUtil.toHexString(this.signature),
                ByteUtil.toHexString(this.prevTxHash),
                this.value, this.prevOutputIndex
        );
        return new JSONObject(json);
    }

    public String toString() {
        return this.toStringWithSuffix(", ");
    }

    public String toStringWithSuffix(String suffix) {
        String encoded = "TxInputData {";
        encoded += "tx_hash=" + (this.txHash == null ? "" : ByteUtil.toHexString(this.txHash)) + suffix;
        encoded += "prev_out_hash=" + ByteUtil.toHexString(this.prevTxHash) + suffix;
        encoded += "sig=" + ByteUtil.toHexString(this.signature) + suffix;
        encoded += "output_index=" + this.prevOutputIndex + suffix;
        encoded += "value=" + this.value;
        encoded += "}";
        return encoded;
    }

    public String toRawStringWithSuffix(String suffix) {
        return ((this.txHash == null ? "" : ByteUtil.toHexString(this.txHash)) +
                suffix + this.value + suffix + this.prevOutputIndex);
    }

    public static String arrayToString(ArrayList<TxInput> array) {
        return arrayToStringWithSuffix(array, ", ");
    }

    public static String arrayToStringWithSuffix(ArrayList<TxInput> array, String suffix) {
        String out = "[";
        for (TxInput output : array) {
            out += output.toStringWithSuffix(", ") + suffix;
        }
        out += "]";
        return out;
    }

}
