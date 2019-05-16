package core;

import config.Constants;
import core.block.Block;
import core.transaction.*;
import crypto.KeyUtil;
import org.junit.Test;

import java.io.File;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class HelperGenerator {

    // first account
    static KeyUtil keys = KeyUtil.generate();
    static PrivateKey privateKey = keys.getPrivateKey();
    static PublicKey publicKey = keys.getPublicKey();
    static byte[] address = publicKey.getEncoded();

    public static long OUTPUT_VALUE = 10;
    public static long INPUT_VALUE = 100;

    public static void generateDbFolderStructure() {
        if (!new File(Constants.DB_DIR).exists()) {
            new File(Constants.DB_DIR).mkdir();
        }
        if (!new File(Constants.ACCOUNT_STORE_DIR).exists()) {
            new File(Constants.ACCOUNT_STORE_DIR).mkdir();
        }
        if (!new File(Constants.BLOCK_STORE_DIR).exists()) {
            new File(Constants.BLOCK_STORE_DIR).mkdir();
        }
        if (!new File(Constants.TX_STORE_DIR).exists()) {
            new File(Constants.TX_STORE_DIR).mkdir();
        }
        if (!new File(Constants.PEERS_STORE_DIR).exists()) {
            new File(Constants.PEERS_STORE_DIR).mkdir();
        }
        if (!new File(Constants.INDEX_STORE_DIR).exists()) {
            new File(Constants.INDEX_STORE_DIR).mkdir();
        }
        if (!new File(Constants.LOG_DIR).exists()) {
            new File(Constants.LOG_DIR).mkdir();
        }
    }

    public static Transaction generateTestValidTransaction() throws InvalidKeySpecException, InvalidKeyException {

        // coinbase transaction
        CoinbaseTransaction coinbaseTx = CoinbaseTransaction.generate(HelperGenerator.address);

        // input-outputs for tx1
        ArrayList<TxInput> inputsTx1 = new ArrayList<>();
        inputsTx1.add(new TxInput(coinbaseTx.getOutput()));
        ArrayList<TxOutput> outputsTx1 = new ArrayList<>();
        outputsTx1.add(new TxOutput(HelperGenerator.address, HelperGenerator.OUTPUT_VALUE, 0)); // sending some to other address
        outputsTx1.add(new TxOutput(HelperGenerator.address, HelperGenerator.OUTPUT_VALUE, 1)); // sending rest back to itself -> include fee

        // first transaction
        Transaction tx1 = new Transaction(inputsTx1, outputsTx1, HelperGenerator.publicKey);
        try {
            tx1.sign(HelperGenerator.privateKey);
        } catch (InvalidKeyException e) {
            assertNull(e);
        }
        return tx1;
    }

    public static ArrayList<AbstractTransaction> generateTwoTransactionArrayWithGenesis() throws InvalidKeySpecException, InvalidKeyException {
        ArrayList<AbstractTransaction> transactions = new ArrayList<>();

        // coinbase transaction
        CoinbaseTransaction coinbaseTx = CoinbaseTransaction.generate(HelperGenerator.address);

        // input-outputs for tx1
        ArrayList<TxInput> inputsTx1 = new ArrayList<>();
        inputsTx1.add(new TxInput(coinbaseTx.getOutput()));
        ArrayList<TxOutput> outputsTx1 = new ArrayList<>();
        outputsTx1.add(new TxOutput(HelperGenerator.address, HelperGenerator.OUTPUT_VALUE, 0)); // sending some to other address
        outputsTx1.add(new TxOutput(HelperGenerator.address, HelperGenerator.OUTPUT_VALUE, 1)); // sending rest back to itself -> include fee

        // first transaction
        Transaction tx1 = new Transaction(inputsTx1, outputsTx1, HelperGenerator.publicKey);
        try {
            tx1.sign(HelperGenerator.privateKey);
        } catch (InvalidKeyException e) {
            assertNull(e);
        }

        transactions.add(coinbaseTx);
        transactions.add(tx1);

        return transactions;
    }

    public static Block generateTestEmptyBlock() {
        return new Block(
                new byte[]{1,1,1,1,1,1,1},
                new byte[]{0,0,0,0,0,0,0},
                new byte[]{0,0,0,0,0,0},
                new byte[]{0,0,0},
                System.currentTimeMillis(), 5, 1);
    }

    public static Block generateValidFullBlock() throws InvalidKeySpecException, InvalidKeyException {
        ArrayList<AbstractTransaction> tx = generateTwoTransactionArrayWithGenesis();
        // TODO generate block
        return null;
    }

    @Test
    public void testTransactionGeneration() throws InvalidKeySpecException, InvalidKeyException {
        Transaction tx = generateTestValidTransaction();

        assertTrue(tx.valid());
    }

    @Test
    public void testTwoArrayTransactionsGeneration() throws InvalidKeySpecException, InvalidKeyException {
        ArrayList<AbstractTransaction> transactions = generateTwoTransactionArrayWithGenesis();

        for (AbstractTransaction tx : transactions) {
            assertTrue(tx.valid());
        }
    }
}
