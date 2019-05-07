package core;

import crypto.KeyUtil;
import org.junit.Test;
import util.BytesUtil;

import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.Assert.*;

public class TransactionTest {

    @Test
    public void verifyTransaction() {
        KeyUtil keys = KeyUtil.generate();
        PrivateKey privateKey = keys.getPrivateKey();
        PublicKey publicKey = keys.getPublicKey();

        String toAddress = BytesUtil.toHexString(publicKey.getEncoded());
        Transaction transaction = new Transaction(toAddress, 100);
        try {
            transaction.sign(privateKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        boolean isValid = transaction.verify(publicKey);
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

        String toAddress = BytesUtil.toHexString(publicKey.getEncoded());
        Transaction transaction = new Transaction(toAddress, 100);
        try {
            transaction.sign(privateKey);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return transaction;
    }
}