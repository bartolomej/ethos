package core;

import core.transaction.CoinbaseTransaction;
import core.transaction.Transaction;
import core.transaction.TxInput;
import core.transaction.TxOutput;
import crypto.KeyUtil;
import org.junit.Test;

import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class TransactionChainingTest {

    @Test
    public void generateValidTransactionWithMultipleOutputs() {
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
        CoinbaseTransaction coinbaseTx = new CoinbaseTransaction(address1);

        // input-outputs for tx1
        ArrayList<TxInput> inputsTx1 = new ArrayList<>();
        inputsTx1.add(new TxInput(coinbaseTx.getOutput()));
        ArrayList<TxOutput> outputsTx1 = new ArrayList<>();
        outputsTx1.add(new TxOutput(address2, 10, 0)); // sending some to other address
        outputsTx1.add(new TxOutput(address1, 90 - Transaction.MIN_FEE, 1)); // sending rest back to itself -> include fee

        // first transaction
        Transaction tx1 = new Transaction(inputsTx1, outputsTx1, publicKey1);
        try {
            tx1.sign(privateKey1);
        } catch (InvalidKeyException e) {
            assertNull(e);
        }

        assertTrue(tx1.valid());
    }

    @Test
    public void generateChainWithCoinbaseTransaction() {
        byte[] address1 = new byte[]{0,0,0,0,0,0,0,0,0};
        byte[] address2 = new byte[]{1,1,1,1,1,1,1,1,1};

        // first account
        KeyUtil keys1 = KeyUtil.generate();
        PrivateKey privateKey1 = keys1.getPrivateKey();
        PublicKey publicKey1 = keys1.getPublicKey();

        // second account
        KeyUtil keys2 = KeyUtil.generate();
        PrivateKey privateKey2 = keys2.getPrivateKey();
        PublicKey publicKey2 = keys2.getPublicKey();

        // coinbase transaction
        CoinbaseTransaction coinbaseTx = new CoinbaseTransaction(address1);

        // input-outputs for tx1
        ArrayList<TxInput> inputsTx1 = new ArrayList<>();
        inputsTx1.add(new TxInput(coinbaseTx.getHash(), 100, 0));
        ArrayList<TxOutput> outputsTx1 = new ArrayList<>();
        outputsTx1.add(new TxOutput(address2, 10, 0));

        // first transaction
        Transaction tx1 = new Transaction(inputsTx1, outputsTx1, publicKey1);
        try {
            tx1.sign(privateKey1);
        } catch (InvalidKeyException e) {
            assertNull(e);
        }

        // input-outputs for tx2
        ArrayList<TxInput> inputsTx2 = new ArrayList<>();
        inputsTx1.add(new TxInput(coinbaseTx.getHash(), 100, 0));
        ArrayList<TxOutput> outputsTx2 = new ArrayList<>();
        outputsTx1.add(new TxOutput(address2, 10, 0));

        Transaction tx2 = new Transaction(inputsTx2, outputsTx2, publicKey2);
        try {
            tx2.sign(privateKey2);
        } catch (Exception e) {
            assertNull(e);
        }
        try {
            tx1.validate();
        } catch (TransactionException e) {
            assertNull(e);
        }

        assertTrue(tx1.valid()); // TODO: doesn't sign first transaction
        assertTrue(tx2.valid());
    }

}
