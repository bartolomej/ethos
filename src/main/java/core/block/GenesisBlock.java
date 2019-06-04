package core.block;

import config.Constants;
import core.transaction.AbstractTransaction;
import crypto.HashUtil;
import crypto.KeyUtil;
import java.lang.Exception;
import org.json.JSONObject;
import util.ByteUtil;
import util.StringUtil;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class GenesisBlock extends AbstractBlock {

    public static byte[] PREV_HASH = new byte[]{0,0,0,0,0};
    public static int INDEX = 0;

    public static GenesisBlock generate() {
        return GenesisBlock.generate(Constants.START_DIFFICULTY);
    }

    public static GenesisBlock generate(int difficulty) {
        KeyUtil keys = KeyUtil.generate();
        PrivateKey privateKey = keys.getPrivateKey();
        PublicKey publicKey = keys.getPublicKey();
        GenesisBlock genesis = new GenesisBlock(publicKey.getEncoded(), difficulty);
        genesis.computeHash();
        return genesis;
    }

    public static GenesisBlock generate(byte[] miner, int difficulty) {
        GenesisBlock genesis = new GenesisBlock(miner, difficulty);
        genesis.computeHash();
        return genesis;
    }

    public GenesisBlock(byte[] miner, int difficulty) {
        super(null, GenesisBlock.PREV_HASH, miner, difficulty, GenesisBlock.INDEX);
    }

    public boolean valid() {
        return (
                this.getHash() != null &&
                this.getTimestamp() < System.currentTimeMillis() &&
                this.getStringHash()
                    .startsWith(StringUtil.repeat("0", (int)this.getDifficulty()))
        );
    }

    public ArrayList<Exception> getAllExceptions() throws Exception {
        ArrayList<Exception> exceptions = new ArrayList<>();
        if (this.getHash() == null)
            exceptions.add(new Exception("Hash is null"));
        if (this.getTimestamp() > System.currentTimeMillis())
            exceptions.add(new Exception("Timestamp invalid"));
        if (!this.getStringHash().startsWith(StringUtil.repeat("0", (int)this.getDifficulty())))
            exceptions.add(new Exception("Hash invalid"));
        return exceptions;
    }

    public void addTransaction(AbstractTransaction tx) {}

    public void addTransactions(List<AbstractTransaction> txs) {}

    public boolean equals(Block block) {
        return (
                ByteUtil.arraysEqual(this.getHash(), block.getHash()) &&
                ByteUtil.arraysEqual(this.getMiner(), block.getMiner()) &&
                this.getTimestamp() == block.getTimestamp() &&
                this.getDifficulty() == block.getDifficulty()
        );
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
        return txRootHash + prevBlockHash + this.getNonce() + this.getTimestamp();
    }

    public String toString() {
        return this.toStringHeaderWithSuffix("\n");
    }

    private String toStringHeaderWithSuffix(String suffix) {
        String encoded = "";
        encoded += "BlockData {";
        encoded += "difficulty=" + this.getDifficulty() + suffix;
        encoded += "index=" + this.getHeight() + suffix;
        encoded += "timestamp=" + this.timestamp + suffix;
        encoded += "miner=" + (this.getMiner() == null ? "null" : ByteUtil.encodeToBase64(this.getMiner())) + suffix;
        encoded += "prev_block=" + null + suffix;
        encoded += "hash=" + this.getStringHash() + suffix;
        //encoded += "tx_root=" + ByteUtil.encodeToBase64(this.transactionRootHash) + suffix;
        encoded += "}";
        return encoded;
    }

    public JSONObject toJson() {
        String json = String.format("{hash: %s, difficulty: %s, index: %s, timestamp: %s, miner: %s, prev_block_hash: %s}",
                ByteUtil.encodeToBase64(this.hash),
                this.getDifficulty(),
                this.getHeight(),
                this.timestamp,
                ByteUtil.encodeToBase64(this.getMiner()),
                null
                //ByteUtil.encodeToBase64(this.transactionRootHash) TODO: add txRoot
        );
        return new JSONObject(json);
    }

    public JSONObject toJsonFull() {
        String json = String.format("{hash: %s, difficulty: %s, index: %s, timestamp: %s, miner: %s, prev_block_hash: %s, tx: %s}",
                ByteUtil.encodeToBase64(this.hash),
                this.getDifficulty(),
                this.getHeight(),
                this.timestamp,
                ByteUtil.encodeToBase64(this.getMiner()),
                null,
                //ByteUtil.encodeToBase64(this.transactionRootHash) TODO: add txRoot
                AbstractTransaction.arrayToJson(this.getTransactions())
        );
        return new JSONObject(json);
    }
}
