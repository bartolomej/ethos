package core.blockchain;

import core.Chain;
import core.block.Block;
import core.block.GenesisBlock;
import org.junit.Test;

import static org.junit.Assert.*;

public class ChainTest {

    byte[] miner = new byte[]{0,0,0};

    @Test
    public void getLastBlockInChain() {
        Chain chain = new Chain();
        GenesisBlock genesis = GenesisBlock.generate();
        chain.add(genesis);

        assertTrue(chain.getLast().equals(genesis));
        assertTrue(chain.get(0).equals(genesis));
        assertEquals(chain.getSize(), 1);
    }

    @Test
    public void chainWithReferenceParent() {
        GenesisBlock genesis = GenesisBlock.generate();
        Block firstBlock = new Block(genesis.getHash(), new byte[]{0,0,0}, 5, 1);

        Chain chain = new Chain(genesis);
        chain.add(firstBlock);

        assertTrue(chain.getRootParent().equals(genesis));
    }

    @Test
    public void chainWithTwoBlocks() {
        GenesisBlock genesis = GenesisBlock.generate();
        Block firstBlock = new Block(genesis.getHash(), miner, 5, 1);
        Block secondBlock = new Block(firstBlock.getHash(), miner, 5, 2);

        Chain chain = new Chain(genesis);
        chain.add(firstBlock);
        chain.add(secondBlock);

        assertTrue(chain.getLast().equals(secondBlock));
        assertTrue(chain.get(0).equals(firstBlock));
        assertEquals(chain.getSize(), 2);
    }

}