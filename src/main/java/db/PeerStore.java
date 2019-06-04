package db;

import config.Constants;
import net.PeerNode;
import org.json.JSONObject;
import util.ByteUtil;
import util.ObjectParser;

import java.io.File;
import java.util.ArrayList;

public class PeerStore {

    public static void save(byte[] hash, JSONObject node) {
        String filepath = Constants.PEERS_STORE_DIR + ByteUtil.toHexString(hash) + ".json";
        FileSystem.store(filepath, node.toString());
    }

    public static ArrayList<PeerNode> getAllPeers() {
        File directory = new File(Constants.PEERS_STORE_DIR);
        String[] files = directory.list();

        ArrayList<PeerNode> nodes = new ArrayList<>();
        for (String filename : files) {
            String input = FileSystem.read(filename);
            if (input == null) continue;
            JSONObject jsonObject = new JSONObject(input);
            nodes.add(ObjectParser.parseJsonPeer(jsonObject));
        }
        return nodes;
    }
}
