package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Chain {

    private static ArrayList<Block> chain = new ArrayList<>();
    private static Map<byte[], Block> index = new HashMap<>();
    private static long difficulty;

    public static void add(Block block) {
        if (!block.valid()) return;
        chain.add(block);
        index.put(block.getHash(), block);
    }

    public static Block get(int index) {
        return chain.get(index);
    }

    public Block getLast() {
        return chain.get(chain.size() - 1);
    }

    public int getSize() {
        return chain.size();
    }

    public long getDifficulty() {
        return difficulty;
    }

}
