package core.block;

import core.transaction.AbstractTransaction;
import core.transaction.CoinbaseTransaction;
import core.transaction.Transaction;
import org.json.JSONObject;
import util.ByteUtil;

import javax.swing.table.AbstractTableModel;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
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

    abstract public ArrayList<Exception> getAllExceptions() throws Exception;

    public void addCoinbaseTransaction() {
        try {
            this.getTransactions().add(0, CoinbaseTransaction.generate(this.getMiner()));
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    };

    public void addTransaction(AbstractTransaction tx) {
        this.transactions.add(tx);
    };

    public void addTransactions(List<AbstractTransaction> txs) {
        this.transactions.addAll(txs);
    };

    public CoinbaseTransaction getCoinbaseTransaction() {
        return (CoinbaseTransaction) this.transactions.get(0);
    }

    public ArrayList<AbstractTransaction> getTransactions() {
        return this.transactions;
    }

    public long getDifficulty() {
        return this.difficulty;
    }

    public int getHeight() {
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
        return this.getHash();
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

    abstract public String toString();

    abstract public JSONObject toJson();

    abstract public JSONObject toJsonFull();
}
