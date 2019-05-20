package net;

import com.sun.net.httpserver.*;
import config.SystemValues;
import errors.NetworkException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;

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
        if (!exchange.getRequestMethod().equals("POST")) invalidHttpMethod(exchange);

        InputStream body = exchange.getRequestBody();

        PeerMessage message = null;
        try {
            JSONObject jsonBody = ParseUtil.parseBody(body);
            message = PeerMessage.decode(jsonBody);
        } catch (JSONException e) {
            invalidDataFormat(exchange, e);
        } catch (NetworkException e) {
            unknownException(exchange, e);
        }

        try {
            PeerMessage response = NetService.onRequest(message);
            sendJsonResponse(exchange, response.encode());
        } catch (Exception e) {
            unknownException(exchange, e);
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

    private static void invalidHttpMethod(HttpExchange exchange) {
        sendJsonResponse(exchange,  MessageGenerators.invalidRequest("Invalid http method"));
    }

    private static void invalidDataFormat(HttpExchange exchange, Exception e) {
        sendJsonResponse(exchange,  MessageGenerators.invalidRequest("Invalid data format", e));
    }

    private static void unknownException(HttpExchange exchange, Exception e) {
        JSONObject eBody = new JSONObject();
        eBody.put("error", ParseUtil.parseException(e));
        sendJsonResponse(exchange, new PeerMessage(MessageTypes.ERROR, eBody).encode());
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
