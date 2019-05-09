package core.transaction;

import crypto.HashUtil;
import org.json.JSONObject;
import util.ByteUtil;
import util.Serializable;

import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

public class CoinbaseTransaction implements Serializable {

    public long BLOCK_REWARD = 100;

    private long timestamp;
    private byte[] hash;
    private byte[] receiveAddress;
    private TxOutput output;

    private CoinbaseTransaction(byte[] receiveAddress) {
        this.timestamp = System.currentTimeMillis();
        this.receiveAddress = receiveAddress;
        this.hash = HashUtil.sha256(this.getHeaderString());
        this.output = new TxOutput(receiveAddress, BLOCK_REWARD);
    }

    private String getHeaderString() {
        return (
                this.timestamp +
                this.output.toRawStringWithSuffix("") +
                ByteUtil.toHexString(this.receiveAddress)
        );
    }

    @Override
    public String toString() {
        return this.toStringWithSuffix("\n");
    }

    @Override
    public String toStringWithSuffix(String suffix) {
        return (
                "VALUE: " + this.output.toRawStringWithSuffix("") + suffix +
                "TO: " + ByteUtil.toHexString(this.receiveAddress) + suffix +
                "HASH: " + ByteUtil.toHexString(this.hash) + suffix
        );
    }

    @Override
    public String toRawStringWithSuffix(String suffix) {
        return (
                this.output.toRawStringWithSuffix("") + suffix +
                ByteUtil.toHexString(this.receiveAddress) + suffix +
                ByteUtil.toHexString(this.hash) + suffix
        );
    }

    @Override
    public JSONObject toJson() {
        String json = String.format("{timestamp: %s, value: %s, hash: %s, to: %s}",
                this.timestamp, this.output.toRawStringWithSuffix(""),
                ByteUtil.toHexString(this.hash),
                ByteUtil.toHexString(this.receiveAddress)
        );
        return new JSONObject(json);
    }

}
