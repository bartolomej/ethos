package core;

import config.Constants;
import core.block.Block;
import core.block.GenesisBlock;
import core.transaction.*;
import crypto.HashUtil;
import crypto.KeyUtil;
import crypto.SigUtil;
import db.DbFacade;
import org.junit.Test;
import util.ByteUtil;

import java.io.File;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class HelperGenerator {

    Chain chain;
    Block block;
    CoinbaseTransaction coinbaseTransaction;
    Transaction transaction;
    ArrayList<TxOutput> coinbaseOutputs;
    ArrayList<TxInput> inputsTx1;
    ArrayList<TxOutput> outputsTx1;

    private PrivateKey privateKey1;
    private PublicKey address1;
    private PrivateKey privateKey2;
    private PublicKey address2;

    public HelperGenerator() throws InvalidKeySpecException, InvalidKeyException {
        KeyUtil keyPairAccount1 = KeyUtil.generate();
        privateKey1 = keyPairAccount1.getPrivateKey();
        address1 = keyPairAccount1.getPublicKey();
        KeyUtil keyPairAccount2 = KeyUtil.generate();
        privateKey2 = keyPairAccount2.getPrivateKey();
        address2 = keyPairAccount2.getPublicKey();

        this.chain = new Chain();

        this.init();
    }

    public static void generateDbFolderStructure() {
        DbFacade.init();
    }

    public ArrayList<TxOutput> getCoinbaseOutputs() {
        return this.coinbaseOutputs;
    }

    public ArrayList<TxInput> getInputsTx1() {
        return this.inputsTx1;
    }

    public ArrayList<TxOutput> getOutputsTx1() {
        return this.outputsTx1;
    }

    public CoinbaseTransaction getCoinbaseTransaction() {
        return this.coinbaseTransaction;
    }

    public Transaction getTransaction() {
        return this.transaction;
    }

    public Block getBlock() {
        return this.block;
    }

    public Chain getChain() {
        return this.chain;
    }

    public void init() throws InvalidKeySpecException, InvalidKeyException {
        GenesisBlock genesisBlock = GenesisBlock.generate(address1.getEncoded(), 3);
        while (!genesisBlock.valid()) {
            genesisBlock.computeHash();
        }
        genesisBlock.addCoinbaseTransaction();
        this.chain.add(genesisBlock);

        this.coinbaseTransaction = CoinbaseTransaction.generate(address1.getEncoded());

        this.inputsTx1 = new ArrayList<>();

        byte[] sigData = (ByteUtil.toHexString(coinbaseTransaction.getHash()) + coinbaseTransaction.getOutput().getOutputIndex()).getBytes();
        byte[] sig = SigUtil.sign(privateKey1, sigData);

        this.inputsTx1.add(new TxInput(sig, coinbaseTransaction.getHash(), coinbaseTransaction.getOutput()));
        this.outputsTx1 = new ArrayList<>();
        this.outputsTx1.add(new TxOutput(address2.getEncoded(), 10, 0)); // address1 balance: 100 (BLOCK_REWARD) - 10 - 10

        long timestamp = System.currentTimeMillis();
        String txHeaderData = timestamp + outputsTx1.toString() + inputsTx1.toString() + ByteUtil.toHexString(address1.getEncoded());
        byte[] tx1Signature = SigUtil.sign(privateKey1, txHeaderData.getBytes());
        byte[] tx1Hash = HashUtil.sha256(txHeaderData.getBytes());

        this.transaction = new Transaction(inputsTx1, outputsTx1, address1.getEncoded(), tx1Signature, tx1Hash, timestamp);

        this.block = new Block(genesisBlock.getHash(), address1.getEncoded(), 3, 1);
        this.block.addTransaction(this.transaction);
        while (!block.valid()) {
            block.computeHash();
        }
        this.block.addCoinbaseTransaction();

        this.chain.add(block);
    }
}
