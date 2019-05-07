package crypto;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyUtil {

    private String publicKey;
    private String privateKey;
    private PublicKey publicKeyRSA;
    private PrivateKey privateKeyRSA;

    public static KeyUtil generate() {
        KeyUtil instance = new KeyUtil();
        instance.generateRSAKeys();
        return instance;
    }

    public PublicKey getPublicKey() {
        return this.publicKeyRSA;
    }

    public PrivateKey getPrivateKey() {
        return this.privateKeyRSA;
    }

    public static PublicKey parsePublicKey(String storedPublic) throws InvalidKeySpecException {
        PublicKey publicKey = null;
        try {
            byte[] data = Base64.getDecoder().decode((storedPublic.getBytes()));
            X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            publicKey = fact.generatePublic(spec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return publicKey;
    }

    public static PrivateKey parsePrivateKey(String storedPrivate) throws InvalidKeySpecException {
        PrivateKey privateKey = null;
        try {
            byte[] data = Base64.getDecoder().decode((storedPrivate.getBytes()));
            X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            privateKey = fact.generatePrivate(spec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return privateKey;
    }

    public void generateRSAKeys() {
        try {
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("DSA");
            keyPairGen.initialize(2048);
            KeyPair pair = keyPairGen.generateKeyPair();
            this.privateKeyRSA = pair.getPrivate();
            this.publicKeyRSA = pair.getPublic();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }

}
