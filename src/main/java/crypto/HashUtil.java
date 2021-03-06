package crypto;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashUtil {

    public static byte[] sha256(byte[] message) {
        if (message == null) return new byte[]{};
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(message);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return messageDigest.digest();
    }
}
