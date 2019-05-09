package core;

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

    @Test
    public void verifyTransactionWithInvalidInputs() {}

    public void verifyTransactionWithInvalidUTXO() {}

    @Test
    public void verifyTransaction() {
        KeyUtil keys = KeyUtil.generate();
        PrivateKey privateKey = keys.getPrivateKey();
        PublicKey publicKey = keys.getPublicKey();

        /* TESTING BOUNDARIES:
        * This tests span out of transaction class boundaries!
        * */
        byte[] toAddress = publicKey.getEncoded();
        Transaction transaction = null; //new Transaction(toAddress, 100);
        try {
            transaction.sign(privateKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        transaction.verify(publicKey);
        boolean isValid = transaction.valid();
        assertTrue(isValid);
    }

    @Test
    public void toStringTest() {
        Transaction transaction = this.processTransaction();
        String stringEncoded = transaction.toString();
        assertNotNull(stringEncoded);
    }

    private Transaction processTransaction() {
        KeyUtil keys = KeyUtil.generate();
        PrivateKey privateKey = keys.getPrivateKey();
        PublicKey publicKey = keys.getPublicKey();

        // TODO: think about testing boundaries
        ArrayList<TxInput> inputs = generateTestInputs(null);
        ArrayList<TxOutput> outputs = generateTestOutputs(null);
        byte[] toAddress = publicKey.getEncoded();

        Transaction transaction = new Transaction(toAddress, inputs, outputs);
        try {
            transaction.sign(privateKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return transaction;
    }

    private ArrayList<TxInput> generateTestInputs(byte[] referenceTransaction) {
        ArrayList<TxInput> inputs = new ArrayList<>();
        inputs.add(new TxInput(referenceTransaction, 100));
        inputs.add(new TxInput(referenceTransaction, 200));
        return inputs;
    }

    private ArrayList<TxOutput> generateTestOutputs(byte[] referenceTransaction) {
        ArrayList<TxOutput> outputs = new ArrayList<>();
        outputs.add(new TxOutput(referenceTransaction, 100));
        outputs.add(new TxOutput(referenceTransaction, 200));
        return outputs;
    }
}