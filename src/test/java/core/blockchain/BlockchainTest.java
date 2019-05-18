package core.blockchain;

import core.Blockchain;
import core.HelperGenerator;
import core.TransactionPool;
import core.transaction.AbstractTransaction;
import core.transaction.Transaction;
import org.junit.Test;

import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class BlockchainTest {

    @Test
    public void addTransactionsInOneBlock() throws InvalidKeySpecException, InvalidKeyException {
        ArrayList<AbstractTransaction> tx = HelperGenerator.generateTwoTransactionArrayWithGenesis();

        Blockchain blockchain = new Blockchain();
        blockchain.addTransaction(tx.get(0));
        blockchain.addTransaction(tx.get(1));

        assertTrue(TransactionPool.peek().equals(tx.get(0)));

        blockchain.createGenesisBlock();
        blockchain.createBlock(blockchain.getBestBlock(), TransactionPool.peekAll());

        assertEquals(2, blockchain.getBlockchain().getSize());
    }

    @Test
    public void addTransactionsInTwoBlocks() throws InvalidKeySpecException, InvalidKeyException {
        ArrayList<AbstractTransaction> tx = HelperGenerator.generateTwoTransactionArrayWithGenesis();

        Blockchain blockchain = new Blockchain();

        // first transactions
        blockchain.addTransaction(tx.get(0));
        blockchain.addTransaction(tx.get(1));

        assertTrue(TransactionPool.peek().equals(tx.get(0)));

        // first block
        blockchain.createGenesisBlock();
        blockchain.createBlock(blockchain.getBestBlock(), TransactionPool.pollAll());

        assertEquals(2, blockchain.getBlockchain().getSize());

        // second transactions
        blockchain.addTransaction(tx.get(0));
        blockchain.addTransaction(tx.get(1));

        assertTrue(TransactionPool.peek().equals(tx.get(0)));

        // second block
        blockchain.createBlock(blockchain.getBestBlock(), TransactionPool.pollAll());

        assertEquals(3, blockchain.getBlockchain().getSize());
    }
}