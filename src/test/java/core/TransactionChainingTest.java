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
        inputsTx1.add(new TxInput(coinbaseTx.getHash(), 0, 100));
        ArrayList<TxOutput> outputsTx1 = new ArrayList<>();
        outputsTx1.add(new TxOutput(address2, 10));

        // first transaction
        Transaction tx1 = new Transaction(inputsTx1, outputsTx1, publicKey1);
        try {
            tx1.sign(privateKey1);
        } catch (InvalidKeyException e) {
            assertNull(e);
        }

        // input-outputs for tx1
        ArrayList<TxInput> inputsTx2 = new ArrayList<>();
        inputsTx1.add(new TxInput(coinbaseTx.getHash(), 0, 100));
        ArrayList<TxOutput> outputsTx2 = new ArrayList<>();
        outputsTx1.add(new TxOutput(address2, 10));

        Transaction tx2 = new Transaction(inputsTx2, outputsTx2, publicKey2);
        try {
            tx2.sign(privateKey2);
        } catch (Exception e) {
            assertNull(e);
        }

        assertTrue(tx1.valid());
        assertTrue(tx2.valid());
    }
}
