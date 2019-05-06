package crypto;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class KeyGen {

    private String publicKey;
    private String privateKey;
    private PublicKey publicKeyRSA;
    private PrivateKey privateKeyRSA;

    public static KeyGen generate() {
        KeyGen instance = new KeyGen();
        instance.generateECCKeys();
        return instance;
    }

    public static Key parsePublicKey(String storedPublic) throws InvalidKeySpecException {
        Key pubKey = null;
        try {
            byte[] data = Base64.getDecoder().decode((storedPublic.getBytes()));
            X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            pubKey = fact.generatePublic(spec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return pubKey;
    }

    public static Key parsePrivateKey(String storedPrivate) throws InvalidKeySpecException {
        Key privKey = null;
        try {
            byte[] data = Base64.getDecoder().decode((storedPrivate.getBytes()));
            X509EncodedKeySpec spec = new X509EncodedKeySpec(data);
            KeyFactory fact = KeyFactory.getInstance("RSA");
            privKey = fact.generatePrivate(spec);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return privKey;
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

    public void generateECCKeys() {
        Security.addProvider(new BouncyCastleProvider());

        try {
            KeyPairGenerator ecKeyGen = KeyPairGenerator.getInstance("EC", BouncyCastleProvider.PROVIDER_NAME);
            ecKeyGen.initialize(new ECGenParameterSpec("brainpoolP384r1"));

            // doesn't work, which means we are dancing on the leading edge :)
            // KeyPairGenerator ecKeyGen = KeyPairGenerator.getInstance("EC");
            // ecKeyGen.initialize(new ECGenParameterSpec("secp384r1"));

            KeyPair ecKeyPair = ecKeyGen.generateKeyPair();
            System.out.println("What is slow?");

            Cipher iesCipher = Cipher.getInstance("ECIESwithAES");
            iesCipher.init(Cipher.ENCRYPT_MODE, ecKeyPair.getPublic());


            iesCipher.init(Cipher.DECRYPT_MODE, ecKeyPair.getPrivate());

            // TODO private / public key gen

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
