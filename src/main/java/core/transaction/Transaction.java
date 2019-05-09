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
    private byte[] fromAddress;
    private byte[] receiveAddress;
    private ArrayList<TxOutput> outputs;
    private ArrayList<TxInput> inputs;
    private boolean verified = false;

    public Transaction(byte[] receiveAddress, ArrayList<TxInput> inputs, ArrayList<TxOutput> outputs) {
        this.receiveAddress = receiveAddress;
        this.outputs = outputs;
        this.inputs = inputs;
    }

    // TODO: define more initialization methods (abstract - for client, raw - for incoming transactions, raw - for db init)
    // TODO: add input validation (not null etc)
    // TODO: add development plan
    // TODO: unit test components in isolated boundaries !!
    public Transaction(byte[] receiveAddress, ArrayList<TxInput> inputs, ArrayList<TxOutput> outputs, byte[] signature) {
        this.receiveAddress = receiveAddress;
        this.signature = signature;
        this.outputs = outputs;
        this.inputs = inputs;
    }

    public long getValue() {
        return TxOutput.sum(this.outputs);
    }

    public void sign(PrivateKey privateKey) throws InvalidKeyException {
        this.signature = SigUtil.sign(privateKey, this.getHeaderString().getBytes());
        this.hash = HashUtil.sha256(this.getHeaderString());
    }

    public boolean verify(PublicKey publicKey) {
        try {
            this.verified = SigUtil.verify(publicKey, signature, this.getHeaderString().getBytes());
        } catch (Exception e) {
            return false;
        }
        return this.verified;
    }

    public boolean valid() {
        boolean timestamp = this.timestamp < System.currentTimeMillis();
        boolean ioNotNull = this.inputs != null & this.outputs != null;
        boolean feeValid = (TxInput.sum(this.inputs) - TxOutput.sum(this.outputs)) > MIN_FEE;
        boolean inputsValid = TxInput.sum(this.inputs) > TxOutput.sum(this.outputs);
        return timestamp & this.verified & ioNotNull & feeValid & inputsValid;
    }

    private String getHeaderString() {
        return (
                this.timestamp +
                this.outputs.toString() +
                ByteUtil.toHexString(this.fromAddress) +
                ByteUtil.toHexString(this.receiveAddress)
        );
    }

    public boolean equals(Transaction transaction) {
        return (
                Arrays.equals(this.signature, transaction.signature) &
                this.receiveAddress.equals(transaction.receiveAddress) &
                this.fromAddress.equals(transaction.receiveAddress) &
                this.verified == transaction.verified &
                this.timestamp == transaction.timestamp &
                outputs.equals(transaction.outputs) &
                inputs.equals(transaction.inputs) &
                TxInput.sum(this.inputs) - TxOutput.sum(this.outputs) ==
                        TxInput.sum(transaction.inputs) - TxOutput.sum(transaction.outputs)
        );
    }

    public String toString() {
        return this.toStringWithSuffix("\n");
    }

    @Override
    public String toStringWithSuffix(String suffix) {
        return (
                "VALUE: " + TxOutput.sum(this.outputs) + suffix +
                "SIG: " + ByteUtil.toHexString(this.signature) + suffix +
                "FROM: " + ByteUtil.toHexString(this.fromAddress) + suffix +
                "TO: " + ByteUtil.toHexString(this.receiveAddress) + suffix +
                "HASH: " + ByteUtil.toHexString(this.hash) + suffix
        );
    }

    @Override
    public String toRawStringWithSuffix(String suffix) {
        return (
                TxOutput.sum(this.outputs) + suffix +
                ByteUtil.toHexString(this.signature) + suffix +
                ByteUtil.toHexString(this.fromAddress) + suffix +
                ByteUtil.toHexString(this.receiveAddress) + suffix +
                ByteUtil.toHexString(this.hash) + suffix
        );
    }

    public JSONObject toJson() {
        String json = String.format("{timestamp: %s, value: %s, hash: %s, signature: %s, from: %s, to: %s}",
                this.timestamp, TxOutput.sum(this.outputs),
                ByteUtil.toHexString(this.hash),
                ByteUtil.toHexString(this.signature),
                ByteUtil.toHexString(this.fromAddress),
                ByteUtil.toHexString(this.receiveAddress)
        );
        return new JSONObject(json);
    }

}
