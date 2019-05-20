package core.transaction;

import config.Constants;
import errors.TransactionException;
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

    public void validate() throws TransactionException {
        if (!this.validTimestamp())
            throw new TransactionException("Timestamp invalid");
        if (this.inputs == null)
            throw new TransactionException("Transaction inputs null");
        if (this.outputs == null)
            throw new TransactionException("Transaction outputs null");
        if (!this.validInputs())
            throw  new TransactionException("Inputs invalid");
        if (!this.validOutputs())
            throw new TransactionException("Outputs invalid");
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
        if (!this.validInputs())
            exceptions.add(new TransactionException("Inputs invalid"));
        if (!this.validOutputs())
            exceptions.add(new TransactionException("Outputs invalid"));
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
                ByteUtil.toHexString(publicKey.getEncoded())
        );
    }

    public boolean equals(AbstractTransaction transaction) {
        if (transaction.getInputs() == null && this.getInputs() != null) return false;
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
        encoded += "hash=" + ByteUtil.toHexString(this.hash) + suffix;
        encoded += "sig=" + ByteUtil.toHexString(this.signature) + suffix;
        encoded += "timestamp=" + this.timestamp + suffix;
        encoded += "inputs=" + TxInput.arrayToStringWithSuffix(this.inputs, " ") + suffix;
        encoded += "outputs=" + TxOutput.arrayToStringWithSuffix(this.outputs, " ");
        encoded += "}";
        return encoded;
    }

    public JSONObject toJson() {
        String json = String.format("{timestamp: %s, hash: %s, pub_key: %s, signature: %s, inputs: %s, outputs: %s}",
                this.timestamp,
                ByteUtil.toHexString(this.hash),
                ByteUtil.toHexString(this.publicKey.getEncoded()),
                ByteUtil.toHexString(this.signature),
                TxInput.arrayToJson(this.inputs),
                TxOutput.arrayToJson(this.outputs)
        );
        return new JSONObject(json);
    }

}
