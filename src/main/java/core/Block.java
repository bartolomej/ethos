package core;

import core.transaction.Transaction;
import crypto.HashUtil;
import util.ByteUtil;
import util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Block {

    // NOTE: do not employ higher level properties such as block height -> construct them at higher levels

    // BLOCK HEADER
    private Block previousBlock;
    private byte[] transactionsRootHash;
    private long timestamp;
    private long nonce = 0;

    // BODY
    private int difficulty;
    private int index;
    private String miner;
    public byte[] hash;
    private ArrayList<Transaction> transactions;

    public Block(int index, int difficulty, Block previousBlock) {
        this.previousBlock = previousBlock;
        this.index = index;
        this.difficulty = difficulty;
    }

    public String getStringHash() {
        if (this.hash == null) return "";
        return ByteUtil.toHexString(this.hash);
    }

    public boolean valid(byte[] prevBlockHash) {
        if (getStringHash().length() < 32) return false;
        boolean time = this.timestamp < System.currentTimeMillis();
        boolean prevHashMatches = Arrays.equals(this.previousBlock.hash, prevBlockHash);
        boolean hashValid = this.getStringHash().substring(0, this.difficulty)
                .equals(StringUtil.repeat("0", this.difficulty));
        return time & prevHashMatches & hashValid;
        // TODO: transaction verification (merkle root)
    }

    public boolean valid() {
        boolean time = this.timestamp < System.currentTimeMillis();
        boolean prevHashMatches = this.previousBlock != null;
        boolean hashValid = this.getStringHash().substring(0, this.difficulty)
                .equals(StringUtil.repeat("0", this.difficulty));
        return time & prevHashMatches & hashValid;
    }

    public void addTransaction(Transaction transaction) {
        if (!transaction.valid()) return;
        this.transactions.add(transaction);
    }

    public void addTransacctions(List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            if (!transaction.valid()) return;
        }
        this.transactions.addAll(transactions);
    }

    public void incrementNonce() {
        this.nonce++;
    }

    public void computeHash() {
        this.timestamp = System.currentTimeMillis();
        this.hash = HashUtil.sha256(getHeaderString() + this.nonce);
    }

    private String getHeaderString() {
        String txRootHash = this.transactionsRootHash == null ? "" :
                ByteUtil.toHexString(this.transactionsRootHash);
        String prevBlockHash = this.previousBlock == null ? "" :
                ByteUtil.toHexString(this.previousBlock.hash);
        return txRootHash + prevBlockHash + this.nonce + this.timestamp;
    }

    public String toString() {
        return this.toStringHeaderWithSuffix("\n");
    }

    private String toStringHeaderWithSuffix(String suffix) {
        return (
                "DIFFICULTY: " + this.difficulty + suffix +
                "SEQ_NUMBER: " + this.index + suffix +
                "TIMESTAMP: " + this.timestamp + suffix +
                "MINER_ADDRESS: " + this.miner + suffix +
                "PREV_BLOCK_HASH: " + (
                        this.previousBlock == null ? "" : this.previousBlock.getStringHash()
                ) + suffix +
                "CURRENT_BLOCK_HASH: " + this.getStringHash() + suffix +
                "TRANSACTION_ROOT: " + ByteUtil.toHexString(this.transactionsRootHash) + suffix
        );
    }
}
