package core;

import java.util.ArrayList;

public class Block {

    private int difficulty;
    private int number;
    private long timestamp;
    private int verificationCount;
    private String miner;
    private String hash;
    private String prevHash;
    private String stateRoot;
    private ArrayList<Transaction> transactions;
    private String transactionsRoot;

    public Block(int number) {
        this.number = number;
        this.timestamp = System.currentTimeMillis();
    }

    public void addTransaction(Transaction transaction) {
        // TODO: transaction validation mechanism
    }

    private String toStringHeader() {
        return this.toStringHeaderWithSuffix("\n");
    }

    private String toStringHeaderWithSuffix(String suffix) {
        return (
                "DIFFICULTY: " + this.difficulty + suffix +
                "SEQ_NUMBER: " + this.number + suffix +
                "TIMESTAMP: " + this.timestamp + suffix +
                "MINER_ADDRESS: " + this.miner + suffix +
                "PREV_BLOCK_HASH: " + this.prevHash + suffix +
                "CURRENT_BLOCK_HASH: " + this.hash + suffix +
                "STATE_ROOT: " + this.stateRoot + suffix
        );
    }
}
