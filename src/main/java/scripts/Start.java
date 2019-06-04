package scripts;

import com.sun.net.httpserver.HttpServer;
import config.SystemValues;
import core.StateManager;
import db.DbFacade;
import net.HTTPServer;

import java.io.IOException;
import java.net.UnknownHostException;

public class Start {

    static StateManager stateManager;
    static HTTPServer httpServer;

    public static void main(String[] args) {
        stateManager = new StateManager();

        startServer();
        DbFacade.init();
        initState();
    }

    private static void startServer() {
        try {
            httpServer = new HTTPServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initState() {
        try {
            SystemValues.NODE_ADDRESS = HTTPServer.getLocalIp();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

}
