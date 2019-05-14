package core;

import core.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

public class EventEmmiter {

    private static List<EthosListener> listeners = new ArrayList<>();

    public void addListener(EthosListener listener) {
        listeners.add(listener);
    }

    public void onTransactionReceived(Transaction tx) {
        for (EthosListener listener : listeners) {
            listener.onTransaction(tx);
        }
    }
}
