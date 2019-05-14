package core;

import config.Constants;
import core.transaction.CoinbaseTransaction;
import core.transaction.Transaction;
import core.transaction.*;
import crypto.HashUtil;
import crypto.KeyUtil;
import crypto.SigUtil;
import org.junit.Test;
import util.ByteUtil;

import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class TransactionTest {

    @Test
    public void assertTransactionEquality() {
        Transaction transaction1 = generateTransaction();

        assertTrue(transaction1.equals(transaction1));
    }

    @Test
    public void generateValidCoinbaseTransaction() throws InvalidKeyException, InvalidKeySpecException{
        KeyUtil keys = KeyUtil.generate();
        PrivateKey privateKey = keys.getPrivateKey();
        PublicKey publicKey = keys.getPublicKey();

        CoinbaseTransaction coinbase = CoinbaseTransaction.generate(publicKey.getEncoded());

        assertArrayEquals(coinbase.getOutput().getTxid(), publicKey.getEncoded());
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
    public void generateValidRawTransaction() throws InvalidKeyException, InvalidKeySpecException {
        // first account
        KeyUtil keys1 = KeyUtil.generate();
        PrivateKey privateKey1 = keys1.getPrivateKey();
        PublicKey publicKey1 = keys1.getPublicKey();

        // second account
        KeyUtil keys2 = KeyUtil.generate();
        PrivateKey privateKey2 = keys2.getPrivateKey();
        PublicKey publicKey2 = keys2.getPublicKey();

        byte[] address1 = publicKey1.getEncoded();
        byte[] address2 = publicKey2.getEncoded();

        // coinbase transaction
        CoinbaseTransaction coinbaseTx = CoinbaseTransaction.generate(address1);

        // input-outputs for tx1
        ArrayList<TxInput> inputsTx1 = new ArrayList<>();
        inputsTx1.add(new TxInput(coinbaseTx.getOutput()));
        ArrayList<TxOutput> outputsTx1 = new ArrayList<>();
        outputsTx1.add(new TxOutput(address2, 10, 0)); // sending some to other address
        outputsTx1.add(new TxOutput(address1, 90 - Constants.MIN_TX_FEE, 1)); // sending rest back to itself

        // transaction parameters
        long timestamp = System.currentTimeMillis();
        String headerString = timestamp + outputsTx1.toString() + inputsTx1.toString() + ByteUtil.toHexString(publicKey1.getEncoded());
        byte[] signature = SigUtil.sign(privateKey1, headerString.getBytes());
        byte[] hash = HashUtil.sha256((headerString + ByteUtil.toHexString(signature)).getBytes());

        Transaction tx1 = new Transaction(inputsTx1, outputsTx1, publicKey1, signature, hash, timestamp);

        assertEquals(headerString, tx1.getHeaderString());
        assertTrue(tx1.valid()); // TODO: shows false but on evaluation shows true
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
        assertTrue((transaction.getInputsValue() - transaction.getOutputsValue()) < Constants.MIN_TX_FEE);
    }

    @Test
    public void generateTransactionWithSufficientFee() {
        KeyUtil keys = KeyUtil.generate();
        PrivateKey privateKey = keys.getPrivateKey();
        PublicKey publicKey = keys.getPublicKey();

        byte[][] txInputAddresses = new byte[][]{new byte[]{0,0,0,0,0,0}};
        byte[][] txOutputAddresses = new byte[][]{new byte[]{1,1,1,1,1,1}};

        ArrayList<TxInput> inputs = generateTestInputs(txInputAddresses, new long[]{100});
        ArrayList<TxOutput> outputs = generateTestOutputs(txOutputAddresses, new long[]{100-Constants.MIN_TX_FEE});

        Transaction transaction = new Transaction(inputs, outputs, publicKey);
        try {
            transaction.sign(privateKey);
            transaction.validate();
        } catch (Exception e) {
            assertNull(e);
        }

        assertTrue(transaction.valid());
        assertEquals((transaction.getInputsValue() - transaction.getOutputsValue()), Constants.MIN_TX_FEE);
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

        assertTrue(inputs.get(0).equals(new TxInput(txAddress1, 50, 0)));
        assertTrue(inputs.get(1).equals(new TxInput(txAddress2, 100, 0)));
    }

    @Test
    public void outputTestGenerators() {
        byte[] txAddress1 = new byte[]{0,0,0,0,0,0};
        byte[] txAddress2 = new byte[]{1,1,1,1,1,1};

        ArrayList<TxOutput> outputs = generateTestOutputs(new byte[][]{txAddress1, txAddress2}, new long[]{50, 100});

        assertTrue(outputs.get(0).equals(new TxOutput(txAddress1, 50, 0)));
        assertTrue(outputs.get(1).equals(new TxOutput(txAddress2, 100, 1)));
    }

    @Test
    public void inputTestGenerators() {
        byte[] txAddress1 = new byte[]{0,0,0,0,0,0};
        byte[] txAddress2 = new byte[]{1,1,1,1,1,1};

        ArrayList<TxInput> inputs = generateTestInputs(new byte[][]{txAddress1, txAddress2}, new long[]{50, 100});

        assertTrue(inputs.get(0).equals(new TxInput(txAddress1, 50, 0)));
        assertTrue(inputs.get(1).equals(new TxInput(txAddress2, 100, 0)));
    }

    private ArrayList<TxInput> generateTestInputs(byte[][] referenceTransactions, long[] values) {
        ArrayList<TxInput> inputs = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            inputs.add(new TxInput(referenceTransactions[i], values[i], 0));
        }
        return inputs;
    }

    private ArrayList<TxOutput> generateTestOutputs(byte[][] referenceTransactions, long[] values) {
        ArrayList<TxOutput> outputs = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            outputs.add(new TxOutput(referenceTransactions[i], values[i], i));
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