package core;

import core.block.AbstractBlock;
import core.block.Block;
import core.block.GenesisBlock;
import core.transaction.AbstractTransaction;
import crypto.KeyUtil;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

public class Blockchain {

    // implement synchronized methods -> allows multiple threads to access shared resource

    // test miner
    static KeyUtil keys = KeyUtil.generate();
    static PrivateKey privateKey = keys.getPrivateKey();
    static PublicKey publicKey = keys.getPublicKey();
    static byte[] minerAddress = publicKey.getEncoded();

    int difficulty = 2;
    Chain chain;

    public Blockchain() {
        chain = new Chain();
    }

    public void loadFullBlock(Block block) {
        chain.add(block.getHeight(), block);
    }

    public void addTransaction(AbstractTransaction tx) throws Exception {
        if (TransactionPool.contains(tx.getHash())) return;
        if (!tx.valid()) {
            throw new Exception("Transaction invalid");
        }
        TransactionPool.add(tx);
    }

    public int getTxPoolSize() {
        return TransactionPool.size();
    }

    public AbstractTransaction getLastTxInPool() {
        return TransactionPool.peek();
    }

    public void addExternalBlock(Block block) {
        chain.add(block);
    }

    public void createBlock(AbstractBlock parent, List<AbstractTransaction> txs) {
        AbstractBlock candidate = new Block(
                parent.getHash(),
                minerAddress,
                difficulty,
                parent.getHeight()+1
        );
        candidate.addTransactions(txs);
        if (!candidate.valid()) {
            System.out.println("Block invalid");
        }
        chain.add(mineBlock(candidate));
        // TODO: add worker queue -> mine ?
        // TODO: if validated block received terminate PoW execution
    }

    public void createGenesisBlock() {
        GenesisBlock genesis = new GenesisBlock(minerAddress, difficulty);
        GenesisBlock mined = (GenesisBlock) mineBlock(genesis);
        chain.add(mined);
    }

    private AbstractBlock mineBlock(AbstractBlock block) {
        // call hashValid() to avoid conflict ?
        while (!block.valid()) {
            block.computeHash();
        }
        return block;
    }

    public Chain getBlockchain() {
        return this.chain;
    }

    public AbstractBlock getBestBlock() {
        return chain.getLast();
    }

}
