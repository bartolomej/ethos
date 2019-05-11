package core;

import crypto.KeyUtil;
import org.junit.Test;
import util.ByteUtil;

import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.Assert.*;

public class BlockTest {

    @Test
    public void firstBlock() {
        int DIFFICULTY = 3;

        // first account
        KeyUtil keys1 = KeyUtil.generate();
        PrivateKey privateKey1 = keys1.getPrivateKey();
        PublicKey publicKey1 = keys1.getPublicKey();

        byte[] address1 = publicKey1.getEncoded();

        // blocks
        GenesisBlock genesisBlock = GenesisBlock.generate();
        mine(genesisBlock);

        Block firstBlock = new Block(genesisBlock.getHash(), address1, 5, 1);
        mine(firstBlock);

        assertEquals(genesisBlock.getStringHash().substring(0, DIFFICULTY), "000");
        assertTrue(genesisBlock.valid());

        assertEquals(firstBlock.getStringHash().substring(0, DIFFICULTY), "000");
        assertTrue(firstBlock.valid());

        assertArrayEquals(firstBlock.getPreviousBlockHash(), genesisBlock.getHash());
    }

    @Test
    public void firstFewBlocksChaining() {
        int DIFFICULTY = 3;

        // first account
        KeyUtil keys1 = KeyUtil.generate();
        PrivateKey privateKey1 = keys1.getPrivateKey();
        PublicKey publicKey1 = keys1.getPublicKey();

        byte[] address1 = publicKey1.getEncoded();

        // blocks
        GenesisBlock genesisBlock = GenesisBlock.generate();
        mine(genesisBlock);

        Block firstBlock = new Block(genesisBlock.getHash(), address1, DIFFICULTY, 1);
        mine(firstBlock);

        Block secondBlock = new Block(firstBlock.getHash(), address1, DIFFICULTY, 2);
        mine(secondBlock);

        Block thirdBlock = new Block(secondBlock.getHash(), address1, DIFFICULTY, 3);
        mine(thirdBlock);

        Block forthBlock = new Block(thirdBlock.getHash(), address1, DIFFICULTY, 4);
        mine(forthBlock);

        // chaining validation
        assertArrayEquals(firstBlock.getPreviousBlockHash(), genesisBlock.getHash());
        assertArrayEquals(secondBlock.getPreviousBlockHash(), firstBlock.getHash());
        assertArrayEquals(thirdBlock.getPreviousBlockHash(), secondBlock.getHash());
        assertArrayEquals(forthBlock.getPreviousBlockHash(), thirdBlock.getHash());
    }

    private void mine(Block block) {
        while (!block.valid())
            block.computeHash();
    }

}