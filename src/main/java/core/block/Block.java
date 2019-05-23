package core.block;

import core.transaction.AbstractTransaction;
import crypto.HashUtil;
import java.lang.Exception;
import org.json.JSONObject;
import util.ByteUtil;
import util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Block extends AbstractBlock {

    public Block(byte[] hash,
                 byte[] previousBlockHash,
                 byte[] txRootHash,
                 byte[] miner,
                 long timestamp,
                 int difficulty,
                 int index) {
        super(hash, previousBlockHash, txRootHash, miner, timestamp, difficulty, index);
        // TODO: validate txRootHash
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
        // TODO: transaction verification (merkle root) -> validate txRootHash
    }

    public boolean valid() {
        if (this.hash == null) return false;
        boolean time = this.getTimestamp() < System.currentTimeMillis();
        boolean prevHashMatches = this.getPreviousBlockHash() != null;
        boolean validHash = this.getStringHash()
                .startsWith(StringUtil.repeat("0", (int)this.getDifficulty()));
        return time & prevHashMatches & validHash;
    }

    public ArrayList<Exception> getAllExceptions() {
        ArrayList<Exception> exceptions = new ArrayList<>();
        if (this.hash == null)
            exceptions.add(new Exception("Hash is null"));
        if (this.timestamp > System.currentTimeMillis())
            exceptions.add(new Exception("Timestamp invalid"));
        if (this.getStringHash().startsWith(StringUtil.repeat("0", (int)this.getDifficulty())))
            exceptions.add(new Exception("Hash invalid"));
        return exceptions;
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

    public void addTransactions(List<AbstractTransaction> transactions) {
        this.getTransactions().addAll(transactions);
    }

    public void computeHash()  {
        this.timestamp = System.currentTimeMillis();
        this.hash = HashUtil.sha256((getHeaderString() + this.getNonce()).getBytes());
        this.nonce++;
    }

    private String getHeaderString() {
        String txRootHash = this.getTransactionsRootHash() == null ? "" :
                ByteUtil.encodeToBase64(this.getTransactionsRootHash());
        String prevBlockHash = this.getPreviousBlockHash() == null ? "" :
                ByteUtil.encodeToBase64(this.getPreviousBlockHash());
        return txRootHash + prevBlockHash + this.nonce + this.getTimestamp();
    }

    public String toString() {
        return this.toStringHeaderWithSuffix("\n");
    }

    private String toStringHeaderWithSuffix(String suffix) {
        String encoded = "";
        encoded += "BlockData {";
        encoded += "difficulty=" + this.getDifficulty() + suffix;
        encoded += "index=" + this.getIndex() + suffix;
        encoded += "timestamp=" + this.getTimestamp() + suffix;
        encoded += "miner=" + (this.getMiner() == null ? "null" : ByteUtil.encodeToBase64(this.getMiner())) + suffix;
        encoded += "prev_block=" + ByteUtil.encodeToBase64(this.getPreviousBlockHash()) + suffix;
        encoded += "hash=" + this.getStringHash() + suffix;
        encoded += "tx_root=" + ByteUtil.encodeToBase64(this.getTransactionsRootHash()) + suffix;
        encoded += "}";
        return encoded;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("hash", ByteUtil.encodeToBase64(this.getHash()));
        jsonObject.put("difficulty", this.getDifficulty());
        jsonObject.put("index", this.getIndex());
        jsonObject.put("timestamp", this.getTimestamp());
        jsonObject.put("miner", ByteUtil.encodeToBase64(this.getMiner()));
        jsonObject.put("prev_block", ByteUtil.encodeToBase64(this.getPreviousBlockHash()));
        jsonObject.put("tx_root", ByteUtil.encodeToBase64(this.getTransactionsRootHash()));
        return jsonObject;
    }

    public JSONObject toJsonFull() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("hash", ByteUtil.encodeToBase64(this.hash));
        jsonObject.put("difficulty", this.getDifficulty());
        jsonObject.put("index", this.getIndex());
        jsonObject.put("timestamp", this.getTimestamp());
        jsonObject.put("miner", ByteUtil.encodeToBase64(this.getMiner()));
        jsonObject.put("prev_block", ByteUtil.encodeToBase64(this.getPreviousBlockHash()));
        jsonObject.put("tx_root", ByteUtil.encodeToBase64(this.getTransactionsRootHash()));
        jsonObject.put("tx", AbstractTransaction.arrayToJson(this.getTransactions()));
        return jsonObject;
    }
}
