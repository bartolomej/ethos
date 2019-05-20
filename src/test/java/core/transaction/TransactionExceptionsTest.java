package core.transaction;

import config.Constants;
import core.transaction.CoinbaseTransaction;
import core.transaction.Transaction;
import core.transaction.TxInput;
import core.transaction.TxOutput;
import crypto.HashUtil;
import crypto.KeyUtil;
import crypto.SigUtil;
import errors.TransactionException;
import org.junit.Test;
import util.ByteUtil;

import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class TransactionExceptionsTest {

    @Test
    public void shouldProduceInvalidTimestampException() throws InvalidKeyException, InvalidKeySpecException {
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

        // TODO: adjust for new input/outputs design
        // input-outputs for tx1
        ArrayList<TxInput> inputsTx1 = new ArrayList<>();
        //inputsTx1.add(new TxInput(coinbaseTx.getOutput()));
        ArrayList<TxOutput> outputsTx1 = new ArrayList<>();
        //outputsTx1.add(new TxOutput(address2, 10, 0)); // sending some to other address
        //outputsTx1.add(new TxOutput(address1, 90 - Constants.MIN_TX_FEE, 1)); // sending rest back to itself

        // transaction parameters
        long timestamp = System.currentTimeMillis() + 1000;
        String headerString = timestamp + outputsTx1.toString() + inputsTx1.toString() + ByteUtil.toHexString(publicKey1.getEncoded());
        byte[] signature = SigUtil.sign(privateKey1, headerString.getBytes());
        byte[] hash = HashUtil.sha256((headerString + ByteUtil.toHexString(signature)).getBytes());

        Transaction tx1 = new Transaction(inputsTx1, outputsTx1, publicKey1.getEncoded(), signature, hash, timestamp);

        try {
            tx1.validate();
            assert false; // errors not triggered
        } catch (TransactionException e) {
            assertEquals(e.getMessage(), "Timestamp invalid");
        }

        assertEquals(headerString, tx1.getHeaderString());
        assertFalse(tx1.valid());
    }

    @Test
    public void shouldProduceInsufficientInputsException() throws InvalidKeyException, InvalidKeySpecException {
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
        //inputsTx1.add(new TxInput(coinbaseTx.getOutput()));
        ArrayList<TxOutput> outputsTx1 = new ArrayList<>();
        //outputsTx1.add(new TxOutput(address2, 10, 0)); // sending some to other address
        //outputsTx1.add(new TxOutput(address1, 101, 1)); // sending rest back to itself

        // transaction parameters
        long timestamp = System.currentTimeMillis();
        String headerString = timestamp + outputsTx1.toString() + inputsTx1.toString() + ByteUtil.toHexString(publicKey1.getEncoded());
        byte[] signature = SigUtil.sign(privateKey1, headerString.getBytes());
        byte[] hash = HashUtil.sha256((headerString + ByteUtil.toHexString(signature)).getBytes());

        Transaction tx1 = new Transaction(inputsTx1, outputsTx1, publicKey1.getEncoded(), signature, hash, timestamp);

        try {
            tx1.validate();
            assert false; // errors not triggered
        } catch (TransactionException e) {
            assertEquals(e.getMessage(), "Insufficient inputs");
        }

        assertEquals(headerString, tx1.getHeaderString());
        assertFalse(tx1.valid());
    }

    @Test
    public void shouldProduceInsufficientFeeException() throws InvalidKeyException, InvalidKeySpecException {
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
        //inputsTx1.add(new TxInput(coinbaseTx.getOutput()));
        ArrayList<TxOutput> outputsTx1 = new ArrayList<>();
        //outputsTx1.add(new TxOutput(address2, 10, 0)); // sending some to other address
        //outputsTx1.add(new TxOutput(address1, 90 - Constants.MIN_TX_FEE + 1, 1)); // sending rest back to itself

        // transaction parameters
        long timestamp = System.currentTimeMillis();
        String headerString = timestamp + outputsTx1.toString() + inputsTx1.toString() + ByteUtil.toHexString(publicKey1.getEncoded());
        byte[] signature = SigUtil.sign(privateKey1, headerString.getBytes());
        byte[] hash = HashUtil.sha256((headerString + ByteUtil.toHexString(signature)).getBytes());

        Transaction tx1 = new Transaction(inputsTx1, outputsTx1, publicKey1.getEncoded(), signature, hash, timestamp);

        try {
            tx1.validate();
            assert false; // errors not triggered
        } catch (TransactionException e) {
            assertEquals(e.getMessage(), "Insufficient transaction fee");
        }

        assertEquals(headerString, tx1.getHeaderString());
        assertFalse(tx1.valid());
    }

}
