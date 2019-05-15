import net.server.HTTPServer;

import java.io.IOException;

public class Start {

    public static void main(String[] args) {
        try {
            HTTPServer server = new HTTPServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ON STARTUP
    // check if initialized
    // check if stored state valid
    // sync blockchain

    // FIRST STARTUP
    // folder initialization
    //

}
