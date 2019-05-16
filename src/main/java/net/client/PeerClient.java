package net.client;

import net.PeerMessage;
import net.server.MessageEncoder;

import javax.websocket.*;

@ClientEndpoint(
        encoders = MessageEncoder.class
)
public class PeerClient {

    // https://www.baeldung.com/java-websockets

    @OnOpen
    public void onOpenConnection(Session session) {
        System.out.println("Opening connection");
    }

    @OnClose
    public void onCloseConnection(Session session, CloseReason reason) {
        System.out.println("Closing connection");
    }

    @OnMessage
    public void onMessage(PeerMessage message) {
        // handle message || emmit event to listeners
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
    }
}
