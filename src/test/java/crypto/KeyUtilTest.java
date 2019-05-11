package crypto;

import org.junit.Test;

import static org.junit.Assert.*;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;

public class KeyUtilTest {

    @Test
    public void generateAndParse() throws InvalidKeySpecException {
        KeyUtil keys = KeyUtil.generate();
        PrivateKey privateKey = keys.getPrivateKey();
        PublicKey publicKey = keys.getPublicKey();

        PrivateKey decodedPrivate = KeyUtil.parsePrivateKey(privateKey.getEncoded());
        PublicKey decodedPublic = KeyUtil.parsePublicKey(publicKey.getEncoded());

        assertArrayEquals(decodedPrivate.getEncoded(), privateKey.getEncoded());
        assertArrayEquals(decodedPublic.getEncoded(), publicKey.getEncoded());
    }

}