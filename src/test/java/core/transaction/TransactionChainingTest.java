package core.transaction;

import config.Constants;
import core.transaction.CoinbaseTransaction;
import core.transaction.Transaction;
import core.transaction.TxInput;
import core.transaction.TxOutput;
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

public class TransactionChainingTest {

    @Test
    public void generateValidTransactionWithMultipleOutputs() throws InvalidKeySpecException, InvalidKeyException {
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
        String inputSigData = ByteUtil.toHexString(coinbaseTx.getHash()) + coinbaseTx.getOutput().getOutputIndex();
        byte[] tx1Signature = SigUtil.sign(privateKey1, inputSigData.getBytes());
        inputsTx1.add(new TxInput(
                tx1Signature,
                publicKey1.getEncoded(),
                coinbaseTx.getHash(),
                coinbaseTx.getOutput().getOutputIndex(),
                coinbaseTx.getOutput().getValue()
        ));

        ArrayList<TxOutput> outputsTx1 = new ArrayList<>();
        outputsTx1.add(new TxOutput(address2, 10, 0)); // sending some to other address
        outputsTx1.add(new TxOutput(address1, 90 - Constants.MIN_TX_FEE, 1)); // sending rest back to itself -> include fee

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
    public void generateChainWithCoinbaseTransaction() throws InvalidKeySpecException, InvalidKeyException {
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

        // COINBASE TRANSACTION
        CoinbaseTransaction coinbaseTx = CoinbaseTransaction.generate(address1);

        // FIRST TRANSACTION
        byte[] sigData1 = (ByteUtil.toHexString(coinbaseTx.getHash()) + coinbaseTx.getOutput().getOutputIndex()).getBytes();;
        byte[] sigTx1Input1 = SigUtil.sign(privateKey1, sigData1);
        ArrayList<TxInput> inputsTx1 = new ArrayList<>();
        inputsTx1.add(new TxInput(
                sigTx1Input1,
                address1,
                coinbaseTx.getHash(),
                coinbaseTx.getOutput().getOutputIndex(),
                coinbaseTx.getOutput().getValue()
        ));
        ArrayList<TxOutput> outputsTx1 = new ArrayList<>();
        outputsTx1.add(new TxOutput(address2, 10, 0));

        long timestamp1 = System.currentTimeMillis() - 100;
        String headerString1 = timestamp1 + outputsTx1.toString() + inputsTx1.toString() + ByteUtil.toHexString(address1);
        byte[] sigTx1 = SigUtil.sign(privateKey1, headerString1.getBytes());
        byte[] hash1 = HashUtil.sha256((headerString1 + ByteUtil.toHexString(sigTx1)).getBytes());

        Transaction tx1 = new Transaction(inputsTx1, outputsTx1, address1, sigTx1, hash1, timestamp1);
        assertTrue(tx1.valid());

        // SECOND TRANSACTION
        byte[] sigData2 = (ByteUtil.toHexString(coinbaseTx.getHash()) + coinbaseTx.getOutput().getOutputIndex()).getBytes();
        byte[] sigTx2Input1 = SigUtil.sign(privateKey1, sigData2);
        ArrayList<TxInput> inputsTx2 = new ArrayList<>();
        inputsTx1.add(new TxInput(
                sigTx2Input1,
                address1,
                coinbaseTx.getHash(),
                coinbaseTx.getOutput().getOutputIndex(),
                coinbaseTx.getOutput().getValue()
        ));
        ArrayList<TxOutput> outputsTx2 = new ArrayList<>();
        outputsTx1.add(new TxOutput(address2, 10, 0));

        Transaction tx2 = new Transaction(inputsTx2, outputsTx2, publicKey2);
        assertTrue(tx2.valid());

        assertEquals(0, tx2.getAllExceptions().size());
        assertTrue(tx1.valid());
        assertTrue(tx2.valid());
    }

}
