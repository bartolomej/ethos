package core;

import core.transaction.AbstractTransaction;
import core.transaction.Transaction;
import util.ByteUtil;

import java.util.ArrayList;

public class TransactionPool {

    private static ArrayList<AbstractTransaction> transactionQueue = new ArrayList<>();

    public static int size() {
        return transactionQueue.size();
    }

    public static AbstractTransaction peek() {
        if (transactionQueue.isEmpty()) return null;
        return transactionQueue.get(0);
    }

    public static ArrayList<AbstractTransaction> peekAll() {
        return transactionQueue;
    }

    public static ArrayList<AbstractTransaction> pollAll() {
        ArrayList<AbstractTransaction> txs = transactionQueue;
        transactionQueue.clear();
        return txs;
    }

    public static void add(AbstractTransaction transaction) {
        transactionQueue.add(transaction);
    }

    /*
     * Returns and removes last element in queue
     * Returns null if queue empty */
    public static AbstractTransaction poll() {
        if (transactionQueue.isEmpty()) return null;
        return transactionQueue.remove(0);
    }

    public static boolean isEmpty() {
        return transactionQueue.isEmpty();
    }

    public static boolean contains(AbstractTransaction transaction) {
        for (AbstractTransaction tx : transactionQueue) {
            if (tx.equals(transaction))
                return true;
        }
        return false;
    }

    public static boolean contains(byte[] hash) {
        for (AbstractTransaction tx : transactionQueue) {
            if (ByteUtil.arraysEqual(tx.getHash(), hash))
                return true;
        }
        return false;
    }


}
