package events;

import core.block.Block;
import core.StatusReport;
import core.transaction.Transaction;
import net.PeerNode;

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

    public void onBlockReceived(Block block) {
        for (EthosListener listener : listeners) {
            listener.onBlock(block);
        }
    }

    public void onBlockMined(Block block) {
        for (EthosListener listener : listeners) {
            listener.onBlockMined(block);
        }
    }

    public void onNodeDiscovered(PeerNode node) {
        for (EthosListener listener : listeners) {
            listener.onNodeDiscovered(node);
        }
    }

    public ArrayList<StatusReport> onStatusReportRequest() {
        ArrayList<StatusReport> reports = new ArrayList<>();
        for (EthosListener listener : listeners) {
            reports.add(listener.onStatusReport());
        }
        return reports;
    }


}
