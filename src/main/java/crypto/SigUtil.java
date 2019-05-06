package crypto;

import java.security.*;

public class SigUtil {

    public String sign(PrivateKey privateKey, byte[] content) throws InvalidKeyException {
        byte[] signature = null;
        try {
            Signature sign = Signature.getInstance("SHA256withDSA");
            sign.initSign(privateKey);
            sign.update(content);
            signature = sign.sign();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return new String(signature);
    }

    public boolean verify(PublicKey publicKey, byte[] signature, byte[] data) throws SignatureException, InvalidKeyException {
        boolean isValid = false;
        try {
            Signature sign = Signature.getInstance("SHA256withDSA");
            sign.initVerify(publicKey);
            sign.update(data);
            isValid = sign.verify(signature);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return isValid;
    }
}
