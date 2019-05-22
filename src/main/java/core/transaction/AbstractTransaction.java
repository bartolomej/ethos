package core.transaction;

import core.block.AbstractBlock;
import crypto.HashUtil;
import crypto.KeyUtil;
import crypto.SigUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

public abstract class AbstractTransaction {

    public long timestamp;
    public byte[] hash;
    public byte[] signature;
    public ArrayList<TxOutput> outputs;
    public ArrayList<TxInput> inputs;
    public PublicKey publicKey;


    public AbstractTransaction(ArrayList<TxInput> inputs,
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

    public AbstractTransaction(ArrayList<TxInput> inputs,
                       ArrayList<TxOutput> outputs,
                       PublicKey publicKey,
                       byte[] signature,
                       byte[] hash,
                       long timestamp) {
        this.timestamp = timestamp;
        this.publicKey = publicKey;
        this.signature = signature;
        this.outputs = outputs;
        this.inputs = inputs;
        this.hash = hash;
    }

    abstract public JSONObject toJson();

    abstract public boolean equals(AbstractTransaction tx);

    abstract public String getHeaderString();

    abstract public boolean valid();

    public long getTimestamp() {
        return this.timestamp;
    }

    public byte[] getHash() {
        return this.hash;
    }

    public PublicKey getPublicKey() {
        return this.publicKey;
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

    // sign transaction to prevent altering content
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

    public static JSONArray arrayToJson(ArrayList<AbstractTransaction> transactions) {
        JSONArray jsonArray = new JSONArray();
        for (AbstractTransaction tx : transactions) {
            jsonArray.put(tx.toJson());
        }
        return jsonArray;
    }
}
