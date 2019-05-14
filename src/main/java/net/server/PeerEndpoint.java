package net.server;

import net.PeerMessage;
import net.client.MessageDecoder;

import javax.websocket.*;
import javax.websocket.server.*;
import java.io.IOException;

@ServerEndpoint(
        value="/",
        decoders = MessageDecoder.class,
        encoders = MessageEncoder.class
)
public class PeerEndpoint {

    @OnOpen
    public void onOpen(Session session) throws IOException {
        // Get session and WebSocket connection
    }

    @OnMessage
    public void onMessage(Session session, PeerMessage message) throws IOException {
        // Handle new messages
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        // WebSocket connection closes
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }

}
