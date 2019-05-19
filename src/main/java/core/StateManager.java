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

    private Blockchain blockchain;

    public StateManager() {
        blockchain = new Blockchain();
    }

    public void onTransaction(Transaction tx) {
        // check if transaction already received
        System.out.println("Transaction received");
        blockchain.addTransaction(tx);
    }

    public void onBlock(Block block) {
        // check if block already received
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

    public static void saveBlock(Block block) {
        TxRootIndex txRootIndex = new TxRootIndex(block.getHash(), block.getTransactions());

        BlockStore.save(block.getHash(), block.toJson());
        TransactionStore.saveTxRootIndex(txRootIndex.getBlockHash(), txRootIndex.toJson());
        for (AbstractTransaction tx : block.getTransactions()) {
            TransactionStore.save(tx.getHash(), tx.toJson());
        }
    }

    public static Block getBlock(byte[] hash) {
        Block block = BlockStore.getByHash(hash);
        TxRootIndex txRootIndex = TransactionStore.getTxRoot(hash);
        byte[][] txHashes = txRootIndex.getTxHashes();
        for (byte[] txHash : txHashes) {
            block.addTransaction(TransactionStore.getTransaction(txHash));
        }
        return block;
    }

    public static ArrayList<PeerNode> getPeers() {
        return null;
    }

    public StatusReport onStatusReport() {
        StatusReport report = new StatusReport("StateManager");
        report.add("blockchain_size", blockchain.getBlockchain().getSize());
        if (blockchain.getBestBlock() != null)
            report.add("best_block", blockchain.getBestBlock().toJson());
        return report;
    }

}
