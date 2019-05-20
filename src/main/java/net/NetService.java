package net;

import core.StateManager;
import core.StatusReport;
import core.block.Block;
import core.transaction.AbstractTransaction;
import core.transaction.Transaction;
import events.EthosListener;
import events.EventEmmiter;
import org.json.JSONObject;
import util.ObjectParser;

import java.util.ArrayList;

public class NetService extends EthosListener {

    public static PeerMessage onRequest(PeerMessage message) {
        switch (message.getType()) {
            case INFO: {
                ArrayList<StatusReport> reports = EventEmmiter.onStatusReportRequest(); // TODO: remove ?
                JSONObject reportsObject = new JSONObject();
                reportsObject.put("reports", StatusReport.parseReportsArray(reports));
                return new PeerMessage(MessageTypes.INFO, reportsObject);
            }
            case PING: {
                return new PeerMessage(MessageTypes.PONG, null);
            }
            case SYNC: {
                // TODO: send blocks (use metadata in body)
            }
            case GET_PEERS: {
                // TODO: send peers
            }
            case BLOCK: {
                Block block = ObjectParser.parseJsonBlock(message.getBody());
                // EventEmmiter.onBlockReceived(block); // TODO: remove ?
                return new PeerMessage(MessageTypes.OK, null);
            }
            case TRANSACTION: {
                Transaction tx = ObjectParser.parseJsonTransaction(message.getBody());
                // EventEmmiter.onTransactionReceived(tx); // TODO: remove ?
                return new PeerMessage(MessageTypes.OK, null);
            }
            default: {
                Exception exception = new Exception("Invalid message code");
                return new PeerMessage(MessageTypes.ERROR, ParseUtil.parseException(exception));
            }
        }
    }

    public static PeerMessage broadcastBlock(Block block) {
        return null;
    }

    public static PeerMessage broadcastTx(Transaction transaction) {
        return null;
    }

}
