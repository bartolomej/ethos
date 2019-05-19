package events;

import core.block.Block;
import core.StatusReport;
import core.transaction.Transaction;
import net.PeerNode;

public abstract class EthosListener {

    public StatusReport onStatusReport() { return null; };

    public void onPeerDiscovered(PeerNode node) {};
    public void onSyncUpdate() {};

    public void onTransaction(Transaction tx) {};
    public void onBlock(Block block) {};

    public void onBlockRejected(Block block) {}; // implement error report class ?
    public void onBlockMined(Block block) {};

}
