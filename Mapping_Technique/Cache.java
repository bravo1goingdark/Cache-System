package Mapping_Technique;

public abstract class Cache {

    public abstract int read (int address) throws Exception;
    public abstract void write(int data , int address) throws Exception;
    public abstract int getCacheHits();
    public abstract int getCacheMisses();
    public abstract void calculateEffectiveTime(long hitTime , long missPenalty);
    public int CACHE_SIZE = 16;
    public int BLOCK_SIZE = 4;
    
}