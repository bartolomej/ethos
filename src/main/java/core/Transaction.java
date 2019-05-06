package core;

public class Transaction {

    private long value;
    private long fee;
    private int nonce;
    private int v;
    private int r;
    private int s;
    private String signature;
    private String fromAddress;
    private String receiveAddress;

    public Transaction(String receiveAddress, long value, String signature) {
        this.receiveAddress = receiveAddress;
        this.signature = signature;
        this.value = value;
    }

    // TODO: add creation helper method create()
    // TODO: add transaction parse method
    // TODO: add toString() method
}
