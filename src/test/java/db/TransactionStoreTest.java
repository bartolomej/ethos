package db;

import config.Constants;
import core.HelperGenerator;
import core.transaction.Transaction;
import core.transaction.TxRootIndex;
import org.junit.Test;
import util.ByteUtil;

import java.io.File;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;

import static org.junit.Assert.*;

public class TransactionStoreTest {

    @Test
    public void storeTxRootIndex() {
        HelperGenerator.generateDbFolderStructure();

        byte[] blockHash = new byte[]{0,0,0,0,0,0};
        byte[][] txHashes = new byte[][]{new byte[]{1,1,1,1,1}, new byte[]{2,2,2,2,2}};
        TxRootIndex txRootIndex = new TxRootIndex(blockHash, txHashes);

        TransactionStore.saveTxRootIndex(txRootIndex.getBlockHash(), txRootIndex.toJson());

        String path = Constants.INDEX_STORE_DIR + ByteUtil.toHexString(txRootIndex.getBlockHash()) + ".json";

        assertTrue(fileExists(path));
        removeFile(path);
    }

    @Test
    public void getTxRootIndex() {
        byte[] blockHash = new byte[]{0,0,0,0,0,0};
        byte[][] txHashes = new byte[][]{new byte[]{1,1,1,1,1}, new byte[]{2,2,2,2,2}};
        TxRootIndex txRootIndex = new TxRootIndex(blockHash, txHashes);

        TransactionStore.saveTxRootIndex(txRootIndex.getBlockHash(), txRootIndex.toJson());
        TxRootIndex actualStoredTxRoot = TransactionStore.getTxRoot(blockHash);

        String path = Constants.INDEX_STORE_DIR + ByteUtil.toHexString(txRootIndex.getBlockHash()) + ".json";

        assertTrue(txRootIndex.equals(actualStoredTxRoot));
        assertTrue(fileExists(path));
        removeFile(path);
    }

    @Test
    public void storeTransaction() throws InvalidKeySpecException, InvalidKeyException {
        Transaction tx1 = HelperGenerator.generateTestValidTransaction();
        TransactionStore.save(tx1.getHash(), tx1.toJson());

        String path = Constants.TX_STORE_DIR + ByteUtil.toHexString(tx1.getHash()) + ".json";

        assertTrue(fileExists(path));
        removeFile(path);
    }

    @Test
    public void getTransaction() throws InvalidKeySpecException, InvalidKeyException {
        Transaction tx1 = HelperGenerator.generateTestValidTransaction();

        TransactionStore.save(tx1.getHash(), tx1.toJson());
        Transaction storedTx = TransactionStore.getTransaction(tx1.getHash());

        String path = Constants.TX_STORE_DIR + ByteUtil.toHexString(tx1.getHash()) + ".json";

        assertTrue(tx1.equals(storedTx));
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