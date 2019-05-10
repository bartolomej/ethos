package core;

import core.transaction.CoinbaseTransaction;
import core.transaction.Transaction;
import core.transaction.TxInput;
import core.transaction.TxOutput;
import crypto.KeyUtil;
import org.junit.Test;
import util.ByteUtil;

import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class TransactionTest {

    // TODO: resolve signature invalid error on verify

    @Test
    public void assertTransactionEquality() {
        Transaction transaction1 = generateTransaction();

        assertTrue(transaction1.equals(transaction1));
    }

    @Test
    public void generateValidCoinbaseTransaction() {
        byte[] toAddress = new byte[]{0,0,0,0,0,0,0};
        CoinbaseTransaction coinbase = new CoinbaseTransaction(toAddress);

        assertEquals(coinbase.getReceiveAddress(), toAddress);
    }

    @Test
    public void generateValidTransaction() {
        KeyUtil keys = KeyUtil.generate();
        PrivateKey privateKey = keys.getPrivateKey();
        PublicKey publicKey = keys.getPublicKey();

        byte[][] txInputAddresses = new byte[][]{new byte[]{0,0,0,0,0,0}, new byte[]{1,1,1,1,1,1}};
        byte[][] txOutputAddresses = new byte[][]{new byte[]{0,0,0,0,0,0}, new byte[]{1,1,1,1,1,1}};

        ArrayList<TxInput> inputs = generateTestInputs(txInputAddresses, new long[]{200, 100});
        ArrayList<TxOutput> outputs = generateTestOutputs(txOutputAddresses, new long[]{50, 200});

        Transaction transaction = new Transaction(inputs, outputs, publicKey);
        try {
            transaction.sign(privateKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        assertTrue(transaction.valid());
    }

    @Test
    public void generateTransactionWithInsufficientFee() {
        KeyUtil keys = KeyUtil.generate();
        PrivateKey privateKey = keys.getPrivateKey();
        PublicKey publicKey = keys.getPublicKey();

        byte[][] txInputAddresses = new byte[][]{new byte[]{0,0,0,0,0,0}};
        byte[][] txOutputAddresses = new byte[][]{new byte[]{1,1,1,1,1,1}};

        ArrayList<TxInput> inputs = generateTestInputs(txInputAddresses, new long[]{100});
        ArrayList<TxOutput> outputs = generateTestOutputs(txOutputAddresses, new long[]{98});

        Transaction transaction = new Transaction(inputs, outputs, publicKey);
        try {
            transaction.sign(privateKey);
            transaction.validate();
            throw new Exception("Test failed");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Insufficient transaction fee");
        }
        assertFalse(transaction.valid());
        assertTrue((transaction.getInputsValue() - transaction.getOutputsValue()) < Transaction.MIN_FEE);
    }

    @Test
    public void generateTransactionWithSufficientFee() {
        KeyUtil keys = KeyUtil.generate();
        PrivateKey privateKey = keys.getPrivateKey();
        PublicKey publicKey = keys.getPublicKey();

        byte[][] txInputAddresses = new byte[][]{new byte[]{0,0,0,0,0,0}};
        byte[][] txOutputAddresses = new byte[][]{new byte[]{1,1,1,1,1,1}};

        ArrayList<TxInput> inputs = generateTestInputs(txInputAddresses, new long[]{100});
        ArrayList<TxOutput> outputs = generateTestOutputs(txOutputAddresses, new long[]{97});

        Transaction transaction = new Transaction(inputs, outputs, publicKey);
        try {
            transaction.sign(privateKey);
            transaction.validate();
        } catch (Exception e) {
            assertNull(e);
        }
        assertFalse(transaction.valid());
        assertTrue((transaction.getInputsValue() - transaction.getOutputsValue()) == Transaction.MIN_FEE);
    }

    @Test
    public void generateTransactionWithInvalidInputs() {
        KeyUtil keys = KeyUtil.generate();
        PrivateKey privateKey = keys.getPrivateKey();
        PublicKey publicKey = keys.getPublicKey();

        byte[][] txInputAddresses = new byte[][]{new byte[]{0,0,0,0,0,0}, new byte[]{1,1,1,1,1,1}};
        byte[][] txOutputAddresses = new byte[][]{new byte[]{0,0,0,0,0,0}, new byte[]{1,1,1,1,1,1}};

        ArrayList<TxInput> inputs = generateTestInputs(txInputAddresses, new long[]{50, 100});
        ArrayList<TxOutput> outputs = generateTestOutputs(txOutputAddresses, new long[]{100, 200});

        Transaction transaction = new Transaction(inputs, outputs, publicKey);
        try {
            transaction.sign(privateKey);
            transaction.validate();
            throw new Exception("Test failed");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "Insufficient inputs");
        }
        assertFalse(transaction.valid());
    }

    @Test
    public void inputTestGeneratorUtils() {
        byte[] txAddress1 = new byte[]{0,0,0,0,0,0};
        byte[] txAddress2 = new byte[]{1,1,1,1,1,1};

        ArrayList<TxInput> inputs = generateTestInputs(new byte[][]{txAddress1, txAddress2}, new long[]{50, 100});

        assertTrue(inputs.get(0).equals(new TxInput(txAddress1, 0, 50)));
        assertTrue(inputs.get(1).equals(new TxInput(txAddress2, 0, 100)));
    }

    @Test
    public void outputTestGenerators() {
        byte[] txAddress1 = new byte[]{0,0,0,0,0,0};
        byte[] txAddress2 = new byte[]{1,1,1,1,1,1};

        ArrayList<TxOutput> outputs = generateTestOutputs(new byte[][]{txAddress1, txAddress2}, new long[]{50, 100});

        assertTrue(outputs.get(0).equals(new TxOutput(txAddress1, 50)));
        assertTrue(outputs.get(1).equals(new TxOutput(txAddress2, 100)));
    }

    @Test
    public void inputTestGenerators() {
        byte[] txAddress1 = new byte[]{0,0,0,0,0,0};
        byte[] txAddress2 = new byte[]{1,1,1,1,1,1};

        ArrayList<TxInput> inputs = generateTestInputs(new byte[][]{txAddress1, txAddress2}, new long[]{50, 100});

        assertTrue(inputs.get(0).equals(new TxInput(txAddress1, 0, 50)));
        assertTrue(inputs.get(1).equals(new TxInput(txAddress2, 0, 100)));
    }

    private ArrayList<TxInput> generateTestInputs(byte[][] referenceTransactions, long[] values) {
        ArrayList<TxInput> inputs = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            inputs.add(new TxInput(referenceTransactions[i], 0, values[i]));
        }
        return inputs;
    }

    private ArrayList<TxOutput> generateTestOutputs(byte[][] referenceTransactions, long[] values) {
        ArrayList<TxOutput> outputs = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            outputs.add(new TxOutput(referenceTransactions[i], values[i]));
        }
        return outputs;
    }

    private Transaction generateTransaction() {
        KeyUtil keys = KeyUtil.generate();
        PrivateKey privateKey = keys.getPrivateKey();
        PublicKey publicKey = keys.getPublicKey();

        byte[][] txInputAddresses = new byte[][]{new byte[]{0,0,0,0,0,0}, new byte[]{1,1,1,1,1,1}};
        byte[][] txOutputAddresses = new byte[][]{new byte[]{0,0,0,0,0,0}, new byte[]{1,1,1,1,1,1}};

        ArrayList<TxInput> inputs = generateTestInputs(txInputAddresses, new long[]{200, 100});
        ArrayList<TxOutput> outputs = generateTestOutputs(txOutputAddresses, new long[]{50, 200});

        Transaction transaction = new Transaction(inputs, outputs, publicKey);
        try {
            transaction.sign(privateKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return transaction;
    }

}