package db;

import config.Constants;
import core.block.Block;
import org.json.JSONObject;
import util.ByteUtil;
import util.ObjectParser;

import java.io.File;

public class BlockStore {

    public static void save(byte[] hash, JSONObject block) {
        String filepath = Constants.BLOCK_STORE_DIR + ByteUtil.toHexString(hash) + ".json";
        FileSystemStore.store(filepath, block.toString());
    }

    public static Block getByHash(byte[] hash) throws Exception {
        String filepath = Constants.BLOCK_STORE_DIR + ByteUtil.toHexString(hash) + ".json";
        String fileInput = FileSystemStore.read(filepath);
        if (fileInput == null) throw new Exception("Block not found: " + filepath);
        JSONObject jsonBlock = new JSONObject(fileInput);
        return ObjectParser.parseJsonBlock(jsonBlock);
    }

}
