package core.transaction;

import crypto.HashUtil;
import crypto.KeyUtil;
import org.json.JSONObject;
import util.ByteUtil;

import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;

public class CoinbaseTransaction extends AbstractTransaction {

    public static long BLOCK_REWARD = 100;

    public static CoinbaseTransaction generate(byte[] recipientPubKey) throws InvalidKeySpecException, InvalidKeyException {
        ArrayList<TxOutput> outputs = new ArrayList<>();
        outputs.add(new TxOutput(recipientPubKey, CoinbaseTransaction.BLOCK_REWARD, 0));
        long timestamp = System.currentTimeMillis();
        return new CoinbaseTransaction(recipientPubKey, null, timestamp, outputs);
    }

    public CoinbaseTransaction(byte[] recipientPubKey, byte[] signature, long timestamp, ArrayList<TxOutput> outputs) throws InvalidKeyException, InvalidKeySpecException { // recipientPubKey == receiveAddress for now
        super(null, outputs, recipientPubKey, signature, null, timestamp);
        this.hash = HashUtil.sha256(this.getHeaderString().getBytes());
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

    public boolean valid() {
        return (this.getInputs() == null && this.getOutputs().size() == 1);
    }

    public ArrayList<Exception> getAllExceptions() {
        ArrayList<Exception> exceptions = new ArrayList<>();
        if (this.inputs == null)
            exceptions.add(new Exception("Transaction inputs null"));
        if (this.outputs == null)
            exceptions.add(new Exception("Transaction outputs null"));
        if (this.signature == null)
            exceptions.add(new Exception("Signature null"));
        return exceptions;
    }

    public boolean equals(AbstractTransaction transaction) {
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
                TxOutput.arrayToJson(this.getOutputs())
        );
        return new JSONObject(json);
    }

}
