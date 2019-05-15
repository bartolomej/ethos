package events;

import core.block.Block;
import core.StatusReport;
import core.transaction.Transaction;
import net.PeerNode;

import java.util.ArrayList;
import java.util.List;

public class EventEmmiter {

    private static List<EthosListener> listeners = new ArrayList<>();

    public static void addListener(EthosListener listener) {
        listeners.add(listener);
    }

    public static void onTransactionReceived(Transaction tx) {
        for (EthosListener listener : listeners) {
            listener.onTransaction(tx);
        }
    }

    public static void onBlockReceived(Block block) {
        for (EthosListener listener : listeners) {
            listener.onBlock(block);
        }
    }

    public static void onBlockMined(Block block) {
        for (EthosListener listener : listeners) {
            listener.onBlockMined(block);
        }
    }

    public static void onNodeDiscovered(PeerNode node) {
        for (EthosListener listener : listeners) {
            listener.onPeerDiscovered(node);
        }
    }

    public static ArrayList<StatusReport> onStatusReportRequest() {
        ArrayList<StatusReport> reports = new ArrayList<>();
        for (EthosListener listener : listeners) {
            reports.add(listener.onStatusReport());
        }
        return reports;
    }

}
