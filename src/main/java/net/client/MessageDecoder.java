package net.client;

import net.PeerMessage;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;

public class MessageDecoder implements Decoder.Text<PeerMessage> {

    @Override
    public PeerMessage decode(String s) throws DecodeException {
        return null; // decode json message from transmission
    }

    @Override
    public boolean willDecode(String s) {
        return s != null;
    }

}
