package core.block;

import config.Constants;
import core.transaction.AbstractTransaction;
import core.transaction.Transaction;
import crypto.HashUtil;
import crypto.KeyUtil;
import errors.BlockException;
import util.ByteUtil;
import util.StringUtil;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

public class GenesisBlock extends AbstractBlock {

    public static byte[] PREV_HASH = new byte[]{0,0,0,0,0};
    public static int INDEX = 0;

    public static GenesisBlock generate() {
        return GenesisBlock.generateBlock(Constants.START_DIFFICULTY);
    }

    public static GenesisBlock generate(int difficulty) {
        return GenesisBlock.generateBlock(difficulty);
    }

    private static GenesisBlock generateBlock(int difficulty) {
        KeyUtil keys = KeyUtil.generate();
        PrivateKey privateKey = keys.getPrivateKey();
        PublicKey publicKey = keys.getPublicKey();
        GenesisBlock genesis = new GenesisBlock(publicKey.getEncoded(), difficulty);
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

    public void validate() throws BlockException {
        if (this.getHash() == null)
            throw new BlockException("Hash is null");
        if (this.getTimestamp() > System.currentTimeMillis())
            throw new BlockException("Timestamp invalid");
        if (!this.getStringHash()
                .startsWith(StringUtil.repeat("0", (int)this.getDifficulty())))
            throw new BlockException("Hash invalid");
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
                ByteUtil.toHexString(this.getTransactionsRootHash());
        String prevBlockHash = this.getPreviousBlockHash() == null ? "" :
                ByteUtil.toHexString(this.getPreviousBlockHash());
        return txRootHash + prevBlockHash + this.getNonce() + this.getTimestamp();
    }
}
