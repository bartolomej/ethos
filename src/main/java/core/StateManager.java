package core;

import core.block.AbstractBlock;
import core.block.Block;
import core.transaction.Transaction;
import events.EthosListener;
import net.PeerNode;

public class StateManager extends EthosListener {

    Blockchain blockchain;

    public StateManager() {
        new Blockchain();
    }

    public void onTransaction(Transaction tx) {
        System.out.println("Transaction received");
        blockchain.addTransaction(tx);
    }

    public void onBlock(Block block) {
        System.out.println("Block received");
    }

    public void onPeerDiscovered(PeerNode node) {
        System.out.println("Peer discovered");
        // store peer info
    }

    public void onBlockRejected(AbstractBlock block) {
        System.out.println("Block rejected");
        // propagate to peers ?
    }

}
