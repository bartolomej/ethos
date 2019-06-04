package db;

import config.Constants;
import core.block.Block;
import org.json.JSONObject;
import util.ByteUtil;
import util.ObjectParser;

import java.io.File;

public class BlockStore {

    public static void save(byte[] hash, int height, JSONObject block) {
        String filepath = Constants.BLOCK_STORE_DIR + height + "_" + ByteUtil.toHexString(hash) + ".json";
        FileSystem.store(filepath, block.toString());
    }

    public static Block getByHash(byte[] hash) throws Exception {
        String filepathRegex = "[0-9]+_" + ByteUtil.toHexString(hash) + ".json";
        String fileInput = FileSystem.read(Constants.BLOCK_STORE_DIR, filepathRegex);
        if (fileInput == null) throw new Exception("Block not found for regex: " + filepathRegex);
        JSONObject jsonBlock = new JSONObject(fileInput);
        return ObjectParser.parseJsonBlock(jsonBlock);
    }

    public static Block getByHeight(int height) throws Exception {
        String regex = height + "_.+";
        String fileInput = FileSystem.read(Constants.BLOCK_STORE_DIR, regex);
        if (fileInput == null) throw new Exception("Block with height " + height + " not found");
        JSONObject jsonBlock = new JSONObject(fileInput);
        return ObjectParser.parseJsonBlock(jsonBlock);
    }

    public static Block getBest() throws Exception {
        File blockFolder = new File(Constants.BLOCK_STORE_DIR);
        String[] blocks = blockFolder.list();
        if (blocks == null || blocks.length == 0)
            throw new Exception("No blocks found");
        int bestHeight = 0;
        for (String blockIndex : blocks) {
            int height = Integer.parseInt(blockIndex.split("_")[0]);
            if (height > bestHeight) bestHeight = height;
        }
        return getByHeight(bestHeight);
    }

}
