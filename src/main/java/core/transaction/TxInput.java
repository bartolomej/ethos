package core.transaction;

import com.google.gson.JsonArray;
import org.json.JSONArray;
import org.json.JSONObject;
import util.ByteUtil;
import util.Serializable;

import java.util.ArrayList;
import java.util.Arrays;

public class TxInput implements Serializable {

    public byte[] txid; // pointing to transaction containing outputs -> used for retrieving transaction
    public int outputIndex; // which output of that transaction is referenced
    // wallet software generated appropriate number of outputs (one for me, one for Alice)
    public long value;


    public TxInput(byte[] referenceAddress, long value, int outputIndex) { // pass raw values or object references ?
        this.txid = referenceAddress;
        this.outputIndex = outputIndex;
        this.value = value;
    }

    // constructor that accepts prev output
    public TxInput(TxOutput prevOutput) {
        this.txid = prevOutput.getTxid();
        this.outputIndex = prevOutput.getOutputIndex();
        this.value = prevOutput.getValue();
    }

    public byte[] getTxid() {
        return this.txid;
    }

    public long getValue() {
        return this.value;
    }

    public int getOutputIndex() {
        return this.outputIndex;
    }

    public boolean equals(TxInput input) {
        return Arrays.equals(this.txid, input.txid) &
                this.outputIndex == input.outputIndex & this.value == input.value;
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
            jsonArray.put(input.toJson().toString());
        }
        return jsonArray;
    }

    public JSONObject toJson() {
        String json = String.format("{txid: %s, value: %s, output_index: %s}",
                (this.txid == null ? "" : ByteUtil.toHexString(this.txid)), this.value, this.outputIndex
        );
        return new JSONObject(json);
    }

    public String toString() {
        return this.toStringWithSuffix(", ");
    }

    public String toStringWithSuffix(String suffix) {
        String encoded = "TxInputData {";
        encoded += "txid=" + (this.txid == null ? "" : ByteUtil.toHexString(this.txid)) + suffix;
        encoded += "output_index=" + this.outputIndex + suffix;
        encoded += "value=" + this.value;
        encoded += "}";
        return encoded;
    }

    public String toRawStringWithSuffix(String suffix) {
        return ((this.txid == null ? "" : ByteUtil.toHexString(this.txid)) +
                suffix + this.value + suffix + this.outputIndex);
    }

    public static String arrayToString(ArrayList<TxInput> array) {
        return arrayToStringWithSuffix(array, ", ");
    }

    // TODO: remove when resolve interface issue
    public static String arrayToStringWithSuffix(ArrayList<TxInput> array, String suffix) {
        String out = "[";
        for (TxInput output : array) {
            out += output.toStringWithSuffix(", ") + suffix;
        }
        out += "]";
        return out;
    }

}
