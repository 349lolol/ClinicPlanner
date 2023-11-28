import java.util.HashMap;
import java.util.Map;
import java.util.Set;

    //Solution 1: Pure brute force O(2^N) 
    //bitmask through every single combination, literal worst case brute force
    //Average solution: 2^(N-1)

    //Solution 2: "optimized" brute force O(2^N)
    //bitmask thorugh every single combination, but this start by testing all combinations of 1, 2, 3
    //Average solution: Dependent on how connected city is, but should be around O(N * 2^(m+1)), where m is number of clinics required

    //Solution 3 : Greedy algorithm O(N^2)
    //Calculate the weight: The number of uncovered neighbours for each node
    //Store the most weighted node
    //mark weighted node as a new clinic, recompute until all nodes are covered

public class Algorithm {

    public Set<Integer> bruteForce(){

        return null;
    }
    
}
