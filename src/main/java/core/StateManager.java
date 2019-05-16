package core;

import core.block.AbstractBlock;
import core.block.Block;
import core.transaction.AbstractTransaction;
import core.transaction.Transaction;
import core.transaction.TxRootIndex;
import db.BlockStore;
import db.TransactionStore;
import events.EthosListener;
import net.PeerNode;

import java.util.ArrayList;

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

    public void saveBlock(Block block) {
        TxRootIndex txRootIndex = new TxRootIndex(block.getHash(), block.getTransactions());

        BlockStore.save(block.getHash(), block.toJson());
        TransactionStore.saveTxRootIndex(txRootIndex.getBlockHash(), txRootIndex.toJson());
        saveTransactions(block.getTransactions());

        // save TxInputs / TxOutputs on separate store ?
    }

    public void saveTransactions(ArrayList<AbstractTransaction> transactions) {
        for (AbstractTransaction tx : transactions) {
            TransactionStore.save(tx.getHash(), tx.toJson());
        }
    }

}
