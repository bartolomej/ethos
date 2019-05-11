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
    private byte[] miner;
    private byte[] hash;
    private ArrayList<Transaction> transactions;

    public Block(int index, int difficulty, Block previousBlock) {
        transactions = new ArrayList<>();
        this.previousBlock = previousBlock;
        this.index = index;
        this.difficulty = difficulty;
    }

    public String getStringHash() {
        if (this.hash == null) return "";
        return ByteUtil.toHexString(this.hash);
    }

    public byte[] getMiner() {
        return this.miner;
    }

    public boolean valid(byte[] prevBlockHash) {
        return this.validTimestamp() & this.prevHashMatches(prevBlockHash) & this.validHash();
        // TODO: transaction verification (merkle root)
        // TODO: hash byte length verification
    }

    public boolean valid() {
        if (this.hash == null) return false;
        boolean time = this.timestamp < System.currentTimeMillis();
        boolean prevHashMatches = this.previousBlock != null;
        boolean validHash = this.getStringHash().substring(0, this.difficulty)
                .equals(StringUtil.repeat("0", this.difficulty));
        return time & prevHashMatches & validHash;
    }

    private boolean validTimestamp() {
        return this.timestamp < System.currentTimeMillis();
    }

    private boolean validHash() {
        return this.getStringHash().substring(0, this.difficulty)
                .equals(StringUtil.repeat("0", this.difficulty));
    }

    private boolean prevHashMatches(byte[] prevBlockHash) {
        return Arrays.equals(this.previousBlock.hash, prevBlockHash);
    }

    public void addTransaction(Transaction transaction) throws TransactionException {
        transaction.validate();
        this.transactions.add(transaction);
    }

    public void addTransactions(List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            if (!transaction.valid()) return;
        }
        this.transactions.addAll(transactions);
    }

    public void computeHash() {
        this.timestamp = System.currentTimeMillis();
        this.hash = HashUtil.sha256(getHeaderString() + this.nonce);
        this.nonce++;
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
        String encoded = "";
        encoded += "BlockData {";
        encoded += "difficulty=" + this.difficulty + suffix;
        encoded += "index=" + this.index + suffix;
        encoded += "timestamp=" + this.timestamp + suffix;
        encoded += "miner=" + (this.miner == null ? "null" : ByteUtil.toHexString(this.miner)) + suffix;
        encoded += "prev_block=" + this.previousBlock.getStringHash() + suffix;
        encoded += "hash=" + this.getStringHash() + suffix;
        encoded += "transaction_root=" + ByteUtil.toHexString(this.transactionsRootHash) + suffix;
        encoded += "}";
        return encoded;
    }
}
