package net.client;

import config.Constants;
import net.PeerNode;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HTTPClient {

    public static PeerNode getStaticPeers() {
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(Constants.STATIC_PEERS))
                .build();
        httpClient.sendAsync(httpRequest, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(System.out::println)
                .join();
        return null;
    }

    public static void postSystemReportToCentralRepo() {};

    public static void postSystemLogsToCentralrepo() {};

}
