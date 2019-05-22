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
    public void assertTransactionEquality() throws InvalidKeySpecException, InvalidKeyException {
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
        PublicKey address1 = keys1.getPublicKey();

        // second account
        KeyUtil keys2 = KeyUtil.generate();
        PrivateKey privateKey2 = keys2.getPrivateKey();
        PublicKey address2 = keys2.getPublicKey();

        // coinbase transaction
        CoinbaseTransaction coinbaseTx = CoinbaseTransaction.generate(address2.getEncoded());

        byte[] sigTx1 = SigUtil.sign(privateKey1, coinbaseTx.getOutput().getHashValue());

        // input-outputs for tx1
        ArrayList<TxInput> inputsTx1 = new ArrayList<>();
        inputsTx1.add(new TxInput(sigTx1, coinbaseTx.getHash(), coinbaseTx.getOutput()));
        ArrayList<TxOutput> outputsTx1 = new ArrayList<>();
        outputsTx1.add(new TxOutput(address2.getEncoded(), 10, 0)); // sending some to other address
        outputsTx1.add(new TxOutput(address1.getEncoded(), 90 - Constants.MIN_TX_FEE, 1)); // sending rest back to itself

        // transaction parameters
        long timestamp = System.currentTimeMillis() - 100;
        String headerString = timestamp + outputsTx1.toString() + inputsTx1.toString() + ByteUtil.toHexString(address1.getEncoded());
        byte[] signature = SigUtil.sign(privateKey1, headerString.getBytes());
        byte[] hash1 = HashUtil.sha256((headerString + ByteUtil.toHexString(signature)).getBytes());

        Transaction tx1 = new Transaction(inputsTx1, outputsTx1, address1.getEncoded(), signature, hash1, timestamp);

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
        PublicKey address1 = keys1.getPublicKey();

        // second account
        KeyUtil keys2 = KeyUtil.generate();
        PrivateKey privateKey2 = keys2.getPrivateKey();
        PublicKey address2 = keys2.getPublicKey();

        // coinbase transaction
        CoinbaseTransaction coinbaseTx = CoinbaseTransaction.generate(address1.getEncoded());

        // input-outputs for tx1
        ArrayList<TxInput> inputsTx1 = new ArrayList<>();
        byte[] sigData = (ByteUtil.toHexString(coinbaseTx.getHash()) + coinbaseTx.getOutput().getOutputIndex()).getBytes();
        byte[] sigTx1 = SigUtil.sign(privateKey1, sigData);
        inputsTx1.add(new TxInput(sigTx1, coinbaseTx.getHash(), coinbaseTx.getOutput()));
        ArrayList<TxOutput> outputsTx1 = new ArrayList<>();
        outputsTx1.add(new TxOutput(address2.getEncoded(), 10, 0)); // sending some to other address
        outputsTx1.add(new TxOutput(address1.getEncoded(), 90 - Constants.MIN_TX_FEE, 1)); // sending rest back to itself

        // transaction parameters
        long timestamp = System.currentTimeMillis() - 100;
        String headerString = timestamp + outputsTx1.toString() + inputsTx1.toString() + ByteUtil.toHexString(address1.getEncoded());
        byte[] signature = SigUtil.sign(privateKey1, headerString.getBytes());
        byte[] hash1 = HashUtil.sha256((headerString + ByteUtil.toHexString(signature)).getBytes());

        Transaction tx1 = new Transaction(inputsTx1, outputsTx1, address1.getEncoded(), signature, hash1, timestamp);

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
        PublicKey address1 = keys1.getPublicKey();

        // second account
        KeyUtil keys2 = KeyUtil.generate();
        PrivateKey privateKey2 = keys2.getPrivateKey();
        PublicKey address2 = keys2.getPublicKey();

        // COINBASE TRANSACTION
        CoinbaseTransaction coinbaseTx = CoinbaseTransaction.generate(address1.getEncoded());

        // FIRST TRANSACTION
        ArrayList<TxInput> inputsTx1 = new ArrayList<>();
        byte[] sigData1 = (ByteUtil.toHexString(coinbaseTx.getHash()) + coinbaseTx.getOutput().getOutputIndex()).getBytes();
        byte[] sigInput1Tx1 = SigUtil.sign(privateKey1, sigData1);
        inputsTx1.add(new TxInput(sigInput1Tx1, coinbaseTx.getHash(), coinbaseTx.getOutput()));
        ArrayList<TxOutput> outputsTx1 = new ArrayList<>();
        outputsTx1.add(new TxOutput(address2.getEncoded(), 10, 0)); // sending some to other address
        outputsTx1.add(new TxOutput(address1.getEncoded(), 90 - Constants.MIN_TX_FEE, 1)); // sending rest back to itself

        long timestamp1 = System.currentTimeMillis() - 100;
        String headerString1 = timestamp1 + outputsTx1.toString() + inputsTx1.toString() + ByteUtil.toHexString(address1.getEncoded());
        byte[] sigTx1 = SigUtil.sign(privateKey1, headerString1.getBytes());
        byte[] hash1 = HashUtil.sha256((headerString1 + ByteUtil.toHexString(sigTx1)).getBytes());
        /* - send 80 to address1
         * - send 10 to address2 */
        Transaction tx1 = new Transaction(inputsTx1, outputsTx1, address1.getEncoded(), sigTx1, hash1, timestamp1);

        // SECOND TRANSACTION
        ArrayList<TxInput> inputsTx2 = new ArrayList<>();
        byte[] sigData = (ByteUtil.toHexString(coinbaseTx.getHash()) + tx1.getOutputs()).getBytes();
        byte[] sigInput1Tx2 = SigUtil.sign(privateKey1, sigData);
        inputsTx2.add(new TxInput(sigInput1Tx2, tx1.getHash(), tx1.getOutputs().get(1)));
        ArrayList<TxOutput> outputsTx2 = new ArrayList<>();
        outputsTx2.add(new TxOutput(address2.getEncoded(), 10, 0)); // sending some to other address
        outputsTx2.add(new TxOutput(address1.getEncoded(), 70 - Constants.MIN_TX_FEE, 1)); // sending rest back to itself

        long timestamp2 = System.currentTimeMillis() - 100;
        String headerString2 = timestamp2 + outputsTx2.toString() + inputsTx2.toString() + ByteUtil.toHexString(address1.getEncoded());
        byte[] sigTx2 = SigUtil.sign(privateKey1, headerString2.getBytes());
        byte[] hash2 = HashUtil.sha256((headerString2 + ByteUtil.toHexString(sigTx2)).getBytes());
        /* - send 60 to address1
         * - send 10 to address2 */
        Transaction tx2 = new Transaction(inputsTx2, outputsTx2, address1.getEncoded(), sigTx2, hash2, timestamp2);

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

    private Transaction generateTransaction() throws InvalidKeySpecException, InvalidKeyException {
        // first account
        KeyUtil keyPairAccount1 = KeyUtil.generate();
        PrivateKey privateKey1 = keyPairAccount1.getPrivateKey();
        PublicKey address1 = keyPairAccount1.getPublicKey();
        // second account
        KeyUtil keyPairAccount2 = KeyUtil.generate();
        PrivateKey privateKey2 = keyPairAccount2.getPrivateKey();
        PublicKey address2 = keyPairAccount2.getPublicKey();

        CoinbaseTransaction coinbaseTx1 = CoinbaseTransaction.generate(address1.getEncoded());
        assertTrue(coinbaseTx1.valid());

        ArrayList<TxInput> inputsTx1 = new ArrayList<>();
        // sign prev transaction output content
        byte[] sig = SigUtil.sign(privateKey1,
                (ByteUtil.toHexString(coinbaseTx1.getHash()) + coinbaseTx1.getOutput().getOutputIndex()).getBytes());
        inputsTx1.add(new TxInput(sig, coinbaseTx1.getHash(), coinbaseTx1.getOutput()));
        ArrayList<TxOutput> outputsTx1 = new ArrayList<>();
        outputsTx1.add(new TxOutput(address2.getEncoded(), 10, 0)); // address1 balance: 100 (BLOCK_REWARD) - 10 - 10

        long timestamp = System.currentTimeMillis();
        String txHeaderData = timestamp + outputsTx1.toString() + inputsTx1.toString() + ByteUtil.toHexString(address1.getEncoded());
        byte[] tx1Signature = SigUtil.sign(privateKey1, txHeaderData.getBytes());
        byte[] tx1Hash = HashUtil.sha256(txHeaderData.getBytes());

        return new Transaction(inputsTx1, outputsTx1, address1.getEncoded(), tx1Signature, tx1Hash, timestamp);
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