package events;

import core.block.Block;
import core.StatusReport;
import core.transaction.Transaction;
import net.PeerNode;

import java.util.ArrayList;

public abstract class EthosListener {

    public ArrayList<StatusReport> onStatusReports() { return null; }

    public void onPeerDiscovered(PeerNode node) {};
    public void onSyncUpdate() {};

    public void onTransaction(Transaction tx) {};
    public void onBlock(Block block) {};

    public void onBlockRejected(Block block) {}; // implement error report class ?
    public void onBlockMined(Block block) {};

}
