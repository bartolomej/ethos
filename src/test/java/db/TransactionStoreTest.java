package db;

import config.Constants;
import core.HelperGenerator;
import core.transaction.*;
import org.junit.Test;
import util.ByteUtil;

import java.io.File;
import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class TransactionStoreTest {

    @Test
    public void storeTxRootIndex() throws Exception {
        HelperGenerator.generateDbFolderStructure();

        byte[] blockHash = new byte[]{0,0,0,0,0,0};
        byte[][] txHashes = new byte[][]{new byte[]{1,1,1,1,1}, new byte[]{2,2,2,2,2}};
        TxRootIndex txRootIndex = new TxRootIndex(blockHash, txHashes);

        TransactionStore.saveTxRootIndex(txRootIndex.getBlockHash(), txRootIndex.toJson());
        TxRootIndex savedRootIndex = TransactionStore.getTxRoot(txRootIndex.getBlockHash());

        String path = Constants.INDEX_STORE_DIR + ByteUtil.toHexString(blockHash) + ".json";

        assertTrue(txRootIndex.equals(savedRootIndex));
        assertTrue(fileExists(path));
        removeFile(path);
    }

    @Test
    public void storeTransaction() throws Exception {
        Transaction tx1 = new HelperGenerator().getTransaction();

        TransactionStore.saveTx(tx1.getHash(), tx1.toJson());
        Transaction storedTx = (Transaction)TransactionStore.getTransaction(tx1.getHash());

        String path = Constants.TX_STORE_DIR + ByteUtil.toHexString(tx1.getHash()) + ".json";

        assertTrue(tx1.equals(storedTx));
        assertTrue(fileExists(path));
        removeFile(path);
    }

    @Test
    public void storeCoinbaseTransaction() throws Exception {
        CoinbaseTransaction tx = new HelperGenerator().getCoinbaseTransaction();

        TransactionStore.saveTx(tx.getHash(), tx.toJson());
        CoinbaseTransaction storedTx = (CoinbaseTransaction)TransactionStore.getTransaction(tx.getHash());

        String path = Constants.TX_STORE_DIR + ByteUtil.toHexString(tx.getHash()) + ".json";

        assertTrue(tx.equals(storedTx));
        assertTrue(fileExists(path));
        removeFile(path);
    }

    @Test
    public void storeTransactionInputs() throws Exception {
        HelperGenerator generator = new HelperGenerator();
        byte[] hash = generator.getTransaction().getHash();
        ArrayList<TxInput> inputs = generator.getInputsTx1();

        TransactionStore.saveInputs(hash, TxInput.arrayToJson(inputs));
        TxInput savedInput = TransactionStore.getTxInput(hash, 0);

        String folderPath = Constants.INPUTS_STORE_DIR + ByteUtil.toHexString(hash);
        String filePath = folderPath + "/0.json";

        assertTrue(fileExists(folderPath));
        assertTrue(fileExists(filePath));
        assertTrue(inputs.get(0).equals(savedInput));

        removeFile(filePath);
        removeFile(folderPath);
    }

    @Test
    public void storeTransactionOutputs() throws Exception {
        HelperGenerator generator = new HelperGenerator();
        byte[] hash = generator.getTransaction().getHash();
        ArrayList<TxOutput> outputs = generator.getOutputsTx1();

        TransactionStore.saveOutputs(hash, TxOutput.arrayToJson(outputs));
        TxOutput savedOutput = TransactionStore.getTxOutput(hash, 0);

        String folderPath = Constants.OUTPUTS_STORE_DIR + ByteUtil.toHexString(hash);
        String filePath = folderPath + "/0.json";

        assertTrue(fileExists(folderPath));
        assertTrue(fileExists(filePath));
        assertTrue(outputs.get(0).equals(savedOutput));

        removeFile(filePath);
        removeFile(folderPath);
    }

    private boolean fileExists(String path) {
        return new File(path).exists();
    }

    private void removeFile(String path) {
        new File(path).delete();
    }

}