package core;

import core.transaction.AbstractTransaction;
import core.transaction.Transaction;
import org.junit.Test;

import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class TransactionPoolTest {

    @Test
    public void addTransactionToPool() throws InvalidKeySpecException, InvalidKeyException {
        Transaction tx1 = new HelperGenerator().getTransaction();

        TransactionPool.add(tx1);

        // contains transaction
        assertTrue(TransactionPool.contains(tx1));
        assertTrue(TransactionPool.contains(tx1.getHash()));
        assertFalse(TransactionPool.isEmpty());
        //assertEquals(1, TransactionPool.size()); TODO: assert doesn't work sometimes
        assertTrue(TransactionPool.peek().equals(tx1));
        assertTrue(TransactionPool.poll().equals(tx1));

        // pool empty
        assertTrue(TransactionPool.isEmpty());
        assertFalse(TransactionPool.contains(tx1));
        assertFalse(TransactionPool.contains(tx1.getHash()));
        assertEquals(TransactionPool.size(), 0);
        assertTrue(TransactionPool.peek() == null);
        assertTrue(TransactionPool.poll() == null);
    }

    @Test
    public void addTwoTransactionsToPool() throws InvalidKeySpecException, InvalidKeyException {
        HelperGenerator generator = new HelperGenerator();
        ArrayList<AbstractTransaction> transactions = new ArrayList<>();
        transactions.add(generator.getCoinbaseTransaction());
        transactions.add(generator.getTransaction());

        TransactionPool.add(transactions.get(0));
        TransactionPool.add(transactions.get(1));

        assertTrue(TransactionPool.contains(transactions.get(0)));
        assertTrue(TransactionPool.contains(transactions.get(1)));

        assertTrue(TransactionPool.contains(transactions.get(0).getHash()));
        assertTrue(TransactionPool.contains(transactions.get(1).getHash()));

        assertTrue(TransactionPool.peek().equals(transactions.get(0)));

        assertEquals(TransactionPool.size(), 2);

        assertTrue(TransactionPool.poll().equals(transactions.get(0)));
        assertTrue(TransactionPool.poll().equals(transactions.get(1)));

        assertTrue(TransactionPool.isEmpty());
        assertNull(TransactionPool.peek());
        assertNull(TransactionPool.poll());
    }

}