import config.Constants;
import core.StateManager;
import events.EventEmmiter;
import net.HTTPServer;

import java.io.File;
import java.io.IOException;

public class Start {

    static StateManager stateManager;
    static HTTPServer httpServer;

    public static void main(String[] args) {
        stateManager = new StateManager();

        EventEmmiter.addListener(stateManager);

        startServer();
    }

    private static void startServer() {
        try {
            httpServer = new HTTPServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initDirs() {
        if (!new File(Constants.DB_DIR).exists()) {
            new File(Constants.DB_DIR).mkdir();
        }
        if (!new File(Constants.ACCOUNT_STORE_DIR).exists()) {
            new File(Constants.ACCOUNT_STORE_DIR).mkdir();
        }
        if (!new File(Constants.BLOCK_STORE_DIR).exists()) {
            new File(Constants.BLOCK_STORE_DIR).mkdir();
        }
        if (!new File(Constants.TX_STORE_DIR).exists()) {
            new File(Constants.TX_STORE_DIR).mkdir();
        }
        if (!new File(Constants.PEERS_STORE_DIR).exists()) {
            new File(Constants.PEERS_STORE_DIR).mkdir();
        }
        if (!new File(Constants.INDEX_STORE_DIR).exists()) {
            new File(Constants.INDEX_STORE_DIR).mkdir();
        }
        if (!new File(Constants.LOG_DIR).exists()) {
            new File(Constants.LOG_DIR).mkdir();
        }
    }

    // ON STARTUP
    // check if stored state valid
    // sync blockchain


}
