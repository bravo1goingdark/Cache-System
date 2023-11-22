package Replacement_Policy;

import java.util.HashMap;
import java.util.Map;

public class CacheWithReplacementPolicies {
    private int cacheSize;
    private int blockSize;
    private int blockCount;
    private int cacheHits;
    private int cacheMisses;
    private Map<Integer, CacheLine> cache;
    private ReplacementPolicy replacementPolicy;
    private int hitTime; // Time taken for a cache hit in nanoseconds
    private int missPenalty; // Time penalty for a cache miss in nanoseconds

    public CacheWithReplacementPolicies(int cacheSize, int blockSize, ReplacementPolicy policy, int hitTime, int missPenalty) {
        this.cacheSize = cacheSize;
        this.blockSize = blockSize;
        this.blockCount = cacheSize / blockSize;
        this.cache = new HashMap<>();
        this.cacheHits = 0;
        this.cacheMisses = 0;
        this.replacementPolicy = policy;
        this.hitTime = hitTime;
        this.missPenalty = missPenalty;

        // Initialize cache lines
        for (int i = 0; i < blockCount; i++) {
            cache.put(i, new CacheLine());
        }
    }

    public void setReplacementPolicy(ReplacementPolicy policy) {
        this.replacementPolicy = policy;
    }

    public void write(int address, int data) {
        int blockNumber = address / blockSize;
        int cacheLineIndex = blockNumber % blockCount;
    
        CacheLine cacheLine = cache.get(cacheLineIndex);
    
        if (cacheLine.getTag() == blockNumber) {
            // Cache hit
            cacheHits++;
            cacheLine.setData(data);
            replacementPolicy.accessed(cacheLineIndex);
            System.out.println("Write - Cache Hit: Address " + address + ", Data: " + data);
        } else {
            // Cache miss, perform replacement
            cacheMisses++;
            int replacedLineIndex = replacementPolicy.chooseLineToReplace();
    
            if (replacedLineIndex != -1) {
                CacheLine replacedLine = cache.get(replacedLineIndex);
                System.out.println("Write - Cache Miss: Evicting Block " + replacedLine.getTag() +
                        " from Cache Line " + replacedLineIndex);
    
                // Insert the new block
                cacheLine.setTag(blockNumber);
                cacheLine.setData(data);
                replacementPolicy.accessed(cacheLineIndex);
                System.out.println("Write - Cache Miss: Address " + address + ", Data: " + data);
            } else {
                System.out.println("Write - Cache Miss: Unable to replace line. Cache may be full.");
            }
        }
    }
    

    public int read(int address) {
        int blockNumber = address / blockSize;
        int cacheLineIndex = blockNumber % blockCount;

        CacheLine cacheLine = cache.get(cacheLineIndex);

        if (cacheLine.getTag() == blockNumber) {
            // Cache hit
            cacheHits++;
            replacementPolicy.accessed(cacheLineIndex);
            System.out.println("Read - Cache Hit: Address " + address + ", Data: " + cacheLine.getData());
            return cacheLine.getData();
        } else {
            // Cache miss
            cacheMisses++;
            System.out.println("Read - Cache Miss: Address " + address + ", Data not in Cache");
            return -1; // Data not in cache
        }
    }

    public double calculateEffectiveTime() {
        int totalAccesses = cacheHits + cacheMisses;
        double hitRate = (double) cacheHits / totalAccesses;
        double missRate = (double) cacheMisses / totalAccesses;

        double effectiveTime = hitRate * hitTime + missRate * (hitTime + missPenalty);
        return effectiveTime;
    }

    public int getCacheHits() {
        return cacheHits;
    }

    public int getCacheMisses() {
        return cacheMisses;
    }

    private static class CacheLine {
        private int tag;
        private int data;

        public int getTag() {
            return tag;
        }

        public void setTag(int tag) {
            this.tag = tag;
        }

        public int getData() {
            return data;
        }

        public void setData(int data) {
            this.data = data;
        }
    }

    public static void main(String[] args) {
        int cacheSize = 16;
        int blockSize = 4;
        int hitTime = 2;
        int missPenalty = 10;

        CacheWithReplacementPolicies cache = new CacheWithReplacementPolicies(cacheSize, blockSize, new LRUReplacementPolicy(), hitTime, missPenalty);

        // Simulating reads and writes
        cache.write(0, 10);
        cache.write(4, 20);
        cache.write(8, 30);

        cache.read(4);
        cache.read(12); // Should result in a cache miss

        // Print cache hits and misses
        System.out.println("Cache Hits: " + cache.getCacheHits());
        System.out.println("Cache Misses: " + cache.getCacheMisses());

        // Calculate and print the effective time
        System.out.println("Total Effective Time: " + cache.calculateEffectiveTime() + " nanoseconds");

        // Switching to FIFO replacement policy
        cache.setReplacementPolicy(new FIFOReplacementPolicy());

        // Simulating reads and writes with FIFO policy
        cache.write(12, 40);
        cache.write(16, 50);
        cache.write(20, 60);

        // Print cache hits and misses with FIFO policy
        System.out.println("Cache Hits: " + cache.getCacheHits());
        System.out.println("Cache Misses: " + cache.getCacheMisses());

        // Calculate and print the effective time with FIFO policy
        System.out.println("Total Effective Time: " + cache.calculateEffectiveTime() + " nanoseconds");
    }
}
