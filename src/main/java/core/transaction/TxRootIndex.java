package core.transaction;

import org.json.JSONArray;
import org.json.JSONObject;
import util.ByteUtil;

import java.util.ArrayList;

public class TxRootIndex {

    // simple test implementation of transactional index data structure
    // experiment with merkle tree implementation

    byte[] blockHash;
    byte[][] txHashes;
    byte[] rootHash;
    ArrayList<String> encodedTxHashes;
    ArrayList<AbstractTransaction> transactions;

    public TxRootIndex(byte[] blockHash, byte[][] txHashes) {
        encodedTxHashes = new ArrayList<>();
        this.blockHash = blockHash;
        this.txHashes = txHashes;
        this.rootHash = computeRoot(txHashes);
        this.encodeTx();
    }

    public TxRootIndex(byte[] blockHash, ArrayList<AbstractTransaction> transactions) {
        encodedTxHashes = new ArrayList<>();
        this.blockHash = blockHash;
        this.transactions = transactions;
        this.txHashes = new byte[transactions.size()][];
        this.rootHash = computeRoot(txHashes);
        this.parseTx();
        this.encodeTx();
    }

    private byte[] computeRoot(byte[][] txHashes) {
        // TODO: compute root => transform to 1d array and apply hash function
        return new byte[]{0,0,0,0};
    }

    private void parseTx() {
        for (int i = 0; i < this.transactions.size(); i++) {
            txHashes[i] = this.transactions.get(i).getHash();
        }
    }

    private void encodeTx() {
        for (int i = 0; i < this.txHashes.length; i++) {
            encodedTxHashes.add(ByteUtil.encodeToBase64(txHashes[i]));
        }
    }

    public boolean contains(byte[] txHash) {
        for (byte[] hash : txHashes) {
            if (ByteUtil.arraysEqual(hash, txHash))
                return true;
        }
        return false;
    }

    public boolean equals(TxRootIndex txRootIndex) {
        for (int i = 0; i < this.txHashes.length; i++) {
            if (!ByteUtil.arraysEqual(txRootIndex.txHashes[i], txHashes[i]))
                return false;
        }
        return ByteUtil.arraysEqual(this.blockHash, txRootIndex.blockHash);
    }

    public byte[] getBlockHash() {
        return this.blockHash;
    }

    public byte[][] getTxHashes() {
        return this.txHashes;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("block_hash", ByteUtil.encodeToBase64(this.blockHash));
        jsonObject.put("tx_root", ByteUtil.encodeToBase64(this.rootHash));
        jsonObject.put("transactions", encodedTxHashes);
        return jsonObject;
    }

}
