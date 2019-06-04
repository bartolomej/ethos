package core.transaction;

import org.json.JSONObject;
import org.junit.Test;
import util.ByteUtil;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TxRootIndexTest {

    @Test
    public void txRootGeneration() {
        byte[] blockHash = new byte[]{0,0,0,0,0,0};
        byte[][] txHashes = new byte[][]{new byte[]{1,1,1,1,1}, new byte[]{2,2,2,2,2}};
        TxRootIndex txRootIndex = new TxRootIndex(blockHash, txHashes);

        assertTrue(txRootIndex.contains(txHashes[0]));
        assertTrue(txRootIndex.contains(txHashes[1]));
    }

    @Test
    public void txRootParsing() {
        byte[] blockHash = new byte[]{0,0,0,0,0,0};
        byte[][] txHashes = new byte[][]{new byte[]{1,1,1,1,1}, new byte[]{2,2,2,2,2}};
        TxRootIndex txRootIndex = new TxRootIndex(blockHash, txHashes);

        ArrayList<String> expectedTxArray = new ArrayList<>();
        expectedTxArray.add(ByteUtil.encodeToBase64(txHashes[0]));
        expectedTxArray.add(ByteUtil.encodeToBase64(txHashes[1]));

        JSONObject expectedJson = new JSONObject();
        expectedJson.put("tx_root", ByteUtil.encodeToBase64(new byte[]{0,0,0,0})); // TODO: implement
        expectedJson.put("block_hash", ByteUtil.encodeToBase64(blockHash));
        expectedJson.put("transactions", expectedTxArray);

        assertEquals(expectedJson.toString(), txRootIndex.toJson().toString());
    }

}