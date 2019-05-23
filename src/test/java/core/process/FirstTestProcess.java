package core.process;

import core.Blockchain;
import core.Chain;
import core.block.AbstractBlock;
import core.block.Block;
import core.block.GenesisBlock;
import core.transaction.*;
import crypto.HashUtil;
import crypto.KeyUtil;
import crypto.SigUtil;
import org.junit.Test;
import util.ByteUtil;

import static org.junit.Assert.*;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

public class FirstTestProcess {

    /* TEST DESCRIPTION:
     * - account creation
     * - initial tx creation
     * - tx validation
     * - block creation
     * - state calculation
     */

    @Test
    public void firstProcess() throws InvalidKeySpecException, InvalidKeyException {
        // first account
        KeyUtil keyPairAccount1 = KeyUtil.generate();
        PrivateKey privateKey1 = keyPairAccount1.getPrivateKey();
        PublicKey address1 = keyPairAccount1.getPublicKey();
        // second account
        KeyUtil keyPairAccount2 = KeyUtil.generate();
        PrivateKey privateKey2 = keyPairAccount2.getPrivateKey();
        PublicKey address2 = keyPairAccount2.getPublicKey();
        // third account
        KeyUtil keyPairAccount3 = KeyUtil.generate();
        PrivateKey privateKey3 = keyPairAccount3.getPrivateKey();
        PublicKey address3 = keyPairAccount3.getPublicKey();

        // generate global chain state
        Chain chain = new Chain();

        GenesisBlock genesisBlock = GenesisBlock.generate(address1.getEncoded(), 3);
        while (!genesisBlock.valid()) {
            genesisBlock.computeHash();
        }
        genesisBlock.addCoinbaseTransaction();
        chain.add(genesisBlock);

        assertTrue(genesisBlock.valid());
        assertTrue(genesisBlock.getCoinbaseTransaction().valid());

        CoinbaseTransaction coinbaseTx1 = CoinbaseTransaction.generate(address1.getEncoded());
        assertTrue(coinbaseTx1.valid());

        ArrayList<TxInput> inputsTx1 = new ArrayList<>();
        // sign prev transaction output content
        byte[] sigData = (ByteUtil.encodeToBase64(coinbaseTx1.getHash()) + coinbaseTx1.getOutput().getOutputIndex()).getBytes();
        byte[] sig = SigUtil.sign(privateKey1, sigData);
        inputsTx1.add(new TxInput(sig, coinbaseTx1.getHash(), coinbaseTx1.getOutput()));
        ArrayList<TxOutput> outputsTx1 = new ArrayList<>();
        outputsTx1.add(new TxOutput(address2.getEncoded(), 10, 0)); // address1 balance: 100 (BLOCK_REWARD) - 10 - 10

        long timestamp = System.currentTimeMillis();
        String txHeaderData = timestamp + outputsTx1.toString() + inputsTx1.toString() + ByteUtil.encodeToBase64(address1.getEncoded());
        byte[] tx1Signature = SigUtil.sign(privateKey1, txHeaderData.getBytes());
        byte[] tx1Hash = HashUtil.sha256(txHeaderData.getBytes());
        Transaction tx1 = new Transaction(inputsTx1, outputsTx1, address1.getEncoded(), tx1Signature, tx1Hash, timestamp);
        assertTrue(tx1.valid());

        Block block1 = new Block(genesisBlock.getHash(), address1.getEncoded(), 3, 1);
        block1.addTransaction(tx1);
        while (!block1.valid()) {
            block1.computeHash();
        }
        block1.addCoinbaseTransaction();
        assertTrue(block1.valid());

        chain.add(block1);

        assertEquals(chain.getSize(), 2);
    }
}
