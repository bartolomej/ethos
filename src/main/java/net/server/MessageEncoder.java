package net.server;

import net.PeerMessage;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;

public class MessageEncoder implements Encoder.Text<PeerMessage> {

    @Override
    public String encode(PeerMessage peerMessage) throws EncodeException {
        return null; // encode message for transmission
    }

}
