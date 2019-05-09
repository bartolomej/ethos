package core.transaction;

import org.json.JSONObject;
import util.ByteUtil;
import util.Serializable;

import java.util.ArrayList;

public class TxInput implements Serializable {

    public byte[] txid; // pointing to transaction containing outputs -> used for retrieving transaction
    public int outputIndex; // which output of that transaction is referenced
    // wallet software generated appropriate number of outputs (one for me, one for Alice)
    public long value;


    public TxInput(byte[] referenceTransaction, long value) { // pass raw values or object references ?
        this.txid = referenceTransaction;
        this.value = value;
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

    public static int sum(ArrayList<TxInput> inputs) {
        int sum = 0;
        for (TxInput input : inputs) {
            sum += input.value;
        }
        return sum;
    }

    @Override
    public JSONObject toJson() {
        String json = String.format("{txid: %s, value: %s, output_index: %s}",
                ByteUtil.toHexString(this.txid), this.value, this.outputIndex
        );
        return new JSONObject(json);
    }

    @Override
    public String toString() {
        return this.toRawStringWithSuffix("\n");
    }

    @Override
    public String toStringWithSuffix(String suffix) {
        return (
                "TXID: " + ByteUtil.toHexString(this.txid) + suffix +
                "VALUE: " + this.value + suffix +
                "OUTPUT_INDEX: " + this.outputIndex
        );
    }

    @Override
    public String toRawStringWithSuffix(String suffix) {
        return (ByteUtil.toHexString(this.txid) + suffix + this.value + suffix + this.outputIndex);
    }

}
