package net.client;

import net.HTTPClient;
import net.PeerMessage;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.*;

public class HTTPClientTest {

    @Test
    public void getStaticPeerTest() throws MalformedURLException {
        URL url = new URL("http", "localhost:8500", "/");
        PeerMessage message = PeerMessage.ok();
        HTTPClient.sendMessage(url, message);
    }

}