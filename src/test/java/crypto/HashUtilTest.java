package crypto;

import org.junit.Test;
import util.ByteUtil;

import static org.junit.Assert.*;

public class HashUtilTest {

    @Test
    public void shaByteArrayToHexStringSmall() {
        byte[] byteArray = new byte[]{1,2,3};
        String hexString = ByteUtil.toHexString(byteArray);
        assertEquals("010203", hexString);
        assertArrayEquals(byteArray, ByteUtil.toByteArray(hexString));
    }

    @Test
    public void shaByteArrayToHexStringBig() {
        byte[] byteArray = new byte[]{100,120,3};
        String hexString = ByteUtil.toHexString(byteArray);
        assertEquals("647803", hexString);
        assertArrayEquals(byteArray, ByteUtil.toByteArray(hexString));
    }

    @Test
    public void byteArrayToBase64() {
        byte[] byteArray = new byte[]{100,120,3};
        String hexString = ByteUtil.encodeToBase64(byteArray);
        assertArrayEquals(byteArray, ByteUtil.decodeFromBase64(hexString));
    }

    @Test
    public void encodePubKeyToBase64() {
        KeyUtil keyUtil = KeyUtil.generate();
        byte[] pubKey = keyUtil.getPublicKey().getEncoded();
        String encoded = ByteUtil.encodeToBase64(pubKey);
        byte[] decoded = ByteUtil.decodeFromBase64(encoded);
        assertArrayEquals(decoded, pubKey);
    }

    @Test
    public void sha256HashTest() {
        byte[] hash = HashUtil.sha256("testMSG".getBytes());
        assertEquals(32, hash.length);
    }

    @Test
    public void sha256ToString() {
        byte[] hash = HashUtil.sha256("testMSG".getBytes());
        String hexString = ByteUtil.toHexString(hash);
        assertEquals(hexString.length(), 64);
        assertEquals(hash.length, 32);
    }

}