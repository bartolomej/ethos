package core;

import java.security.PublicKey;

public class Account {

    private long balance;
    private String address;
    private PublicKey publicKey;

    /* 256 bit hash of the root node of trie
     * that encodes storage content of the account */
    private byte[] stateRoot;

    public Account() {

    }
}
