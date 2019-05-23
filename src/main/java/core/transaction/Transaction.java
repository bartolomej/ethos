package core.transaction;

import config.Constants;
import java.lang.Exception;
import crypto.*;
import org.json.*;
import util.ByteUtil;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;

public class Transaction extends AbstractTransaction {

    public Transaction(ArrayList<TxInput> inputs,
                       ArrayList<TxOutput> outputs,
                       byte[] publicKey, byte[] signature,
                       byte[] hash, long timestamp) throws InvalidKeySpecException {
        super(inputs, outputs, publicKey, signature, hash, timestamp);
    }

    public Transaction(ArrayList<TxInput> inputs,
                       ArrayList<TxOutput> outputs,
                       PublicKey publicKey) {
        super(inputs, outputs, publicKey, null, null, System.currentTimeMillis());
        this.hash = HashUtil.sha256(this.getHeaderString().getBytes());
    }


    public boolean valid() {
        return (
                this.validTimestamp() &&
                this.inputs != null && this.outputs != null && this.hash != null &&
                this.isSigValid() &&
                this.sufficientInputs() &&
                this.validFee() &&
                this.validInputs() &&
                this.validOutputs()
        );
    }

    public ArrayList<Exception> getAllExceptions() {
        ArrayList<Exception> exceptions = new ArrayList<>();
        if (!this.validTimestamp())
            exceptions.add(new Exception("Timestamp invalid"));
        if (!this.validInputs())
            exceptions.add(new Exception("Inputs invalid"));
        if (!this.validOutputs())
            exceptions.add(new Exception("Outputs invalid"));
        if (this.inputs == null)
            exceptions.add(new Exception("Transaction inputs null"));
        if (this.outputs == null)
            exceptions.add(new Exception("Transaction outputs null"));
        if (this.signature == null)
            exceptions.add(new Exception("Transaction signature missing"));
        if (!this.isSigValid())
            exceptions.add(new Exception("Transaction signature invalid"));
        if (!this.sufficientInputs())
            exceptions.add(new Exception("Insufficient inputs"));
        if (!this.validFee())
            exceptions.add(new Exception("Insufficient transaction fee"));
        return exceptions;
    }

    private boolean validInputs() {
        for (TxInput input : this.getInputs()) {
            if (!input.valid()) return false;
        }
        return true;
    }

    private boolean validOutputs() {
        for (TxOutput output : this.getOutputs()) {
            if (!output.valid()) return false;
        }
        return true;
    }

    public boolean validTimestamp() {
        return (this.timestamp < System.currentTimeMillis());
    }

    public boolean validFee() {
        return (TxInput.sum(this.inputs) - TxOutput.sum(this.outputs) >= Constants.MIN_TX_FEE);
    }

    public boolean sufficientInputs() {
        return TxInput.sum(this.inputs) >= TxOutput.sum(this.outputs);
    }

    public String getHeaderString() {
        return (
                this.timestamp +
                this.outputs.toString() +
                this.inputs.toString() +
                ByteUtil.encodeToBase64(publicKey.getEncoded())
        );
    }

    public boolean equals(AbstractTransaction transaction) {
        if (transaction instanceof CoinbaseTransaction) return false;
        return (
                Arrays.equals(this.signature, transaction.getSignature()) &
                this.inputsEquals(transaction.getInputs()) &
                this.outputsEquals(transaction.getOutputs()) &
                ByteUtil.arraysEqual(this.getSignature(), transaction.getSignature()) &
                TxInput.sum(this.inputs) == TxInput.sum(transaction.getInputs()) &&
                TxOutput.sum(this.outputs) == TxOutput.sum(transaction.getOutputs())
        );
    }

    public ArrayList<Exception> equalsWithException(Transaction transaction) throws Exception {
        ArrayList<Exception> arrayList = new ArrayList<>();
        if (!Arrays.equals(this.signature, transaction.signature))
            arrayList.add(new Exception("Signatures don't match"));
        if (!this.inputsEquals(transaction.inputs))
            arrayList.add(new Exception("Inputs don't match"));
        if (!this.outputsEquals(transaction.outputs))
            arrayList.add(new Exception("Outputs don't match"));
        if (!this.isSigValid() == transaction.isSigValid())
            arrayList.add(new Exception("Signature not valid"));
        if (!(TxInput.sum(this.inputs) == TxInput.sum(transaction.inputs)))
            arrayList.add(new Exception("Inputs sum don't match"));
        if (!(TxOutput.sum(this.outputs) == TxOutput.sum(transaction.outputs)))
            arrayList.add(new Exception("Outputs sum don't match"));
        return arrayList;
    }

    private boolean inputsEquals(ArrayList<TxInput> inputs1) {
        if (inputs1 == null) return false;
        if (this.inputs.size() != inputs1.size()) return false;
        for (int i = 0; i < this.inputs.size(); i++) {
            if (!this.inputs.get(i).equals(inputs1.get(i)))
                return false;
        }
        return true;
    }

    private boolean outputsEquals(ArrayList<TxOutput> outputs1) {
        if (this.outputs.size() != outputs1.size()) return false;
        for (int i = 0; i < this.outputs.size(); i++) {
            if (!this.outputs.get(i).equals(outputs1.get(i)))
                return false;
        }
        return true;
    }

    private String getInputsOutputsMessage() {
        return "inputs=" + this.inputs.toString() + ", outputs=" + this.outputs;
    }

    public String toString() {
        return this.toStringWithSuffix(", ");
    }

    public String toStringWithSuffix(String suffix) {
        String encoded = "TransactionData {";
        encoded += "hash=" + ByteUtil.encodeToBase64(this.hash) + suffix;
        encoded += "sig=" + ByteUtil.encodeToBase64(this.signature) + suffix;
        encoded += "timestamp=" + this.timestamp + suffix;
        encoded += "inputs=" + TxInput.arrayToStringWithSuffix(this.inputs, " ") + suffix;
        encoded += "outputs=" + TxOutput.arrayToStringWithSuffix(this.outputs, " ");
        encoded += "}";
        return encoded;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("timestamp", this.timestamp);
        jsonObject.put("hash", ByteUtil.encodeToBase64(this.hash));
        jsonObject.put("address", ByteUtil.encodeToBase64(this.getPublicKey().getEncoded()));
        jsonObject.put("signature", ByteUtil.encodeToBase64(this.signature));
        jsonObject.put("inputs", TxInput.arrayToJson(this.inputs));
        jsonObject.put("outputs", TxOutput.arrayToJson(this.outputs));
        return jsonObject;
    }

}
