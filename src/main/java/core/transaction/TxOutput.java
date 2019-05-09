package core.transaction;

import org.json.JSONObject;
import util.ByteUtil;
import util.Serializable;

import java.util.ArrayList;

public class TxOutput implements Serializable {

    private byte[] txid;
    private long value;

    public TxOutput(byte[] receiveAddress, long value) {
        this.txid = receiveAddress;
        this.value = value;
    }

    public byte[] getTxid() {
        return this.txid;
    }

    public long getValue() {
        return this.value;
    }

    public static int sum(ArrayList<TxOutput> inputs) {
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
        return this.toRawStringWithSuffix("\n");
    }

    @Override
    public String toStringWithSuffix(String suffix) {
        return (
                "TXID: " + ByteUtil.toHexString(this.txid) + suffix +
                "VALUE: " + this.value
        );
    }

    @Override
    public String toRawStringWithSuffix(String suffix) {
        return (ByteUtil.toHexString(this.txid) + suffix + this.value);
    }
}
