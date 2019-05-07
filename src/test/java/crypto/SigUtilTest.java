package crypto;

import org.junit.Test;

import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.Assert.*;

public class SigUtilTest {

    @Test
    public void sign() {
        KeyUtil keys = KeyUtil.generate();
        PrivateKey privateKey = keys.getPrivateKey();

        String content = "TEST MESSAGE";
        try {
            byte[] signed = SigUtil.sign(privateKey, content.getBytes());
            assertTrue(signed != null);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void verify() {
        KeyUtil keys = KeyUtil.generate();
        PrivateKey privateKey = keys.getPrivateKey();
        PublicKey publicKey = keys.getPublicKey();

        String content = "TEST MESSAGE";
        try {
            byte[] signed = SigUtil.sign(privateKey, content.getBytes());
            boolean isValid = SigUtil.verify(publicKey, signed, content.getBytes());
            assertTrue(isValid);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}