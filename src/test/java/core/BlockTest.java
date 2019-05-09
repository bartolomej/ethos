package core;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class BlockTest {

    @Test
    public void invalidBlockValidation() {
        GenesisBlock genesisBlock = GenesisBlock.generate();
        Block secondBlock = new Block(1, 5, genesisBlock);
        secondBlock.computeHash();
        assertTrue(!secondBlock.valid());
    }

    @Test
    public void addTransactions() {
    }

    @Test
    public void addTimestamp() {
    }

    @Test
    public void computeHash() {
    }

    @Test
    public void toStringTest() {
    }
}