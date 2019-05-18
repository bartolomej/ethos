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
    public void saveBlock() {
        Block block = HelperGenerator.generateTestEmptyBlock();

        BlockStore.save(block.getHash(), block.toJson());
        Block storedBlock = BlockStore.getByHash(block.getHash());

        String path = Constants.BLOCK_STORE_DIR + ByteUtil.toHexString(block.getHash()) + ".json";

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