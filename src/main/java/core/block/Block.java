package core.block;

import core.transaction.AbstractTransaction;
import core.transaction.CoinbaseTransaction;
import core.transaction.Transaction;
import crypto.HashUtil;
import errors.BlockException;
import util.ByteUtil;
import util.StringUtil;

import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Block extends AbstractBlock {

    public Block(byte[] hash,
                         byte[] previousBlockHash,
                         byte[] txRoot,
                         byte[] miner,
                         long timestamp,
                         int difficulty,
                         int index) {
        super(hash, previousBlockHash, txRoot, miner, timestamp, difficulty, index);
    }

    public Block(byte[] hash, byte[] previousBlockHash, byte[] miner, int difficulty, int index) {
        super(hash, previousBlockHash, miner, difficulty, index);
        this.computeHash();
    }

    public Block(byte[] previousBlockHash, byte[] miner, int difficulty, int index) {
        super(null, previousBlockHash, miner, difficulty, index);
        this.computeHash();
    }

    public boolean equals(Block block) {
        return (
                ByteUtil.arraysEqual(this.getHash(), block.getHash()) &&
                ByteUtil.arraysEqual(this.getMiner(), block.getMiner()) &&
                ByteUtil.arraysEqual(this.getPreviousBlockHash(), block.getPreviousBlockHash()) &&
                this.getTimestamp() == block.getTimestamp() &&
                this.getDifficulty() == block.getDifficulty()
        );
    }

    public boolean valid(byte[] prevBlockHash) {
        return this.validTimestamp() & this.prevHashMatches(prevBlockHash) & this.validHash();
        // TODO: transaction verification (merkle root)
    }

    public boolean valid() {
        if (this.hash == null) return false;
        boolean time = this.getTimestamp() < System.currentTimeMillis();
        boolean prevHashMatches = this.getPreviousBlockHash() != null;
        boolean validHash = this.getStringHash()
                .startsWith(StringUtil.repeat("0", (int)this.getDifficulty()));
        return time & prevHashMatches & validHash;
    }

    public void validate() throws BlockException {
        if (this.hash == null)
            throw new BlockException("Hash is null");
        if (this.timestamp > System.currentTimeMillis())
            throw new BlockException("Timestamp invalid");
        if (this.getStringHash()
                .startsWith(StringUtil.repeat("0", (int)this.getDifficulty())))
            throw new BlockException("Hash invalid");
    }

    private boolean prevHashMatches(byte[] prevBlockHash) {
        return Arrays.equals(this.getPreviousBlockHash(), prevBlockHash);
    }

    private boolean validTimestamp() {
        return this.getTimestamp() < System.currentTimeMillis();
    }

    private boolean validHash() {
        return this.getStringHash()
                .startsWith(StringUtil.repeat("0", (int)this.getDifficulty()));
    }

    private void addCoinbaseTransaction() {
        try {
            this.getTransactions().add(0, CoinbaseTransaction.generate(this.getMiner()));
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    };

    public void addTransaction(AbstractTransaction transaction) {
        this.getTransactions().add(transaction);
    }

    public void addTransactions(List<AbstractTransaction> transactions) {
        this.getTransactions().addAll(transactions);
    }

    public void computeHash()  {
        this.timestamp = System.currentTimeMillis();
        this.hash = HashUtil.sha256((getHeaderString() + this.getNonce()).getBytes());
        if (this.validHash()) this.addCoinbaseTransaction();
        this.nonce++;
    }

    private String getHeaderString() {
        String txRootHash = this.getTransactionsRootHash() == null ? "" :
                ByteUtil.toHexString(this.getTransactionsRootHash());
        String prevBlockHash = this.getPreviousBlockHash() == null ? "" :
                ByteUtil.toHexString(this.getPreviousBlockHash());
        return txRootHash + prevBlockHash + this.nonce + this.getTimestamp();
    }
}
