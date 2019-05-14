package core;

import core.block.AbstractBlock;
import core.block.Block;
import core.block.GenesisBlock;
import crypto.KeyUtil;
import org.json.JSONObject;
import org.junit.Test;
import util.ByteUtil;
import util.StringUtil;

import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.Assert.*;

public class BlockTest {

    @Test
    public void genesisBlock() {
        int DIFFICULTY = 2;

        // first account
        KeyUtil keys1 = KeyUtil.generate();
        PrivateKey privateKey1 = keys1.getPrivateKey();
        PublicKey publicKey1 = keys1.getPublicKey();

        byte[] address1 = publicKey1.getEncoded();

        // blocks
        GenesisBlock genesisBlock = GenesisBlock.generate(DIFFICULTY);
        mine(genesisBlock);

        assertTrue(genesisBlock.valid());
    }

    @Test
    public void firstBlock() {
        int DIFFICULTY = 2;

        // first account
        KeyUtil keys1 = KeyUtil.generate();
        PrivateKey privateKey1 = keys1.getPrivateKey();
        PublicKey publicKey1 = keys1.getPublicKey();

        byte[] address1 = publicKey1.getEncoded();

        // blocks
        GenesisBlock genesisBlock = GenesisBlock.generate(DIFFICULTY);
        mine(genesisBlock);

        Block firstBlock = new Block(genesisBlock.getHash(), address1, 2, 1);
        mine(firstBlock);

        assertEquals(genesisBlock.getStringHash().substring(0, DIFFICULTY), StringUtil.repeat("0", DIFFICULTY));
        assertTrue(genesisBlock.valid());

        assertEquals(firstBlock.getStringHash().substring(0, DIFFICULTY), StringUtil.repeat("0", DIFFICULTY));
        assertTrue(firstBlock.valid());
        assertTrue(firstBlock.equals(firstBlock));

        // expected json format
        String json = String.format("{hash: %s, difficulty: %s, index: %s, timestamp: %s, miner: %s, prev_block_hash: %s}",
                ByteUtil.toHexString(firstBlock.getHash()),
                firstBlock.getDifficulty(),
                firstBlock.getIndex(),
                firstBlock.getTimestamp(),
                ByteUtil.toHexString(firstBlock.getMiner()),
                ByteUtil.toHexString(firstBlock.getPreviousBlockHash())
        );

        assertEquals(firstBlock.toJson().toString(), new JSONObject(json).toString());

        assertArrayEquals(firstBlock.getPreviousBlockHash(), genesisBlock.getHash());
    }

    @Test
    public void firstFewBlocksChaining() {
        int DIFFICULTY = 2;

        // first account
        KeyUtil keys1 = KeyUtil.generate();
        PrivateKey privateKey1 = keys1.getPrivateKey();
        PublicKey publicKey1 = keys1.getPublicKey();

        byte[] address1 = publicKey1.getEncoded();

        // blocks
        GenesisBlock genesisBlock = GenesisBlock.generate(DIFFICULTY);
        mine(genesisBlock);

        Block firstBlock = new Block(genesisBlock.getHash(), address1, DIFFICULTY, 1);
        mine(firstBlock);

        Block secondBlock = new Block(firstBlock.getHash(), address1, DIFFICULTY, 2);
        mine(secondBlock);

        Block thirdBlock = new Block(secondBlock.getHash(), address1, DIFFICULTY, 3);
        mine(thirdBlock);

        Block forthBlock = new Block(thirdBlock.getHash(), address1, DIFFICULTY, 4);
        mine(forthBlock);

        assertEquals(firstBlock.getStringHash().substring(0, DIFFICULTY), StringUtil.repeat("0", DIFFICULTY));
        assertEquals(secondBlock.getStringHash().substring(0, DIFFICULTY), StringUtil.repeat("0", DIFFICULTY));
        assertEquals(thirdBlock.getStringHash().substring(0, DIFFICULTY), StringUtil.repeat("0", DIFFICULTY));
        assertEquals(forthBlock.getStringHash().substring(0, DIFFICULTY), StringUtil.repeat("0", DIFFICULTY));

        // chaining validation
        assertArrayEquals(firstBlock.getPreviousBlockHash(), genesisBlock.getHash());
        assertArrayEquals(secondBlock.getPreviousBlockHash(), firstBlock.getHash());
        assertArrayEquals(thirdBlock.getPreviousBlockHash(), secondBlock.getHash());
        assertArrayEquals(forthBlock.getPreviousBlockHash(), thirdBlock.getHash());
    }

    private void mine(AbstractBlock block) {
        while (!block.valid())
            block.computeHash();
    }

}