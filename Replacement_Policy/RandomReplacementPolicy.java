package Replacement_Policy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RandomReplacementPolicy implements ReplacementPolicy {
    private List<Integer> cacheLines;

    public RandomReplacementPolicy(int cacheSize) {
        this.cacheLines = new ArrayList<>(cacheSize);
        for (int i = 0; i < cacheSize; i++) {
            cacheLines.add(i);
        }
        Collections.shuffle(cacheLines);
    }

    @Override
    public void accessed(int lineIndex) {
        // Random doesn't change order upon access
    }

    @Override
    public int chooseLineToReplace() {
        return cacheLines.remove(0);
    }
}

