package core;

import core.transaction.Transaction;

import java.util.List;

public class Blockchain {

    // implement synchronized methods -> allows multiple threads to access shared resource
    // call methods from

    static int difficulty = 5;
    static byte[] testMiner = new byte[]{1,1,1,1,1};

    public static void addTransaction(Transaction tx) {
        if (tx.valid()) {
            TransactionPool.add(tx);
        } else {
            System.out.println("Transaction invalid");
        }
    }

    public static void createBlock(Block parent, List<Transaction> txs) {
        Block candidate = new Block(
                parent.getHash(),
                testMiner,
                difficulty,
                parent.getIndex()+1
        );
        candidate.addTransactions(txs);
        if (!candidate.valid()) {
            System.out.println("Block invalid");
        }
        Block validated = mineBlock(candidate);
        // add to worker queue -> mine ?
        // if validated block received terminate PoW execution
    }

    private static Block mineBlock(Block block) {
        // call hashValid() to avoid conflict ?
        while (!block.valid()) {
            block.computeHash();
        }
        return block;
    }

    public static Block getBestBlock() {
        return null;
    }
}
