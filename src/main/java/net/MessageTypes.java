package net;

public enum MessageTypes {
    PING, // send to confirm that connection is still valid
    PONG, // response to a ping message
    PEERS, // send list of known peers
    GET_PEERS, // requests a list of known peers
    DISCONNECT,
    ERROR,
    BLOCK, // block received
    TRANSACTION, // tx received
    SYNC
}
