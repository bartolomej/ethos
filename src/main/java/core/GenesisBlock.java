package core;

import core.transaction.Transaction;
import crypto.KeyUtil;
import util.ByteUtil;
import util.StringUtil;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

public class GenesisBlock extends Block {

    public static byte[] PREV_HASH = new byte[]{0,0,0,0,0};
    public static int START_DIFFICULTY = 5;
    public static int INDEX = 0;
    public static byte[] GENERIS_ADDRESS = new byte[]{0,0,0,0}; // miner address

    public static GenesisBlock generate() {
        KeyUtil keys = KeyUtil.generate();
        PrivateKey privateKey = keys.getPrivateKey();
        PublicKey publicKey = keys.getPublicKey();
        return new GenesisBlock(publicKey.getEncoded());
    }

    public GenesisBlock(byte[] address) {
        super(GenesisBlock.PREV_HASH, address, GenesisBlock.START_DIFFICULTY, GenesisBlock.INDEX);
    }

    @Override
    public String getStringHash() {
        return super.getStringHash();
    }

    @Override
    public byte[] getMiner() {
        return super.getMiner();
    }

    @Override
    public boolean valid() {
        if (this.getStringHash().equals("")) return false;
        return this.getStringHash().substring(0, GenesisBlock.START_DIFFICULTY)
                .equals(StringUtil.repeat("0", GenesisBlock.START_DIFFICULTY));
    }

    @Override
    public void computeHash() {
        super.computeHash();
    }

    @Override
    public String toString() {
        return this.toStringHeaderWithSuffix("\n");
    }

    private String toStringHeaderWithSuffix(String suffix) {
        String encoded = "";
        encoded += "GenesisBlockData {";
        encoded += "difficulty=" + GenesisBlock.START_DIFFICULTY + suffix;
        encoded += "index=" + GenesisBlock.INDEX + suffix;
        encoded += "timestamp=" + this.getTimestamp() + suffix;
        encoded += "miner=" + ByteUtil.toHexString(GenesisBlock.GENERIS_ADDRESS) + suffix;
        encoded += "prev_block=" + ByteUtil.toHexString(GenesisBlock.PREV_HASH) + suffix;
        encoded += "hash=" + this.getStringHash() + suffix;
        //encoded += "transaction_root=" + ByteUtil.toHexString(this.getTransactionsRootHash()) + suffix;
        encoded += "}";
        return encoded;
    }
}
