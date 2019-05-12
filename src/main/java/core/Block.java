package core;

import core.transaction.CoinbaseTransaction;
import core.transaction.Transaction;
import crypto.HashUtil;
import util.ByteUtil;
import util.StringUtil;

import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Block {

    // NOTE: do not employ higher level properties such as block height -> construct them at higher levels

    // BLOCK HEADER
    private byte[] previousBlockHash;
    private byte[] transactionsRootHash;
    private long timestamp;
    private long nonce = 0;

    // BODY
    private int difficulty;
    private int index;
    private byte[] miner;
    private byte[] hash;
    private ArrayList<Transaction> transactions;

    public Block(byte[] hash, byte[] previousBlockHash, byte[] miner, int difficulty, int index) {
        this.transactions = new ArrayList<>();
        this.previousBlockHash = previousBlockHash;
        this.index = index;
        this.difficulty = difficulty;
    }

    public Block(byte[] previousBlockHash, byte[] miner, int difficulty, int index) {
        transactions = new ArrayList<>();
        this.previousBlockHash = previousBlockHash;
        this.index = index;
        this.miner = miner;
        this.difficulty = difficulty;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public byte[] getPreviousBlockHash() {
        return this.previousBlockHash;
    }

    public byte[] getHash() {
        return this.hash;
    }

    public byte[] getTransactionsRootHash() {
        return this.transactionsRootHash;
    }

    public String getStringHash() {
        if (this.hash == null) return "";
        return ByteUtil.toHexString(this.hash);
    }

    public byte[] getMiner() {
        return this.miner;
    }

    public boolean isParentOf(Block block) {
        return ByteUtil.arraysEqual(block.getHash(), this.previousBlockHash);
    }

    public boolean valid(byte[] prevBlockHash) {
        return this.validTimestamp() & this.prevHashMatches(prevBlockHash) & this.validHash();
        // TODO: transaction verification (merkle root)
        // TODO: hash byte length verification char[32]
    }

    public boolean valid() {
        if (this.hash == null) return false;
        boolean time = this.timestamp < System.currentTimeMillis();
        boolean prevHashMatches = this.previousBlockHash != null;
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
        return Arrays.equals(this.previousBlockHash, prevBlockHash);
    }

    private void addCoinbaseTransaction() {
        try {
            this.transactions.add(0, CoinbaseTransaction.generate(this.miner));
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    };

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

    public void computeHash()  {
        this.timestamp = System.currentTimeMillis();
        this.hash = HashUtil.sha256((getHeaderString() + this.nonce).getBytes());
        if (this.validHash()) this.addCoinbaseTransaction();
        this.nonce++;
    }

    private String getHeaderString() {
        String txRootHash = this.transactionsRootHash == null ? "" :
                ByteUtil.toHexString(this.transactionsRootHash);
        String prevBlockHash = this.previousBlockHash == null ? "" :
                ByteUtil.toHexString(this.previousBlockHash);
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
        encoded += "prev_block=" + ByteUtil.toHexString(this.previousBlockHash) + suffix;
        encoded += "hash=" + this.getStringHash() + suffix;
        // encoded += "transaction_root=" + ByteUtil.toHexString(this.transactionsRootHash) + suffix;
        encoded += "}";
        return encoded;
    }
}
