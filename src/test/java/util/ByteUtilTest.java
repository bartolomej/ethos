package util;

import crypto.KeyUtil;
import org.junit.Test;

import java.security.Key;
import java.security.PrivateKey;
import java.security.PublicKey;

import static org.junit.Assert.*;

public class ByteUtilTest {

    @Test
    public void encodePubKeyToBase64() {
        KeyUtil keyUtil = KeyUtil.generate();
        byte[] pubKey = keyUtil.getPublicKey().getEncoded();

        String encoded = ByteUtil.encodeToBase64(pubKey);
        byte[] decoded = ByteUtil.decodeFromBase64(encoded);

        assertArrayEquals(pubKey, decoded);
    }

    @Test
    public void concat1() {
        byte[] array1 = new byte[]{0,0,0,0,0};
        byte[] array2 = new byte[]{1,1,1,1,1};

        byte[] concat = ByteUtil.concat(array1, array2);
        byte[] expected = new byte[]{0,0,0,0,0,1,1,1,1,1};

        assertArrayEquals(expected, concat);
    }

    @Test
    public void concat2() {
        byte[] array1 = new byte[]{1,1,1,1,1,1,1};
        byte[] array2 = new byte[]{2,2,2,2};

        byte[] concat = ByteUtil.concat(array1, array2);
        byte[] expected = new byte[]{1,1,1,1,1,1,1,2,2,2,2};

        assertArrayEquals(expected, concat);
    }

    @Test
    public void concatPubKeys() {
        // first account
        KeyUtil keyPairAccount1 = KeyUtil.generate();
        PublicKey address1 = keyPairAccount1.getPublicKey();
        // second account
        KeyUtil keyPairAccount2 = KeyUtil.generate();
        PublicKey address2 = keyPairAccount2.getPublicKey();

        byte[] concat = ByteUtil.concat(address1.getEncoded(), address2.getEncoded());

        assertEquals(address1.getEncoded().length + address2.getEncoded().length, concat.length);
    }
}