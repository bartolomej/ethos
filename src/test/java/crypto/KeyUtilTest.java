package crypto;

import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

public class KeyUtilTest {

    @Test
    public void generate() {
        KeyUtil keys = KeyUtil.generate();
        PrivateKey privateKey = keys.getPrivateKey();
        PublicKey publicKey = keys.getPublicKey();
        // TODO: test deterministically
        //assertEquals(1216, ByteUtil.toHexString(privateKey.getEncoded()).length());
        //assertEquals(1216, ByteUtil.toHexString(publicKey.getEncoded()).length());
    }

    @Test
    public void parsePublicKey() {
    }

    @Test
    public void parsePrivateKey() {
    }

    @Test
    public void generateRSAKeys() {
    }

    @Test
    public void generateECCKeys() {
    }
}