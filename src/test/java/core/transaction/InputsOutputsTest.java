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
        byte[] prevTxHash = new byte[]{0,0,0,0,0,0};
        KeyUtil keys = KeyUtil.generate();
        PublicKey publicKey = keys.getPublicKey();
        PrivateKey privateKey = keys.getPrivateKey();

        TxOutput txOutput1 = new TxOutput(publicKey.getEncoded(), 10, 0);

        byte[] sig = SigUtil.sign(privateKey,
                (ByteUtil.toHexString(prevTxHash) + txOutput1.getOutputIndex()).getBytes());
        TxInput txInput1 = new TxInput(sig, prevTxHash, txOutput1);

        assertTrue(txOutput1.valid());
        assertTrue(txInput1.valid());
    }

    @Test
    public void validInputOutputChain() throws InvalidKeyException {
        // test transaction hash id
        byte[] txHash1 = new byte[]{0,0,0,0,0,0};
        byte[] txHash2 = new byte[]{1,1,1,1,1,1};
        // first key pair
        KeyUtil keys1 = KeyUtil.generate();
        PublicKey address1 = keys1.getPublicKey();
        PrivateKey privateKey1 = keys1.getPrivateKey();
        // second key pair
        KeyUtil keys2 = KeyUtil.generate();
        PublicKey address2 = keys2.getPublicKey();
        PrivateKey privateKey2 = keys2.getPrivateKey();

        // FIRST TRANSACTION
        TxOutput txOutput1Tx1 = new TxOutput(address1.getEncoded(), 10, 0); // send 10 to address1
        TxOutput txOutput2Tx1 = new TxOutput(address2.getEncoded(), 10, 0); // send 10 to address2

        byte[] sigData1 = (ByteUtil.toHexString(txHash1) + txOutput1Tx1.getOutputIndex()).getBytes();
        byte[] sigData2 = (ByteUtil.toHexString(txHash1) + txOutput1Tx1.getOutputIndex()).getBytes();

        byte[] sigInput1Tx1 = SigUtil.sign(privateKey1, sigData1);
        byte[] sigInput2Tx1 = SigUtil.sign(privateKey2, sigData2);

        // SECOND TRANSACTION
        TxInput txInput1Tx2 = new TxInput(sigInput1Tx1, txHash1, txOutput1Tx1);
        TxInput txInput2Tx2 = new TxInput(sigInput2Tx1, txHash1, txOutput2Tx1);

        TxOutput txOutput1Tx2 = new TxOutput(address1.getEncoded(), 5, 0); // send 5 to address1
        TxOutput txOutput2Tx2 = new TxOutput(address2.getEncoded(), 5, 0); // send 5 to address2

        byte[] sigData3 =  (ByteUtil.toHexString(txHash2) + txOutput2Tx1.getOutputIndex()).getBytes();
        byte[] sigData4 = (ByteUtil.toHexString(txHash2) + txOutput2Tx2.getOutputIndex()).getBytes();

        byte[] sigOutput1Tx2 = SigUtil.sign(privateKey1, sigData3);
        byte[] sigOutput2Tx2 = SigUtil.sign(privateKey2, sigData4);

        // THIRD TRANSACTION
        TxInput txInput1Tx3 = new TxInput(sigOutput1Tx2, txHash2, txOutput1Tx2);
        TxInput txInput2Tx3 = new TxInput(sigOutput2Tx2, txHash2, txOutput2Tx2);


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
