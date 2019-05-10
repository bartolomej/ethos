package core.transaction;

import crypto.*;
import org.json.JSONObject;
import util.ArrayUtil;
import util.ByteUtil;
import util.Serializable;

import java.security.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Transaction implements Serializable {

    private static long MIN_FEE = 1;

    private long timestamp;
    private byte[] hash;
    private byte[] signature;
    private ArrayList<TxOutput> outputs;
    private ArrayList<TxInput> inputs;
    private PublicKey publicKey;

    public Transaction(ArrayList<TxInput> inputs, ArrayList<TxOutput> outputs) {
        this.timestamp = System.currentTimeMillis();
        this.outputs = outputs;
        this.inputs = inputs;
    }

    // TODO: define more initialization methods (abstract - for client, raw - for incoming transactions, raw - for db init)
    // TODO: add input validation (not null etc)
    // TODO: add development plan
    // TODO: unit test components in isolated boundaries !!
    public Transaction(ArrayList<TxInput> inputs, ArrayList<TxOutput> outputs, PublicKey publicKey, byte[] signature) {
        this.timestamp = System.currentTimeMillis();
        this.publicKey = publicKey;
        this.signature = signature;
        this.outputs = outputs;
        this.inputs = inputs;
    }

    public Transaction(ArrayList<TxInput> inputs, ArrayList<TxOutput> outputs, PublicKey publicKey) {
        this.timestamp = System.currentTimeMillis();
        this.publicKey = publicKey;
        this.outputs = outputs;
        this.inputs = inputs;
    }

    public long getValue() {
        return TxOutput.sum(this.outputs);
    }

    public ArrayList<TxInput> getInputs() {
        return this.inputs;
    }

    public ArrayList<TxOutput> getOutputs() {
        return this.outputs;
    }

    public void sign(PrivateKey privateKey) throws InvalidKeyException {
        this.signature = SigUtil.sign(privateKey, this.getHeaderString().getBytes());
        this.hash = HashUtil.sha256(this.getHeaderString());
    }

    public boolean verify() {
        try {
            return SigUtil.verify(this.publicKey, signature, this.getHeaderString().getBytes());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean valid() {
        boolean timestamp = this.timestamp < System.currentTimeMillis();
        boolean ioNotNull = this.inputs != null & this.outputs != null;
        boolean feeValid = (TxInput.sum(this.inputs) - TxOutput.sum(this.outputs)) > MIN_FEE;
        boolean inputsValid = TxInput.sum(this.inputs) > TxOutput.sum(this.outputs);
        return timestamp && this.verify() && ioNotNull && feeValid && inputsValid && this.hash != null;
    }

    public void validate() throws Exception {
        if (!(this.timestamp < System.currentTimeMillis()))
            throw new Exception("Timestamp invalid!");
        if (this.inputs == null)
            throw new Exception("Transaction inputs null");
        if (this.outputs == null)
            throw new Exception("Transaction outputs null");
        if (!this.verify())
            throw new Exception("Transaction signature invalid");
        if (TxInput.sum(this.inputs) < TxOutput.sum(this.outputs))
            throw new Exception("Insufficient inputs");
        if (!(TxInput.sum(this.inputs) - TxOutput.sum(this.outputs) > MIN_FEE))
            throw new Exception("Insufficient transaction fee");
         // TODO: add custom exceptions
        // TODO: add description: "Output sum larger than input sum"
    }

    private String getHeaderString() {
        return (
                this.timestamp +
                this.outputs.toString() +
                TxOutput.sum(this.outputs) +
                TxInput.sum(this.inputs)
        );
    }

    public boolean equals(Transaction transaction) {
        return (
                Arrays.equals(this.signature, transaction.signature) &
                this.inputs.equals(transaction.inputs) &
                this.outputs.equals(transaction.outputs) &
                this.verify() == transaction.verify() &
                // this.timestamp == transaction.timestamp &
                outputs.equals(transaction.outputs) &
                inputs.equals(transaction.inputs) &
                TxInput.sum(this.inputs) - TxOutput.sum(this.outputs) ==
                        TxInput.sum(transaction.inputs) - TxOutput.sum(transaction.outputs)
        );
    }

    public String toString() {
        return this.toStringWithSuffix(", ");
    }

    @Override
    public String toStringWithSuffix(String suffix) {
        String encoded = "TransactionData {";
        encoded += "hash=" + ByteUtil.toHexString(this.hash) + suffix;
        encoded += "sig=" + ByteUtil.toHexString(this.signature) + suffix;
        encoded += "timestamp=" + this.timestamp + suffix;
        encoded += "inputs=" + TxInput.arrayToStringWithSuffix(this.inputs, " ") + suffix;
        encoded += "outputs=" + TxOutput.arrayToStringWithSuffix(this.outputs, " ");
        encoded += "}";
        return encoded;
    }

    @Override
    public String toRawStringWithSuffix(String suffix) {
        return (
                TxOutput.sum(this.outputs) + suffix +
                ByteUtil.toHexString(this.signature) + suffix +
                TxInput.arrayToStringWithSuffix(this.inputs, "") + suffix +
                TxOutput.arrayToStringWithSuffix(this.outputs, "") + suffix +
                ByteUtil.toHexString(this.hash) + suffix
        );
    }

    public JSONObject toJson() {
        String json = String.format("{timestamp: %s, value: %s, hash: %s, signature: %s, from: %s, to: %s}",
                this.timestamp, TxOutput.sum(this.outputs),
                ByteUtil.toHexString(this.hash),
                ByteUtil.toHexString(this.signature),
                this.inputs.toString(),
                this.outputs.toString()
        );
        return new JSONObject(json);
    }

}
