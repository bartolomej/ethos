package core;

import config.Constants;
import core.block.Block;
import core.transaction.Transaction;
import org.junit.Test;
import util.ByteUtil;

import java.io.File;

import static org.junit.Assert.*;

public class StateManagerTest {

    @Test
    public void storeWholeBlock() throws Exception {
        HelperGenerator generator = new HelperGenerator();

        Transaction tx1 = generator.getTransaction();
        Block block = generator.getBlock();

        StateManager.saveBlock(block);

        String blockPath = Constants.BLOCK_STORE_DIR + block.getHeight() + "_" + ByteUtil.toHexString(block.getHash()) + ".json";
        String tx1Path = Constants.TX_STORE_DIR + ByteUtil.toHexString(tx1.getHash()) + ".json";
        String txRootPath = Constants.INDEX_STORE_DIR + ByteUtil.toHexString(block.getTransactionsRootHash()) + ".json";

        assertTrue(fileExists(blockPath));
        assertTrue(fileExists(tx1Path));
        assertTrue(fileExists(txRootPath));

        removeFile(blockPath);
        removeFile(tx1Path);
        removeFile(txRootPath);
    }

    private void removeFile(String path) {
        new File(path).delete();
    }

    private boolean fileExists(String path) {
        return new File(path).exists();
    }
}