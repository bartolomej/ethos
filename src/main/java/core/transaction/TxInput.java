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
    private byte[] outputAddress;

    /** @Deprecated  */
    public TxInput(byte[] signature, byte[] prevTxHash, TxOutput prevOutput) {
        this.prevTxHash = prevOutput.getHashValue();
        this.prevTxHash = prevTxHash;
        this.signature = signature;
        this.prevOutputIndex = prevOutput.getOutputIndex();
        this.value = prevOutput.getValue();
        this.outputAddress = prevOutput.getRecipientPubKey();
    }

    public TxInput(byte[] signature, byte[] outputAddress, byte[] prevTxHash, int prevOutputIndex, long value) {
        this.prevTxHash = prevTxHash;
        this.signature = signature;
        this.prevOutputIndex = prevOutputIndex;
        this.value = value;
        this.outputAddress = outputAddress;
    }

    public TxInput(byte[] signature, byte[] prevTxHash, int prevOutputIndex, long value) {
        this.prevTxHash = prevTxHash;
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
            PublicKey publicKey = KeyUtil.parsePublicKey(this.outputAddress);
            valid = SigUtil.verify(publicKey, this.signature,
                    (ByteUtil.encodeToBase64(prevTxHash) + prevOutputIndex).getBytes());
        } catch (Exception e) {
            return false;
        }
        return valid;
    }

    public boolean valid(byte[] outputAddress) {
        boolean valid;
        try {
            PublicKey publicKey = KeyUtil.parsePublicKey(outputAddress);
            valid = SigUtil.verify(publicKey, this.signature,
                    (ByteUtil.encodeToBase64(prevTxHash) + prevOutputIndex).getBytes());
        } catch (Exception e) {
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
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("sig", ByteUtil.encodeToBase64(this.signature));
        jsonObject.put("prev_tx", ByteUtil.encodeToBase64(this.prevTxHash));
        jsonObject.put("value", this.value);
        jsonObject.put("output_index", this.prevOutputIndex);
        return jsonObject;
    }

    public String toString() {
        return this.toStringWithSuffix(", ");
    }

    public String toStringWithSuffix(String suffix) {
        String encoded = "TxInputData {";
        encoded += "prev_tx=" + (this.prevTxHash == null ? "" : ByteUtil.encodeToBase64(this.prevTxHash)) + suffix;
        encoded += "sig=" + ByteUtil.encodeToBase64(this.signature) + suffix;
        encoded += "output_index=" + this.prevOutputIndex + suffix;
        encoded += "value=" + this.value;
        encoded += "}";
        return encoded;
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
