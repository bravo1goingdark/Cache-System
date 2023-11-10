import Mapping_Technique.Cache;
import Mapping_Technique.DirectMappedCache;

public class Main  {
    public static void main(String[] args) throws Exception {
        try {
            long hitTime = System.nanoTime();
            int cacheSize = 16; // Assuming 16 lines in the cache
            int blockSize = 4; // Assuming block size is 4 words

            Cache directMappedCache = new DirectMappedCache(cacheSize, blockSize);

            // Simulating reads and writes
            directMappedCache.write(0, 10);
            directMappedCache.write(4, 20);
            directMappedCache.write(8, 30);

            directMappedCache.read(4);
            directMappedCache.read(8); // Should result in a cache miss

            // Attempting to write to an invalid memory address
            directMappedCache.write(-1, 80);
            directMappedCache.read(0);
            long missPenalty = System.nanoTime();

            directMappedCache.calculateEffectiveTime(hitTime , missPenalty);

            // Print cache hits and misses
            System.out.println("Cache Hits: " + directMappedCache.getCacheHits());
            System.out.println("Cache Misses: " + directMappedCache.getCacheMisses());
        } catch (IllegalArgumentException e) {
            System.err.println("Exception: " + e.getMessage());
        }
    }
}
