package net;

public enum MessageTypes {
    PING, // send to confirm that connection is still valid
    PONG, // response to a ping message
    PEERS, // send list of known peers
    GET_PEERS, // requests a list of known peers
    ERROR, // an error occurred on request
    INFO, // send monitoring info
    BLOCK, // block received
    TRANSACTION, // tx received
    SYNC,
    OK // request fulfilled
    // TODO: DISCOVERY -> signals that node is synced and ready (peers add nodes address)
}

/* NETWORK PROTOCOL DESC:
 * - all requests use POST method
 * - all messages use PeerMessage format
 */
