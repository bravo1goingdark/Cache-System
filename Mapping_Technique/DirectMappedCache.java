package Mapping_Technique;
import java.util.HashMap;
import java.util.Map;

public class DirectMappedCache extends Cache {

    // cacheSize represents the total number of cache lines.
    // blockSize represents the number of words in each block.
    // blockCount is derived from the division of cacheSize by blockSize.
    // cache is a map representing the cache, where the key is the cache line number and the value is the data stored in that line.
    // cacheHits and cacheMisses keep track of the number of hits and misses.
    
    private int cacheSize;
    private int blockSize;
    private int blockCount;
    private int cacheHits;
    private int cacheMisses;
    private Map<Integer, Integer> cache;

    public DirectMappedCache() {
        if (super.CACHE_SIZE <= 0 || super.BLOCK_SIZE <= 0 || super.CACHE_SIZE % super.BLOCK_SIZE != 0) {
            throw new IllegalArgumentException("Invalid cache size or block size");
        }

        this.cacheSize = super.CACHE_SIZE;
        this.blockSize = super.BLOCK_SIZE;
        this.blockCount = cacheSize / blockSize;
        this.cache = new HashMap<>();
        this.cacheHits = 0;
        this.cacheMisses = 0;
    }

    public DirectMappedCache(int cacheSize, int blockSize) {
        if (cacheSize <= 0 || blockSize <= 0 || cacheSize % blockSize != 0) {
            throw new IllegalArgumentException("Invalid cache size or block size");
        }

        this.cacheSize = cacheSize;
        this.blockSize = blockSize;
        this.blockCount = cacheSize / blockSize;
        this.cache = new HashMap<>();
        this.cacheHits = 0;
        this.cacheMisses = 0;
    }

    public int getCacheHits() {
        return cacheHits;
    }

    public int getCacheMisses() {
        return cacheMisses;
    }

    public void write(int address, int data) throws Exception {
        if (address < 0 || address >= Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Invalid memory address");
        }

        int blockNumber = address / blockSize;
        int cacheLine = blockNumber % blockCount;

        if (cache.containsKey(cacheLine)) {
            cacheHits++;
            System.out.println("Write - Cache Hit: Address " + address + ", Data: " + data);
        } else {
            cacheMisses++;
            cache.put(cacheLine, data);
            System.out.println("Write - Cache Miss: Address " + address + ", Data: " + data);
        }
    }

    public int read(int address) throws Exception {
        if (address < 0 || address >= Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Invalid memory address");
        }

        int blockNumber = address / blockSize;
        int cacheLine = blockNumber % blockCount;

        if (cache.containsKey(cacheLine)) {
            cacheHits++;
            System.out.println("Read - Cache Hit: Address " + address + ", Data: " + cache.get(cacheLine));
            return cache.get(cacheLine);
        } else {
            cacheMisses++;
            System.out.println("Read - Cache Miss: Address " + address + ", Data not in Cache");
            return -1; // Data not in cache
        }
    }

    // hitTime -> Time taken for a cache hit in nanoseconds
    // missPenalty ->  Time penalty for a cache miss in nanoseconds

    public void calculateEffectiveTime(long hitTime , long missPenalty) {
        int totalAccesses = cacheHits + cacheMisses;
        double hitRate = (double) cacheHits / totalAccesses;
        double missRate = (double) cacheMisses / totalAccesses;

        double effectiveTime = hitRate * hitTime + missRate * (hitTime + missPenalty);
        System.out.println(effectiveTime);
    }

}
