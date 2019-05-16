package core.block;

import core.transaction.AbstractTransaction;
import core.transaction.Transaction;
import errors.BlockException;
import org.json.JSONObject;
import util.ByteUtil;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractBlock {

    // BLOCK HEADER
    private byte[] previousBlockHash;
    private byte[] transactionRootHash;
    public long timestamp;
    public long nonce = 0;

    // BODY
    private int difficulty;
    private int index;
    private byte[] miner;
    public byte[] hash;
    private ArrayList<AbstractTransaction> transactions;

    public AbstractBlock(byte[] hash, byte[] previousBlockHash, byte[] miner, int difficulty, int index) {
        transactions = new ArrayList<>();
        this.hash = hash;
        this.miner = miner;
        this.transactions = new ArrayList<>();
        this.previousBlockHash = previousBlockHash;
        this.index = index;
        this.difficulty = difficulty;
    }

    public AbstractBlock(byte[] hash,
                         byte[] previousBlockHash,
                         byte[] txRoot,
                         byte[] miner,
                         long timestamp,
                         int difficulty,
                         int index) {
        transactions = new ArrayList<>();
        this.hash = hash;
        this.miner = miner;
        this.transactions = new ArrayList<>();
        this.previousBlockHash = previousBlockHash;
        this.transactionRootHash = txRoot;
        this.index = index;
        this.timestamp = timestamp;
        this.difficulty = difficulty;
    }

    abstract public void computeHash();

    abstract public boolean valid();

    abstract public void validate() throws BlockException;

    abstract public void addTransaction(AbstractTransaction tx);

    abstract public void addTransactions(List<AbstractTransaction> txs);

    public ArrayList<AbstractTransaction> getTransactions() {
        return this.transactions;
    }

    public long getDifficulty() {
        return this.difficulty;
    }

    public int getIndex() {
        return this.index;
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

    public long getNonce() {
        return this.nonce;
    }

    public byte[] getTransactionsRootHash() {
        return this.transactionRootHash;
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
        encoded += "tx_root=" + ByteUtil.toHexString(this.transactionRootHash) + suffix;
        encoded += "}";
        return encoded;
    }

    public JSONObject toJson() {
        String json = String.format("{hash: %s, difficulty: %s, index: %s, timestamp: %s, miner: %s, prev_block_hash: %s, tx_root: %s}",
                ByteUtil.toHexString(this.hash),
                this.difficulty,
                this.index,
                this.timestamp,
                ByteUtil.toHexString(this.miner),
                ByteUtil.toHexString(this.previousBlockHash),
                ByteUtil.toHexString(this.transactionRootHash)
        );
        return new JSONObject(json);
    }
}
