package core.transaction;

import crypto.HashUtil;
import crypto.KeyUtil;
import org.json.JSONObject;
import util.ByteUtil;
import util.Serializable;

import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;

public class CoinbaseTransaction extends Transaction implements Serializable {

    public static long BLOCK_REWARD = 100;

    private long timestamp;
    private byte[] hash;

    public static CoinbaseTransaction generate(byte[] publicKey) throws InvalidKeySpecException, InvalidKeyException {
        ArrayList<TxOutput> outputs = new ArrayList<>();
        outputs.add(new TxOutput(publicKey, CoinbaseTransaction.BLOCK_REWARD, 0));
        return new CoinbaseTransaction(publicKey, outputs);
    }

    public CoinbaseTransaction(byte[] publicKey, ArrayList<TxOutput> outputs) throws InvalidKeyException, InvalidKeySpecException { // publicKey == receiveAddress for now
        super(null, outputs, KeyUtil.parsePublicKey(publicKey));
        this.timestamp = System.currentTimeMillis();
        this.hash = HashUtil.sha256(getHeaderString().getBytes());
    }

    public byte[] getHash() {
        return this.hash;
    }

    public TxOutput getOutput() {
        return this.getOutputs().get(0);
    }

    public ArrayList<TxInput> getInputs() {
        return null;
    }

    public String getHeaderString() {
        return (
                this.timestamp +
                this.getOutputs().toString()
        );
    }

    @Override
    public boolean valid() {
        return (this.getInputs() == null && this.getOutputs().size() == 1);
    }

    @Override
    public void validate() {

    }

    @Override
    public boolean equals(Transaction transaction) {
        return (
                Arrays.equals(super.getSignature(), transaction.getSignature()) &
                        this.outputsEquals(transaction.getOutputs()) &
                        this.isSigValid() == transaction.isSigValid() &
                        TxOutput.sum(super.getOutputs()) == TxOutput.sum(transaction.getOutputs())
        );
    }

    private boolean outputsEquals(ArrayList<TxOutput> outputs1) {
        if (super.getOutputs().size() != outputs1.size()) return false;
        for (int i = 0; i < super.getOutputs().size(); i++) {
            if (!super.getOutputs().get(i).equals(outputs1.get(i)))
                return false;
        }
        return true;
    }

    public String toString() {
        return toStringWithSuffix(", ");
    }

    public String toStringWithSuffix(String suffix) {
        String encoded = "TransactionData {";
        encoded += "hash=" + ByteUtil.toHexString(this.hash) + suffix;
        encoded += "timestamp=" + this.timestamp + suffix;
        encoded += this.getOutputs();
        encoded += "}";
        return encoded;
    }

    public String toRawStringWithSuffix(String suffix) {
        return (
                this.getOutputs() + suffix +
                ByteUtil.toHexString(this.hash) + suffix
        );
    }

    public JSONObject toJson() {
        String json = String.format("{timestamp: %s, hash: %s, output: %s}",
                this.timestamp,
                ByteUtil.toHexString(this.hash),
                this.getOutputs().toString()
        );
        return new JSONObject(json);
    }

}
