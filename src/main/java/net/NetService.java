package net;

import core.StateManager;
import core.block.Block;
import core.transaction.Transaction;
import util.ObjectParser;

public class NetService {

    public static PeerMessage onRequest(PeerMessage message) {
        switch (message.getType()) {
            case INFO: {
                return StateManager.onStatusReport();
            }
            case PING: {
                return new PeerMessage(MessageTypes.PONG, null);
            }
            case SYNC: {
                // TODO: send blocks (use metadata in body)
            }
            case GET_PEERS: {
                return StateManager.getPeers();
            }
            case BLOCK: {
                Block block = ObjectParser.parseJsonBlock(message.getBody());
                return StateManager.onBlock(block);
            }
            case TRANSACTION: {
                Transaction tx = (Transaction)ObjectParser.parseJsonTransaction(message.getBody());
                return StateManager.onTransaction(tx);
            }
            default: {
                return PeerMessage.error(new Exception("Invalid message code"));
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
