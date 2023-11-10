package Replacement_Policy;

public interface ReplacementPolicy {
    
    void accessed(int lineIndex);
    int chooseLineToReplace();
}
