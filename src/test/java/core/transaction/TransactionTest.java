package core.transaction;

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

        assertArrayEquals(coinbase.getOutput().getRecipientPubKey(), publicKey.getEncoded());
    }

    @Test
    public void generateInvalidRawTransaction() throws InvalidKeyException, InvalidKeySpecException {
        // first account
        KeyUtil keys1 = KeyUtil.generate();
        PrivateKey privateKey1 = keys1.getPrivateKey();
        PublicKey publicKey1 = keys1.getPublicKey();

        // second account
        KeyUtil keys2 = KeyUtil.generate();
        PrivateKey privateKey2 = keys2.getPrivateKey();
        PublicKey publicKey2 = keys2.getPublicKey();

        // coinbase transaction
        CoinbaseTransaction coinbaseTx = CoinbaseTransaction.generate(publicKey2.getEncoded());

        byte[] sigTx1 = SigUtil.sign(privateKey1, coinbaseTx.getOutput().getHashValue());

        // input-outputs for tx1
        ArrayList<TxInput> inputsTx1 = new ArrayList<>();
        inputsTx1.add(new TxInput(sigTx1, coinbaseTx.getHash(), coinbaseTx.getOutput()));
        ArrayList<TxOutput> outputsTx1 = new ArrayList<>();
        outputsTx1.add(new TxOutput(publicKey2.getEncoded(), 10, 0)); // sending some to other address
        outputsTx1.add(new TxOutput(publicKey1.getEncoded(), 90 - Constants.MIN_TX_FEE, 1)); // sending rest back to itself

        // transaction parameters
        long timestamp = System.currentTimeMillis() - 100;
        String headerString = timestamp + outputsTx1.toString() + inputsTx1.toString() + ByteUtil.toHexString(publicKey1.getEncoded());
        byte[] signature = SigUtil.sign(privateKey1, headerString.getBytes());
        byte[] hash1 = HashUtil.sha256((headerString + ByteUtil.toHexString(signature)).getBytes());

        Transaction tx1 = new Transaction(inputsTx1, outputsTx1, publicKey1.getEncoded(), signature, hash1, timestamp);

        assertEquals(headerString, tx1.getHeaderString());
        assertFalse(tx1.valid());
        assertFalse(inputsValid(inputsTx1));
        assertTrue(outputsValid(outputsTx1));
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

        // coinbase transaction
        CoinbaseTransaction coinbaseTx = CoinbaseTransaction.generate(publicKey1.getEncoded());

        byte[] sigTx1 = SigUtil.sign(privateKey1, coinbaseTx.getOutput().getHashValue());

        // input-outputs for tx1
        ArrayList<TxInput> inputsTx1 = new ArrayList<>();
        inputsTx1.add(new TxInput(sigTx1, coinbaseTx.getHash(), coinbaseTx.getOutput()));
        ArrayList<TxOutput> outputsTx1 = new ArrayList<>();
        outputsTx1.add(new TxOutput(publicKey2.getEncoded(), 10, 0)); // sending some to other address
        outputsTx1.add(new TxOutput(publicKey1.getEncoded(), 90 - Constants.MIN_TX_FEE, 1)); // sending rest back to itself

        // transaction parameters
        long timestamp = System.currentTimeMillis() - 100;
        String headerString = timestamp + outputsTx1.toString() + inputsTx1.toString() + ByteUtil.toHexString(publicKey1.getEncoded());
        byte[] signature = SigUtil.sign(privateKey1, headerString.getBytes());
        byte[] hash1 = HashUtil.sha256((headerString + ByteUtil.toHexString(signature)).getBytes());

        Transaction tx1 = new Transaction(inputsTx1, outputsTx1, publicKey1.getEncoded(), signature, hash1, timestamp);

        assertEquals(headerString, tx1.getHeaderString());
        assertTrue(tx1.valid());
        assertTrue(inputsValid(inputsTx1));
        assertTrue(outputsValid(outputsTx1));
    }

    @Test
    public void generateValidTransactionsWithInputOutputChain() throws InvalidKeyException, InvalidKeySpecException {
        // first account
        KeyUtil keys1 = KeyUtil.generate();
        PrivateKey privateKey1 = keys1.getPrivateKey();
        PublicKey publicKey1 = keys1.getPublicKey();

        // second account
        KeyUtil keys2 = KeyUtil.generate();
        PrivateKey privateKey2 = keys2.getPrivateKey();
        PublicKey publicKey2 = keys2.getPublicKey();

        // coinbase transaction
        CoinbaseTransaction coinbaseTx = CoinbaseTransaction.generate(publicKey1.getEncoded());

        byte[] sigTx1 = SigUtil.sign(privateKey1, coinbaseTx.getOutput().getHashValue());

        // TRANSACTION 1 -> pubKey1_balance: 80, pubKey2_balance: 10
        ArrayList<TxInput> inputsTx1 = new ArrayList<>();
        inputsTx1.add(new TxInput(sigTx1, coinbaseTx.getHash(), coinbaseTx.getOutput()));
        ArrayList<TxOutput> outputsTx1 = new ArrayList<>();
        outputsTx1.add(new TxOutput(publicKey2.getEncoded(), 10, 0)); // sending some to other address
        outputsTx1.add(new TxOutput(publicKey1.getEncoded(), 90 - Constants.MIN_TX_FEE, 1)); // sending rest back to itself

        long timestamp1 = System.currentTimeMillis() - 100;
        String headerString1 = timestamp1 + outputsTx1.toString() + inputsTx1.toString() + ByteUtil.toHexString(publicKey1.getEncoded());
        byte[] signature1 = SigUtil.sign(privateKey1, headerString1.getBytes());
        byte[] hash1 = HashUtil.sha256((headerString1 + ByteUtil.toHexString(signature1)).getBytes());

        Transaction tx1 = new Transaction(inputsTx1, outputsTx1, publicKey1.getEncoded(), signature1, hash1, timestamp1);

        // TRANSACTION 2: pubKey1_balance: 70, pubKey2_balance: 20
        byte[] sig1Tx2 = SigUtil.sign(privateKey1, tx1.getOutputs().get(1).getHashValue());

        ArrayList<TxInput> inputsTx2 = new ArrayList<>();
        inputsTx2.add(new TxInput(sig1Tx2, tx1.getHash(), tx1.getOutputs().get(1)));
        ArrayList<TxOutput> outputsTx2 = new ArrayList<>();
        outputsTx2.add(new TxOutput(publicKey2.getEncoded(), 10, 0)); // sending some to other address
        outputsTx2.add(new TxOutput(publicKey1.getEncoded(), 70 - Constants.MIN_TX_FEE, 1)); // sending rest back to itself

        long timestamp2 = System.currentTimeMillis() - 100;
        String headerString2 = timestamp2 + outputsTx2.toString() + inputsTx2.toString() + ByteUtil.toHexString(publicKey1.getEncoded());
        byte[] signature2 = SigUtil.sign(privateKey1, headerString2.getBytes());
        byte[] hash2 = HashUtil.sha256((headerString2 + ByteUtil.toHexString(signature2)).getBytes());

        Transaction tx2 = new Transaction(inputsTx2, outputsTx2, publicKey1.getEncoded(), signature2, hash2, timestamp2);

        assertEquals(headerString2, tx2.getHeaderString());
        assertTrue(tx2.valid());
        assertTrue(inputsValid(inputsTx2));
        assertTrue(outputsValid(outputsTx2));
    }

    @Test
    public void generateTransactionWithInsufficientFee() {
        // TODO: rewrite with new input / output design
    }

    @Test
    public void generateTransactionWithSufficientFee() {
        // TODO: rewrite with new input / output design
    }

    @Test
    public void generateTransactionWithInvalidInputs() {
        // TODO: rewrite with new input / output design
    }

    private ArrayList<TxInput> generateTestInputs(byte[][] signatures, byte[][] txHashes, ArrayList<TxOutput> outputs) {
        ArrayList<TxInput> inputs = new ArrayList<>();
        for (int i = 0; i < outputs.size(); i++) {
            inputs.add(new TxInput(signatures[i], txHashes[i], outputs.get(i)));
        }
        return inputs;
    }

    private ArrayList<TxOutput> generateTestOutputs(byte[][] recipientPubKeys, long[] values) {
        ArrayList<TxOutput> outputs = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            outputs.add(new TxOutput(recipientPubKeys[i], values[i], i));
        }
        return outputs;
    }

    private Transaction generateTransaction() {
        // TODO: rewrite with new input / output design
        return null;
    }

    private boolean inputsValid(ArrayList<TxInput> inputs) {
        for (TxInput input : inputs) {
            if (!input.valid()) return false;
        }
        return true;
    }

    private boolean outputsValid(ArrayList<TxOutput> outputs) {
        for (TxOutput output : outputs) {
            if (!output.valid()) return false;
        }
        return true;
    }

}