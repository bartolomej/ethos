import config.Constants;
import core.StateManager;
import db.DbFacade;
import events.EventEmmiter;
import net.HTTPServer;

import java.io.File;
import java.io.IOException;

public class Start {

    static StateManager stateManager;
    static HTTPServer httpServer;

    public static void main(String[] args) {
        stateManager = new StateManager();

        startServer();
        DbFacade.init();
    }

    private static void startServer() {
        try {
            httpServer = new HTTPServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
