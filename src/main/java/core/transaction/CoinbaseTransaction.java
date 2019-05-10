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

    public CoinbaseTransaction(byte[] receiveAddress) {
        this.output = new TxOutput(receiveAddress, BLOCK_REWARD);
        this.timestamp = System.currentTimeMillis();
        this.receiveAddress = receiveAddress;
        this.hash = HashUtil.sha256(this.getHeaderString());
    }

    public byte[] getHash() {
        return this.hash;
    }

    public byte[] getReceiveAddress() {
        return this.receiveAddress;
    }

    public TxOutput getOutput() {
        return this.output;
    }

    private String getHeaderString() {
        return (
                this.timestamp +
                this.output.toRawStringWithSuffix("") +
                ByteUtil.toHexString(this.receiveAddress)
        );
    }

    public String toString() {
        return this.toStringWithSuffix(", ");
    }

    @Override
    public String toStringWithSuffix(String suffix) {
        String encoded = "TransactionData {";
        encoded += "hash=" + ByteUtil.toHexString(this.hash) + suffix;
        encoded += "timestamp=" + this.timestamp + suffix;
        encoded += this.output;
        encoded += "}";
        return encoded;
    }

    @Override
    public String toRawStringWithSuffix(String suffix) {
        return (
                this.output + suffix +
                ByteUtil.toHexString(this.hash) + suffix
        );
    }

    public JSONObject toJson() {
        String json = String.format("{timestamp: %s, hash: %s, output: %s}",
                this.timestamp,
                ByteUtil.toHexString(this.hash),
                this.output.toString()
        );
        return new JSONObject(json);
    }

}
