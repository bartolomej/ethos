package db;

import config.Global;
import core.transaction.CoinbaseTransaction;
import core.transaction.Transaction;
import core.transaction.TxInput;
import core.transaction.TxOutput;
import crypto.HashUtil;
import crypto.KeyUtil;
import crypto.SigUtil;
import org.junit.Test;
import util.ByteUtil;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class TransactionStoreTest {

    @Test
    public void storeTransaction() throws InvalidKeySpecException, InvalidKeyException {
        Transaction tx1 = generateTransaction();
        TransactionStore.save(tx1.getHash(), tx1.toJson());

        String path = ByteUtil.toHexString(tx1.getHash()) + ".json";

        assertTrue(fileExists(path));
        removeFile(path);
    }

    // TODO: complete tests
    @Test
    public void getTransaction() throws InvalidKeySpecException, InvalidKeyException {
        Transaction tx1 = generateTransaction();

        TransactionStore.save(tx1.getHash(), tx1.toJson());
        Transaction storedTx = TransactionStore.getByHash(tx1.getHash());

        assertTrue(fileExists("/" + ByteUtil.toHexString(tx1.getHash()) + ".json"));
    }

    private boolean fileExists(String path) {
        path = Global.ROOT_DIR + "/" + path;
        return new File(path).exists();
    }

    private void removeFile(String path) {
        new File(path).delete();
    }

    private Transaction generateTransaction() throws InvalidKeySpecException, InvalidKeyException {
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
        outputsTx1.add(new TxOutput(address1, 90 - Transaction.MIN_FEE, 1)); // sending rest back to itself

        // transaction parameters
        long timestamp = System.currentTimeMillis();
        String headerString = timestamp + outputsTx1.toString() + inputsTx1.toString() + ByteUtil.toHexString(publicKey1.getEncoded());
        byte[] signature = SigUtil.sign(privateKey1, headerString.getBytes());
        byte[] hash = HashUtil.sha256((headerString + ByteUtil.toHexString(signature)).getBytes());

        return new Transaction(inputsTx1, outputsTx1, publicKey1, signature, hash, timestamp);
    }

}