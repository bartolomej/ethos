package crypto;

import org.junit.Test;
import util.BytesUtil;

import static org.junit.Assert.*;

public class HashUtilTest {

    @Test
    public void shaByteArrayToHexStringSmall() {
        byte[] byteArray = new byte[]{1,2,3};
        String hexString = BytesUtil.toHexString(byteArray);
        assertEquals("010203", hexString);
        assertArrayEquals(byteArray, BytesUtil.toByteArray(hexString));
    }

    @Test
    public void shaByteArrayToHexStringBig() {
        byte[] byteArray = new byte[]{100,120,3};
        String hexString = BytesUtil.toHexString(byteArray);
        assertEquals("647803", hexString);
        assertArrayEquals(byteArray, BytesUtil.toByteArray(hexString));
    }

    @Test
    public void sha256HashTest() {
        byte[] hash = HashUtil.sha256("testMSG");
        assertEquals(32, hash.length);
    }

    @Test
    public void sha256ToString() {
        byte[] hash = HashUtil.sha256("testMSG");
        String hexString = BytesUtil.toHexString(hash);
        assertEquals(hexString.length(), 64);
    }

}