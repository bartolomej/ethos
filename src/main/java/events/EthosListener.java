package events;

import core.block.Block;
import core.StatusReport;
import core.transaction.Transaction;
import net.PeerNode;

public abstract class EthosListener {

    StatusReport onStatusReport() {
        return null;
    }; // calls many system modules for status report

    void onNodeDiscovered(PeerNode node) {};
    void onSyncUpdate() {};

    void onTransaction(Transaction tx) {};
    void onBlock(Block block) {};

    void onBlockRejected(Block block) {}; // implement error report class ?
    void onBlockMined(Block block) {};
}
