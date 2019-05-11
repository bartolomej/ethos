package core.transaction;

import core.TransactionException;
import crypto.*;
import org.json.JSONObject;
import util.ArrayUtil;
import util.ByteUtil;
import util.Serializable;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;

public class Transaction implements Serializable {

    public static long MIN_FEE = 3;

    private long timestamp;
    private byte[] hash;
    private byte[] signature;
    private ArrayList<TxOutput> outputs;
    private ArrayList<TxInput> inputs;
    private PublicKey publicKey;


    public Transaction(ArrayList<TxInput> inputs,
                       ArrayList<TxOutput> outputs,
                       byte[] publicKey, byte[] signature,
                       byte[] hash, long timestamp) throws InvalidKeySpecException {
        this.timestamp = timestamp;
        this.publicKey = KeyUtil.parsePublicKey(publicKey);
        this.signature = signature;
        this.outputs = outputs;
        this.inputs = inputs;
        this.hash = hash;
    }

    public Transaction(ArrayList<TxInput> inputs, ArrayList<TxOutput> outputs, PublicKey publicKey, byte[] signature, byte[] hash, long timestamp) {
        this.timestamp = timestamp;
        this.publicKey = publicKey;
        this.signature = signature;
        this.outputs = outputs;
        this.inputs = inputs;
        this.hash = hash;
    }

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

    public byte[] getHash() {
        return this.hash;
    }

    public byte[] getSignature() {
        return this.signature;
    }

    public long getInputsValue() {
        return TxInput.sum(this.inputs);
    }

    public long getOutputsValue() {
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
        this.hash = HashUtil.sha256(this.getHeaderString().getBytes());
    }

    public void sign(byte[] privateKey) throws InvalidKeyException, InvalidKeySpecException {
        PrivateKey privateKey1 = KeyUtil.parsePrivateKey(privateKey);
        this.signature = SigUtil.sign(privateKey1, this.getHeaderString().getBytes());
        this.hash = HashUtil.sha256(this.getHeaderString().getBytes());
    }

    public boolean isSigValid() {
        try {
            return SigUtil.verify(this.publicKey, this.signature, this.getHeaderString().getBytes());
        } catch (Exception e) {
            return false;
        }
    }

    public boolean verify() throws SignatureException, InvalidKeyException {
        return SigUtil.verify(this.publicKey, this.signature, this.getHeaderString().getBytes());
    }

    public boolean valid() {
        return (
                this.validTimestamp() &&
                this.inputs != null && this.outputs != null && this.hash != null &&
                this.isSigValid() &&
                this.sufficientInputs() &&
                this.validFee()
        );
    }

    public void validate() throws TransactionException {
        if (!this.validTimestamp())
            throw new TransactionException("Timestamp invalid");
        if (this.inputs == null)
            throw new TransactionException("Transaction inputs null");
        if (this.outputs == null)
            throw new TransactionException("Transaction outputs null");
        if (this.signature == null)
            throw new TransactionException("Transaction signature missing");
        if (!this.isSigValid())
            throw new TransactionException("Transaction signature invalid");
        if (!this.sufficientInputs())
            throw new TransactionException("Insufficient inputs", this.getInputsOutputsMessage());
        if (!this.validFee())
            throw new TransactionException("Insufficient transaction fee");
    }

    public ArrayList<TransactionException> getAllExceptions() {
        ArrayList<TransactionException> exceptions = new ArrayList<>();
        if (!this.validTimestamp())
            exceptions.add(new TransactionException("Timestamp invalid"));
        if (this.inputs == null)
            exceptions.add(new TransactionException("Transaction inputs null"));
        if (this.outputs == null)
            exceptions.add(new TransactionException("Transaction outputs null"));
        if (this.signature == null)
            exceptions.add(new TransactionException("Transaction signature missing"));
        if (!this.isSigValid())
            exceptions.add(new TransactionException("Transaction signature invalid"));
        if (!this.sufficientInputs())
            exceptions.add(new TransactionException("Insufficient inputs", this.getInputsOutputsMessage()));
        if (!this.validFee())
            exceptions.add(new TransactionException("Insufficient transaction fee"));
        return exceptions;
    }

    public boolean validTimestamp() {
        return (this.timestamp < System.currentTimeMillis());
    }

    public boolean validFee() {
        return (TxInput.sum(this.inputs) - TxOutput.sum(this.outputs) >= MIN_FEE);
    }

    public boolean sufficientInputs() {
        return TxInput.sum(this.inputs) >= TxOutput.sum(this.outputs);
    }

    public String getHeaderString() {
        return (
                this.timestamp +
                this.outputs.toString() +
                this.inputs.toString() +
                ByteUtil.toHexString(publicKey.getEncoded())
        );
    }

    public boolean equals(Transaction transaction) {
        return (
                Arrays.equals(this.signature, transaction.signature) &
                this.inputs.equals(transaction.inputs) &
                this.outputs.equals(transaction.outputs) &
                this.isSigValid() == transaction.isSigValid() &
                outputs.equals(transaction.outputs) &
                inputs.equals(transaction.inputs) &
                TxInput.sum(this.inputs) - TxOutput.sum(this.outputs) ==
                        TxInput.sum(transaction.inputs) - TxOutput.sum(transaction.outputs)
        );
    }

    private String getInputsOutputsMessage() {
        return "inputs=" + this.inputs.toString() + ", outputs=" + this.outputs;
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
        String json = String.format("{timestamp: %s, hash: %s, signature: %s, inputs: %s, outputs: %s}",
                this.timestamp,
                ByteUtil.toHexString(this.hash),
                ByteUtil.toHexString(this.signature),
                this.inputs.toString(),
                this.outputs.toString()
        );
        return new JSONObject(json);
    }

}
