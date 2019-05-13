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

    @Test
    public void testTransactionGeneration() throws InvalidKeySpecException, InvalidKeyException {
        Transaction tx = generateTestValidTransaction();

        assertTrue(tx.valid());
    }
}