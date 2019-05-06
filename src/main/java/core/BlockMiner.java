package core;

public class BlockMiner {

    // TODO: method for current mining block cancellation
    // TODO: method for starting next best block from pool
    // TODO: add task queue

    /*
    BLOCK HASH PARAMS:
    -> version
    -> Previous block hash(“prev_block”)
    -> merkle root
    -> timestamp
    -> difficulty bits
    -> nonce
     */

    public String difficulty = "00000001";

    public void startMinint() {};

    public void restartMining() {};

    public void mineNextBlock() {};

    //private Block getBlock() {};
}
