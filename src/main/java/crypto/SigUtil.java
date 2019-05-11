package crypto;

import java.security.*;

public class SigUtil {

    public static byte[] sign(PrivateKey privateKey, byte[] content) throws InvalidKeyException {
        byte[] signature = null;
        try {
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initSign(privateKey);
            sign.update(content);
            signature = sign.sign();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return signature;
    }

    public static boolean verify(PublicKey publicKey, byte[] signature, byte[] data) throws SignatureException, InvalidKeyException {
        boolean isValid = false;
        try {
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initVerify(publicKey);
            sign.update(data);
            isValid = sign.verify(signature);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return isValid;
    }
}
