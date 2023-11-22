package Replacement_Policy;

import java.util.LinkedList;
import java.util.Queue;

public class FIFOReplacementPolicy implements ReplacementPolicy {
    private Queue<Integer> accessOrder;

    public FIFOReplacementPolicy() {
        this.accessOrder = new LinkedList<>();
    }

    @Override
    public void accessed(int lineIndex) {
        // FIFO doesn't change order upon access
    }

    @Override
    public int chooseLineToReplace() {
        if (!accessOrder.isEmpty()) {
            return accessOrder.poll();
        } else {
            return -1; // Example: Return -1 to indicate an issue
        }
    }
}

