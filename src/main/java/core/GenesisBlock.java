package core;

import core.transaction.Transaction;

public class GenesisBlock extends Block {

    private GenesisBlock(int index, int difficulty, Block previousBlock) {
        super(index, difficulty, previousBlock);
    }

    public static GenesisBlock generate() {
        GenesisBlock genesisBlock = new GenesisBlock(0, 0, null);
        genesisBlock.hash = new byte[]{0,0,0,0,0,0,0};
        return genesisBlock;
    }


}
