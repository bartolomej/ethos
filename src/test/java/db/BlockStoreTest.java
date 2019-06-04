package db;

import config.Constants;
import core.block.Block;
import core.HelperGenerator;
import org.junit.Test;
import util.ByteUtil;

import java.io.File;

import static org.junit.Assert.*;

public class BlockStoreTest {

    @Test
    public void saveBlock() throws Exception {
        Block block = new HelperGenerator().getBlock();

        BlockStore.save(block.getHash(), block.getHeight(), block.toJson());
        Block storedBlock = BlockStore.getByHash(block.getHash());

        String path = Constants.BLOCK_STORE_DIR + storedBlock.getHeight() + "_" + ByteUtil.toHexString(block.getHash()) + ".json";

        assertTrue(storedBlock.equals(block));
        assertTrue(fileExists(path));
        removeFile(path);
    }

    @Test
    public void getBlockByRegex() throws Exception {
        Block block = new HelperGenerator().getBlock();

        BlockStore.save(block.getHash(), block.getHeight(), block.toJson());
        Block storedBlock = BlockStore.getByHeight(block.getHeight());

        String path = Constants.BLOCK_STORE_DIR + storedBlock.getHeight() + "_" + ByteUtil.toHexString(block.getHash()) + ".json";

        assertTrue(storedBlock.equals(block));
        assertTrue(fileExists(path));
        removeFile(path);
    }

    @Test
    public void getBestBlock() throws Exception {
        Block block = new HelperGenerator().getBlock();

        BlockStore.save(block.getHash(), block.getHeight(), block.toJson());
        Block storedBlock = BlockStore.getBest();

        String path = Constants.BLOCK_STORE_DIR + storedBlock.getHeight() + "_" + ByteUtil.toHexString(block.getHash()) + ".json";

        assertTrue(storedBlock.equals(block));
        assertTrue(fileExists(path));
        removeFile(path);
    }

    private boolean fileExists(String path) {
        return new File(path).exists();
    }

    private void removeFile(String path) {
        new File(path).delete();
    }

}