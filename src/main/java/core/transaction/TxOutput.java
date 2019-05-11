package core.transaction;

import org.json.JSONObject;
import util.ByteUtil;
import util.Serializable;

import java.util.ArrayList;
import java.util.Arrays;

public class TxOutput implements Serializable {

    private byte[] txid;
    private long value;
    private int outputIndex;

    public TxOutput(byte[] receiveAddress, long value, int outputIndex) {
        this.txid = receiveAddress;
        this.outputIndex = outputIndex;
        this.value = value;
    }

    public byte[] getTxid() {
        return this.txid;
    }

    public int getOutputIndex() {
        return this.outputIndex;
    }

    public long getValue() {
        return this.value;
    }

    public boolean valid() {
        return this.txid != null;
    }

    public boolean equals(TxOutput output) {
        return Arrays.equals(this.txid, output.txid) & this.value == output.value;
    }

    public static long sum(ArrayList<TxOutput> inputs) {
        int sum = 0;
        for (TxOutput input : inputs) {
            sum += input.value;
        }
        return sum;
    }

    @Override
    public JSONObject toJson() {
        String json = String.format("{txid: %s, value: %s}",
                ByteUtil.toHexString(this.txid), this.value
        );
        return new JSONObject(json);
    }

    @Override
    public String toString() {
        return this.toStringWithSuffix(", ");
    }

    @Override
    public String toStringWithSuffix(String suffix) {
        String encoded = "TxOutputData {";
        encoded += "txid=" + ByteUtil.toHexString(this.txid) + suffix;
        encoded += "value=" + this.value;
        encoded += "}";
        return encoded;
    }

    @Override
    public String toRawStringWithSuffix(String suffix) {
        return (ByteUtil.toHexString(this.txid) + suffix + this.value);
    }

    public static String arrayToString(ArrayList<TxOutput> array) {
        return arrayToStringWithSuffix(array, ", ");
    }

    // TODO: remove when resolve interface issue
    public static String arrayToStringWithSuffix(ArrayList<TxOutput> array, String suffix) {
        String out = "TxOutputArrayData [";
        for (TxOutput output : array) {
            out += output.toStringWithSuffix(", ") + suffix;
        }
        out += "]";
        return out;
    }
}
