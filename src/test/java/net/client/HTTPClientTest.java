package net.client;

import org.junit.Test;

import static org.junit.Assert.*;

public class HTTPClientTest {

    @Test
    public void getStaticPeerTest() {
        String peers = HTTPClient.getStaticPeers();

        assertEquals(peers, "");
    }

}