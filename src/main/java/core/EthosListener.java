package core;

import core.transaction.Transaction;
import net.PeerNode;

public interface EthosListener {

    /* EXAMPLES:
     * - onSyncCompleted()
     * - onPeerConnected();
     * - onNetError();
     *   ...
     */

    StatusReport onStatusReport(); // calls many system modules for status report

    void onNodeDiscovered(PeerNode node);
    void onSyncUpdate();

    void onTransaction(Transaction tx);
    void onBlock(Block block);

    void onBlockRejected(Block block); // implement error report class ?
    void onBlockMined();

}
