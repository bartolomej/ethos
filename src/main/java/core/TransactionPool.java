package core;

import core.transaction.Transaction;

import java.util.ArrayList;

public class TransactionPool {

    private ArrayList<Transaction> transactionQueue = new ArrayList<>();

    public int size() {
        return transactionQueue.size();
    }

    public Transaction peek() {
        return transactionQueue.get(transactionQueue.size()-1);
    }

    public void add(Transaction transaction) {
        transactionQueue.add(0, transaction);
    }

    /*
     * Returns and removes last element in queue
     * Returns null if queue empty */
    public Transaction poll() {
        if (transactionQueue.isEmpty()) return null;
        Transaction transaction = this.peek();
        transactionQueue.remove(transactionQueue.size()-1);
        return transaction;
    }

    public boolean isEmpty() {
        return transactionQueue.isEmpty();
    }

    public boolean contains(Transaction transaction) {
        for (Transaction tr : transactionQueue) {
            if (tr.equals(transaction)) return true;
        }
        return false;
    }

}
