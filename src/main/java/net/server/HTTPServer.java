package net.server;

import com.sun.net.httpserver.*;
import config.SystemConfig;
import config.SystemValues;
import core.StatusReport;
import events.EventEmmiter;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.util.ArrayList;

public class HTTPServer {

    HttpServer server;
    HttpContext context;

    public HTTPServer() throws IOException {
        init();
    }

    private void init() throws IOException {
        server = HttpServer.create(new InetSocketAddress(SystemValues.HTTP_PORT), 0);
        context = server.createContext("/example");
        context.setHandler(HTTPServer::handleRequest);
        server.start();
    }

    private static void handleRequest(HttpExchange exchange) {
        URI requestURI = exchange.getRequestURI();
        printRequestInfo(exchange);
        ArrayList<StatusReport> reports = EventEmmiter.onStatusReportRequest();

        try {
            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
            exchange.sendResponseHeaders(200, StatusReport.parseReportsArray(reports).toString().getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(StatusReport.parseReportsArray(reports).toString().getBytes());
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printRequestInfo(HttpExchange exchange) {
        System.out.println("-- headers --");
        Headers requestHeaders = exchange.getRequestHeaders();
        requestHeaders.entrySet().forEach(System.out::println);

        System.out.println("-- principle --");
        HttpPrincipal principal = exchange.getPrincipal();
        System.out.println(principal);

        System.out.println("-- HTTP method --");
        String requestMethod = exchange.getRequestMethod();
        System.out.println(requestMethod);

        System.out.println("-- query --");
        URI requestURI = exchange.getRequestURI();
        String query = requestURI.getQuery();
        System.out.println(query);
    }
}
