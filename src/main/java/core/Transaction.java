package core;

import crypto.*;
import util.BytesUtil;

import java.security.*;
import java.util.ArrayList;

public class Transaction {

    private long value;
    private long timestamp;
    private long fee;
    private int nonce;
    private byte[] hash;
    private byte[] signature;
    private String fromAddress;
    private String receiveAddress;
    private ArrayList<Transaction> outputs;
    private ArrayList<Transaction> inputs;

    public Transaction(String receiveAddress, long value) {
        this.receiveAddress = receiveAddress;
        this.value = value;
    }

    public Transaction(String receiveAddress, long value, byte[] signature) {
        this.receiveAddress = receiveAddress;
        this.signature = signature;
        this.value = value;
    }

    public void sign(PrivateKey privateKey) throws InvalidKeyException {
        this.signature = SigUtil.sign(privateKey, this.getHeaderString().getBytes());
        this.hash = HashUtil.sha256(this.getHeaderString());
    }

    public boolean verify(PublicKey publicKey) {
        boolean verified = false;
        boolean timestamp = this.timestamp < System.currentTimeMillis();
        try {
            verified = SigUtil.verify(publicKey, signature, this.getHeaderString().getBytes());
        } catch (Exception e) {
            return false;
        }
        // TODO: verify UTXO
        // TODO: verify fee
        return verified && timestamp;
    }

    private String getHeaderString() {
        return (
                this.timestamp +
                this.value +
                //BytesUtil.toHexString(this.hash) +
                this.fromAddress +
                this.receiveAddress
        );
    }

    public String toString() {
        return this.toStringWithSuffix("\n");
    }

    private String toStringWithSuffix(String suffix) {
        return (
                "VALUE: " + this.value + suffix +
                "SIG: " + BytesUtil.toHexString(this.signature) + suffix +
                "NONCE: " + this.nonce + suffix +
                "FROM: " + this.fromAddress + suffix +
                "TO: " + this.receiveAddress + suffix +
                "HASH: " + BytesUtil.toHexString(this.hash) + suffix
        );

    }

    // TODO: add creation helper method create()
    // TODO: add transaction parse method
    // TODO: add toString() method
}
