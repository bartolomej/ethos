package core;

import crypto.KeyUtil;
import org.json.JSONObject;
import util.ByteUtil;

import java.security.PrivateKey;
import java.security.PublicKey;

public class Account {

    private static int ADDRESS_LENGTH = 20;

    private String address;
    private long timestamp;
    private PublicKey publicKey;
    private PrivateKey privateKey; // not persistable

    /* 256 bit hash of the root node of trie
     * that encodes storage content of the account */
    private byte[] stateRoot;

    public Account() {
        this.timestamp = System.currentTimeMillis();
        this.generateKeys();
    }

    private void generateKeys() {
        KeyUtil keys = KeyUtil.generate();
        this.publicKey = keys.getPublicKey();
        this.privateKey = keys.getPrivateKey();
        this.address = ByteUtil.toHexString(
                this.publicKey.getEncoded())
                .substring(0, ADDRESS_LENGTH
        );
    }

    public String toString() {
        return this.toStringWithSuffix("\n");
    }

    private String toStringWithSuffix(String suffix) {
        return (
                "TIMESTAMP: " + this.timestamp + suffix +
                "ADDRESS: " + this.address + suffix +
                "PUBLIC_KEY: " + ByteUtil.toHexString(this.publicKey.getEncoded()) + suffix +
                "PRIVATE_KEY: " + ByteUtil.toHexString(this.privateKey.getEncoded()) + suffix
        );
    }

    public JSONObject toJson() {
        String json = String.format("{timestamp: %s, address: %s, privateKey: %s, publicKey: %s}",
                this.timestamp, this.address,
                ByteUtil.toHexString(this.privateKey.getEncoded()),
                ByteUtil.toHexString(this.publicKey.getEncoded()));
        return new JSONObject(json);
    }
}
