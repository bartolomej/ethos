package net;

import com.sun.net.httpserver.*;
import config.SystemValues;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

public class HTTPServer {

    HttpServer server;
    HttpContext context;

    public HTTPServer() throws IOException {
        init();
    }

    private void init() throws IOException {
        server = HttpServer.create(new InetSocketAddress(SystemValues.HTTP_PORT), 0);
        context = server.createContext("/");
        context.setHandler(HTTPServer::handleRequest);
        server.start();
    }

    private static void handleRequest(HttpExchange exchange) {
        logRequest(exchange);
        if (!exchange.getRequestMethod().equals("POST"))
            sendJsonResponse(exchange,  invalidRequest("Invalid http method"));

        InputStream body = exchange.getRequestBody();

        PeerMessage message = null;
        try {
            JSONObject jsonBody = ParseUtil.parseBody(body);
            message = PeerMessage.decode(jsonBody);
        } catch (JSONException e) {
            sendJsonResponse(exchange,  invalidRequest("Invalid data format", e));
        } catch (Exception e) {
            sendJsonResponse(exchange, PeerMessage.error(e).encode());
        }

        try {
            PeerMessage response = NetService.onRequest(message);
            sendJsonResponse(exchange, response.encode());
        } catch (Exception e) {
            sendJsonResponse(exchange, PeerMessage.error(e).encode());
        }
    }

    private static void sendJsonResponse(HttpExchange exchange, byte[] content) {
        try {
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(200, content.length);
            OutputStream os = exchange.getResponseBody();
            os.write(content);
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static byte[] invalidRequest(String message) {
        Exception exception = new Exception(message);
        JSONObject parsedException = ParseUtil.parseException(exception);
        return new PeerMessage(MessageTypes.ERROR, parsedException).encode();
    }

    public static byte[] invalidRequest(String message, Exception e) {
        JSONObject parsedException = ParseUtil.parseException(e);
        return new PeerMessage(MessageTypes.ERROR, message, parsedException).encode();
    }

    public static String getLocalIp() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    private static void logRequest(HttpExchange exchange) {
        URI URI = exchange.getRequestURI();
        String METHOD = exchange.getRequestMethod();
        String ADDRESS = exchange.getRemoteAddress().toString();

        System.out.println("URI: " + URI.toString());
        System.out.println("METHOD: " + METHOD);
        System.out.println("ADDRESS: " + ADDRESS);
    }

}
