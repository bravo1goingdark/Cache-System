package Mapping_Technique;

import java.util.HashMap;
import java.util.Map;

public class FullyAssociativeCache extends Cache {

    private int cacheSize;
    private int blockSize;
    private int cacheLines;
    private int cacheHits;
    private int cacheMisses;
    private Map<Integer, CacheLine> cache;

    public FullyAssociativeCache(int cacheSize, int blockSize) {
        this.cacheSize = cacheSize;
        this.blockSize = blockSize;
        this.cacheLines = cacheSize / blockSize;
        this.cache = new HashMap<>();
        this.cacheHits = 0;
        this.cacheMisses = 0;

        // Initialize cache lines
        for (int i = 0; i < cacheLines; i++) {
            cache.put(i, new CacheLine());
        }
    }

    public void write(int address, int data) {
        int blockNumber = address / blockSize;

        CacheLine cacheLine = cache.get(blockNumber);

        if (cacheLine.write(data)) {
            cacheHits++;
            System.out.println("Write - Cache Hit: Address " + address + ", Data: " + data);
        } else {
            cacheMisses++;
            System.out.println("Write - Cache Miss: Address " + address + ", Data: " + data);
        }
    }

    public int read(int address) {
        int blockNumber = address / blockSize;

        CacheLine cacheLine = cache.get(blockNumber);

        if (cacheLine.read()) {
            cacheHits++;
            System.out.println("Read - Cache Hit: Address " + address + ", Data: " + cacheLine.getData());
            return cacheLine.getData();
        } else {
            cacheMisses++;
            System.out.println("Read - Cache Miss: Address " + address + ", Data not in Cache");
            return -1; // Data not in cache
        }
    }

    public void calculateEffectiveTime(long hitTime, long missPenalty) {
        int totalAccesses = cacheHits + cacheMisses;
        double hitRate = (double) cacheHits / totalAccesses;
        double missRate = (double) cacheMisses / totalAccesses;

        double effectiveTime = hitRate * hitTime + missRate * (hitTime + missPenalty);
        System.out.println(effectiveTime);
    }

    public int getCacheHits() {
        return cacheHits;
    }

    public int getCacheMisses() {
        return cacheMisses;
    }
    

    private static class CacheLine {
        private int data;

        public boolean write(int data) {
            if (this.data != 0) {
                return false; // Cache miss
            }

            this.data = data;
            return true; // Cache hit
        }
        
        public boolean read() {
            return this.data != 0; // Cache hit if data is present
        }
        
        public int getData() {
            return data;
        }
    }

    public static void main(String[] args) {
        int cacheSize = 16;
        int blockSize = 4;
        int hitTime = 2;
        int missPenalty = 10;

        FullyAssociativeCache fullyAssociativeCache = new FullyAssociativeCache(cacheSize, blockSize);

        // Simulating reads and writes
        fullyAssociativeCache.write(0, 10);
        fullyAssociativeCache.write(4, 20);
        fullyAssociativeCache.write(8, 30);

        fullyAssociativeCache.read(4);
        fullyAssociativeCache.read(12); // Should result in a cache miss

        // Print cache hits and misses
        System.out.println("Cache Hits: " + fullyAssociativeCache.getCacheHits());
        System.out.println("Cache Misses: " + fullyAssociativeCache.getCacheMisses());

        // Calculate and print the effective time
        fullyAssociativeCache.calculateEffectiveTime(hitTime , missPenalty);
    }

}
