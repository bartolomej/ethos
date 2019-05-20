package core;

import config.Constants;
import core.block.Block;
import core.transaction.Transaction;
import core.transaction.TxInput;
import core.transaction.TxOutput;
import crypto.KeyUtil;
import db.BlockStore;
import db.TransactionStore;
import org.junit.Test;
import util.ByteUtil;

import java.io.File;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class StateManagerTest {

    @Test
    public void storeWholeBlock() throws InvalidKeySpecException {
        KeyUtil keys1 = KeyUtil.generate();
        PublicKey pubKey1 = keys1.getPublicKey();


        byte[] prevBlockHash = new byte[]{0,0,0,0,0,0,0};
        byte[] blockHash = new byte[]{1,1,1,1,1,1,1};
        byte[] txRoot = new byte[]{2,2,2,2,2,2,2};
        byte[] miner = new byte[]{3,3,3,3,3,3};
        long timestamp = 1000000;
        int difficulty = 3;
        int index = 23;

        Block block = new Block(blockHash, prevBlockHash, txRoot, miner, timestamp, difficulty, index);

        byte[] input1Sig = new byte[]{1,1,1,1,1};
        byte[] input1Hash = new byte[]{2,2,2,2,2};
        byte[] input1PrevOutHash = new byte[]{3,3,3,3,3};
        int input1PrevOutIndex = 2;
        long input1Value = 1000;

        ArrayList<TxInput> inputsTx1 = new ArrayList<>();
        inputsTx1.add(new TxInput(input1Sig, input1Hash, input1PrevOutHash, input1PrevOutIndex, input1Value));

        byte[] output1RecipientPubKeyTx1 = new byte[]{4,4,4,4,4,4};
        long output1Value = 100;
        int output1Index = 0;

        ArrayList<TxOutput> outputsTx1 = new ArrayList<>();
        outputsTx1.add(new TxOutput(output1RecipientPubKeyTx1, output1Value, output1Index));

        byte[] sig1 = new byte[]{5,5,5,5,5,5};
        byte[] txHash1 = new byte[]{6,6,6,6,6};
        Transaction tx1 = new Transaction(inputsTx1, outputsTx1, pubKey1.getEncoded(), sig1, txHash1, timestamp);

        block.addTransaction(tx1);

        StateManager.saveBlock(block);

        Block fullStoredBlock = StateManager.getBlock(block.getHash());

        assertTrue(block.equals(fullStoredBlock));
        assertTrue(block.getTransactions().get(0).equals(fullStoredBlock.getTransactions().get(0)));

        String blockPath = Constants.BLOCK_STORE_DIR + ByteUtil.toHexString(block.getHash()) + ".json";
        String tx1Path = Constants.TX_STORE_DIR + ByteUtil.toHexString(tx1.getHash()) + ".json";
        String txRootPath = Constants.INDEX_STORE_DIR + ByteUtil.toHexString(block.getTransactionsRootHash()) + ".json";

        removeFile(blockPath);
        removeFile(tx1Path);
        removeFile(txRootPath);
    }

    private void removeFile(String path) {
        new File(path).delete();
    }

}