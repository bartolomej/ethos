package db;

import config.Global;
import core.HelperGenerator;
import core.transaction.Transaction;
import org.junit.Test;
import util.ByteUtil;

import java.io.File;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;

import static org.junit.Assert.*;

public class TransactionStoreTest {

    @Test
    public void storeTransaction() throws InvalidKeySpecException, InvalidKeyException {
        Transaction tx1 = HelperGenerator.generateTestValidTransaction();
        TransactionStore.save(tx1.getHash(), tx1.toJson());

        String path = ByteUtil.toHexString(tx1.getHash()) + ".json";

        assertTrue(fileExists(path));
        removeFile(path);
    }

    @Test
    public void getTransaction() throws InvalidKeySpecException, InvalidKeyException {
        Transaction tx1 = HelperGenerator.generateTestValidTransaction();

        TransactionStore.save(tx1.getHash(), tx1.toJson());
        Transaction storedTx = TransactionStore.getByHash(tx1.getHash());

        String path = "/" + ByteUtil.toHexString(tx1.getHash()) + ".json";

        assertTrue(tx1.equals(storedTx));
        assertTrue(fileExists(path));
        removeFile(path);
    }

    private boolean fileExists(String path) {
        path = Global.ROOT_DIR + "/" + path;
        return new File(path).exists();
    }

    private void removeFile(String path) {
        new File(path).delete();
    }

}