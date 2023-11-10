package Replacement_Policy;

import java.util.LinkedList;

public class LRUReplacementPolicy implements ReplacementPolicy {
    private LinkedList<Integer> accessOrder;

    public LRUReplacementPolicy() {
        this.accessOrder = new LinkedList<>();
    }

    @Override
    public void accessed(int lineIndex) {
        accessOrder.removeFirstOccurrence(lineIndex);
        accessOrder.addFirst(lineIndex);
    }

    @Override
    public int chooseLineToReplace() {
        return accessOrder.getLast();
    }
}

