package Mapping_Technique;

import java.util.HashMap;
import java.util.Map;

public class SetAssociativeCache extends Cache{
    private int cacheSize;
    private int blockSize;
    private int associativity; // Number of lines per set
    private int setCount; // Number of sets in the cache
    private int cacheHits;
    private int cacheMisses;
    private Map<Integer, CacheSet> cache;
    private int hitTime; // Time taken for a cache hit in nanoseconds
    private int missPenalty; // Time penalty for a cache miss in nanoseconds

    public SetAssociativeCache(int cacheSize, int blockSize, int associativity, int hitTime, int missPenalty) {
        this.cacheSize = cacheSize;
        this.blockSize = blockSize;
        this.associativity = associativity;
        this.setCount = cacheSize / (blockSize * associativity);
        this.cache = new HashMap<>();
        this.cacheHits = 0;
        this.cacheMisses = 0;
        this.hitTime = hitTime;
        this.missPenalty = missPenalty;

        // Initialize cache sets
        for (int i = 0; i < setCount; i++) {
            cache.put(i, new CacheSet(associativity));
        }
    }

    public void write(int address, int data) {
        int blockNumber = address / blockSize;
        int setIndex = blockNumber % setCount;

        CacheSet cacheSet = cache.get(setIndex);

        try {
            if (cacheSet.write(blockNumber, data)) {
                cacheHits++;
                System.out.println("Write - Cache Hit: Address " + address + ", Data: " + data);
            } else {
                cacheMisses++;
                System.out.println("Write - Cache Miss: Address " + address + ", Data: " + data);
            }
        } catch (CacheEvictionException e) {
            // Handle cache eviction exception
            System.out.println("Write - Cache Eviction: " + e.getMessage());
        }
    }

    public int read(int address) {
        int blockNumber = address / blockSize;
        int setIndex = blockNumber % setCount;

        CacheSet cacheSet = cache.get(setIndex);

        try {
            if (cacheSet.read(blockNumber)) {
                cacheHits++;
                System.out.println("Read - Cache Hit: Address " + address + ", Data: " + cacheSet.getData(blockNumber));
                return cacheSet.getData(blockNumber);
            } else {
                cacheMisses++;
                System.out.println("Read - Cache Miss: Address " + address + ", Data not in Cache");
                return -1; // Data not in cache
            }
        } catch (CacheEvictionException e) {
            // Handle cache eviction exception
            System.out.println("Read - Cache Eviction: " + e.getMessage());
            return -1; // Data not in cache due to eviction
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

    private static class CacheSet {
        private int associativity;
        private Map<Integer, CacheLine> lines;

        public CacheSet(int associativity) {
            this.associativity = associativity;
            this.lines = new HashMap<>();
            for (int i = 0; i < associativity; i++) {
                lines.put(i, new CacheLine());
            }
        }

        public boolean write(int blockNumber, int data) throws CacheEvictionException {
            for (CacheLine line : lines.values()) {
                if (line.getTag() == blockNumber) {
                    line.setData(data);
                    return true; // Cache hit
                }
            }

            // Cache miss, try to find an empty line
            for (CacheLine line : lines.values()) {
                if (line.getTag() == -1) {
                    line.setTag(blockNumber);
                    line.setData(data);
                    return true; // Cache miss, no eviction
                }
            }

            // Cache miss, eviction needed
            throw new CacheEvictionException("Cache eviction needed for block " + blockNumber);
        }

        public boolean read(int blockNumber) throws CacheEvictionException {
            for (CacheLine line : lines.values()) {
                if (line.getTag() == blockNumber) {
                    return true; // Cache hit
                }
            }

            // Cache miss
            throw new CacheEvictionException("Cache eviction needed for block " + blockNumber);
        }

        public int getData(int blockNumber) {
            for (CacheLine line : lines.values()) {
                if (line.getTag() == blockNumber) {
                    return line.getData();
                }
            }
            return -1; // Data not in cache
        }
    }

    private static class CacheLine {
        private int tag;
        private int data;

        public CacheLine() {
            this.tag = -1; // Initialize tag to -1 indicating an empty line
        }

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

    private static class CacheEvictionException extends Exception {
        public CacheEvictionException(String message) {
            super(message);
        }
    }

    public static void main(String[] args) {
        int cacheSize = 16;
        int blockSize = 4;
        int associativity = 2; // Two-way set-associative
        int hitTime = 2;
        int missPenalty = 10;

        SetAssociativeCache setAssociativeCache = new SetAssociativeCache(cacheSize, blockSize, associativity, hitTime, missPenalty);

        // Simulating reads and writes
        setAssociativeCache.write(0, 10);
        setAssociativeCache.write(4, 20);
        setAssociativeCache.write(8, 30);

        setAssociativeCache.read(4);
        setAssociativeCache.read(12); // Should result in a cache miss

        // Print cache hits and misses
        System.out.println("Cache Hits: " + setAssociativeCache.getCacheHits());
        System.out.println("Cache Misses: " + setAssociativeCache.getCacheMisses());

        // Calculate and print the effective time
       setAssociativeCache.calculateEffectiveTime(hitTime,missPenalty);
    }

}
