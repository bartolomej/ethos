package db;

import config.Constants;
import core.block.Block;
import org.json.JSONObject;
import util.ByteUtil;
import util.ObjectParser;

public class BlockStore {

    // store merkle tree in index file and locate transactions based on index

    public static void save(byte[] hash, JSONObject block) {
        String filepath = Constants.BLOCK_STORE_DIR + ByteUtil.toHexString(hash) + ".json";
        FileSystemStore.store(filepath, block.toString());
    }

    public static Block read(byte[] hash) {
        String filepath = Constants.BLOCK_STORE_DIR + ByteUtil.toHexString(hash) + ".json";
        JSONObject jsonBlock = new JSONObject(FileSystemStore.read(filepath));
        return ObjectParser.parseJsonBlock(jsonBlock);
    }

}
