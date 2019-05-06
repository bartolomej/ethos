package core;

import java.util.ArrayList;

public class Block {

    private int difficulty;
    private int nonce;
    private int number;
    private long timestamp;
    private String miner;
    private String hash;
    private String parentHash;
    private String stateRoot;
    private ArrayList<Transaction> transactions;
    private String transactionsRoot;

}
