package core;

import core.block.AbstractBlock;
import core.block.Block;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Chain {

    private AbstractBlock parentBlock; // if exists
    private ArrayList<AbstractBlock> chain = new ArrayList<>();
    private Map<byte[], AbstractBlock> index = new HashMap<>();
    private long difficulty;

    public Chain(AbstractBlock parent) {
        this.parentBlock = parent;
    }

    public Chain() {};

    public void add(AbstractBlock block) {
        chain.add(block);
        index.put(block.getHash(), block);
    }

    // if null this is main chain
    public AbstractBlock getRootParent() {
        return parentBlock;
    }

    public AbstractBlock get(int index) {
        return chain.get(index);
    }

    public AbstractBlock getLast() {
        return chain.get(chain.size() - 1);
    }

    public ArrayList<AbstractBlock> getChain() {
        return this.chain;
    }

    public int getSize() {
        return chain.size();
    }

    public long getDifficulty() {
        return difficulty;
    }

}
