package core;

import core.block.Block;
import core.transaction.AbstractTransaction;
import core.transaction.Transaction;
import core.transaction.TxRootIndex;
import db.BlockStore;
import db.TransactionStore;
import net.MessageTypes;
import net.PeerMessage;
import net.PeerNode;
import org.json.JSONObject;

import java.util.ArrayList;

public class StateManager {

    private static Blockchain blockchain = new Blockchain();

    public static void init() {
        // TODO: initialize blockchain (load from storage)
    }

    public static PeerMessage onTransaction(Transaction tx) {
        // TODO: find out if inputs exist
        try {
            blockchain.addTransaction(tx);
        } catch (Exception e) {
            return PeerMessage.errors(tx.getAllExceptions());
        }
        return PeerMessage.ok();
    }

    public static PeerMessage onBlock(Block block) {
        // TODO: find out if block already in chain
        try {
            blockchain.addExternalBlock(block);
        } catch (Exception e) {
            return PeerMessage.errors(block.getAllExceptions());
        }
        return PeerMessage.ok();
    }

    public static PeerMessage onPeerDiscovered(PeerNode node) {
        // TODO: store peer info
        return PeerMessage.ok();
    }

    public static void saveBlock(Block block) {
        TxRootIndex txRootIndex = new TxRootIndex(block.getHash(), block.getTransactions());

        BlockStore.save(block.getHash(), block.toJson());
        TransactionStore.saveTxRootIndex(txRootIndex.getBlockHash(), txRootIndex.toJson());
        for (AbstractTransaction tx : block.getTransactions()) {
            TransactionStore.saveTx(tx.getHash(), tx.toJson());
        }
    }

    public static Block getBlock(byte[] hash) throws Exception {
        Block block = BlockStore.getByHash(hash);
        TxRootIndex txRootIndex = TransactionStore.getTxRoot(hash);
        byte[][] txHashes = txRootIndex.getTxHashes();
        for (byte[] txHash : txHashes) {
            block.addTransaction(TransactionStore.getTransaction(txHash));
        }
        return block;
    }

    public static PeerMessage onStatusReports() {
        ArrayList<StatusReport> reports = new ArrayList<>();

        StatusReport blockchainReport = new StatusReport("Blockchain");
        blockchainReport.add("blockchain_size", blockchain.getBlockchain().getSize());
        if (blockchain.getBestBlock() != null)
            blockchainReport.add("best_block", blockchain.getBestBlock().toJson());

        StatusReport poolReport = new StatusReport("TxPool");
        poolReport.add("pool_size", blockchain.getTxPoolSize());
        if (blockchain.getLastTxInPool() != null)
            poolReport.add("last_tx", blockchain.getLastTxInPool().toJson());

        reports.add(blockchainReport);
        reports.add(poolReport);

        JSONObject reportsObject = new JSONObject();
        reportsObject.put("reports", StatusReport.parseReportsArray(reports));
        return new PeerMessage(MessageTypes.INFO, reportsObject);
    }

}
