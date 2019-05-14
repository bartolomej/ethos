package core;

import core.transaction.Transaction;
import util.ByteUtil;

import java.util.ArrayList;

public class TransactionPool {

    private static ArrayList<Transaction> transactionQueue = new ArrayList<>();

    public static int size() {
        return transactionQueue.size();
    }

    public static Transaction peek() {
        if (transactionQueue.isEmpty()) return null;
        return transactionQueue.get(0);
    }

    public static void add(Transaction transaction) {
        transactionQueue.add(transaction);
    }

    /*
     * Returns and removes last element in queue
     * Returns null if queue empty */
    public static Transaction poll() {
        if (transactionQueue.isEmpty()) return null;
        return transactionQueue.remove(0);
    }

    public static boolean isEmpty() {
        return transactionQueue.isEmpty();
    }

    public static boolean contains(Transaction transaction) {
        for (Transaction tx : transactionQueue) {
            if (tx.equals(transaction))
                return true;
        }
        return false;
    }

    public static boolean contains(byte[] hash) {
        for (Transaction tx : transactionQueue) {
            if (ByteUtil.arraysEqual(tx.getHash(), hash))
                return true;
        }
        return false;
    }


}
