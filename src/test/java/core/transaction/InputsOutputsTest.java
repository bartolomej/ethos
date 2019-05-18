package core.transaction;

import crypto.KeyUtil;
import crypto.SigUtil;
import org.junit.Test;
import org.junit.runners.Parameterized;
import util.ByteUtil;
import static org.junit.Assert.*;

import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Collection;

public class InputsOutputsTest {

    @Test
    public void validOutputReference() throws InvalidKeyException {
        byte[] txHash = new byte[]{0,0,0,0,0,0};
        KeyUtil keys = KeyUtil.generate();
        PublicKey publicKey = keys.getPublicKey();
        PrivateKey privateKey = keys.getPrivateKey();

        TxOutput txOutput1 = new TxOutput(publicKey.getEncoded(), 10, 0);

        byte[] sig = SigUtil.sign(privateKey, txOutput1.getHashValue());
        TxInput txInput1 = new TxInput(sig, txHash, txOutput1);

        assertTrue(txOutput1.valid());
        assertTrue(txInput1.valid());
    }

    @Test
    public void invalidOutputReference() throws InvalidKeyException {
        byte[] txHash = new byte[]{0,0,0,0,0,0};
        // first key pair
        KeyUtil keys1 = KeyUtil.generate();
        PublicKey publicKey1 = keys1.getPublicKey();
        PrivateKey privateKey1 = keys1.getPrivateKey();
        // second key pair
        KeyUtil keys2 = KeyUtil.generate();
        PublicKey publicKey2 = keys2.getPublicKey();
        PrivateKey privateKey2 = keys2.getPrivateKey();

        TxOutput txOutput1 = new TxOutput(publicKey1.getEncoded(), 10, 0);

        byte[] sig = SigUtil.sign(privateKey2, txOutput1.getHashValue());
        TxInput txInput1 = new TxInput(sig, txHash, txOutput1);

        assertTrue(txOutput1.valid());
        assertFalse(txInput1.valid());
    }

    @Test
    public void validInputOutputChain() throws InvalidKeyException {
        // test transaction hash id
        byte[] txHash1 = new byte[]{0,0,0,0,0,0};
        byte[] txHash2 = new byte[]{0,0,0,0,0,0};
        // first key pair
        KeyUtil keys1 = KeyUtil.generate();
        PublicKey publicKey1 = keys1.getPublicKey();
        PrivateKey privateKey1 = keys1.getPrivateKey();
        // second key pair
        KeyUtil keys2 = KeyUtil.generate();
        PublicKey publicKey2 = keys2.getPublicKey();
        PrivateKey privateKey2 = keys2.getPrivateKey();

        // first transaction outputs (coinbase transaction)
        TxOutput txOutput1Tx1 = new TxOutput(publicKey1.getEncoded(), 10, 0);
        TxOutput txOutput2Tx1 = new TxOutput(publicKey2.getEncoded(), 10, 0);

        byte[] sigOutput1Tx1 = SigUtil.sign(privateKey1, txOutput1Tx1.getHashValue());
        byte[] sigOutput2Tx1 = SigUtil.sign(privateKey2, txOutput2Tx1.getHashValue());

        // second transaction inputs / outputs
        TxInput txInput1Tx2 = new TxInput(sigOutput1Tx1, txHash1, txOutput1Tx1);
        TxInput txInput2Tx2 = new TxInput(sigOutput2Tx1, txHash1, txOutput2Tx1);

        TxOutput txOutput1Tx2 = new TxOutput(publicKey1.getEncoded(), 5, 0);
        TxOutput txOutput2Tx2 = new TxOutput(publicKey2.getEncoded(), 5, 0);

        byte[] sigOutput1Tx2 = SigUtil.sign(privateKey1, txOutput1Tx1.getHashValue());
        byte[] sigOutput2Tx2 = SigUtil.sign(privateKey2, txOutput2Tx1.getHashValue());

        // third transaction inputs / outputs
        TxInput txInput1Tx3 = new TxInput(sigOutput1Tx2, txHash2, txOutput1Tx1);
        TxInput txInput2Tx3 = new TxInput(sigOutput2Tx2, txHash2, txOutput2Tx1);


        assertTrue(txOutput1Tx1.valid());
        assertTrue(txInput1Tx2.valid());

        assertTrue(txOutput2Tx1.valid());
        assertTrue(txInput2Tx2.valid());

        assertTrue(txOutput1Tx2.valid());
        assertTrue(txOutput2Tx2.valid());

        assertTrue(txInput1Tx3.valid());
        assertTrue(txInput2Tx3.valid());
    }
}
