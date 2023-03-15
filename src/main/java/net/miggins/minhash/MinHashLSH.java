package net.miggins.minhash;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinHashLSH {

    private int numHashFunctions;
    private List<HashFunction> hashFunctions;
    private Map<Integer, List> buckets;

    public MinHashLSH(int numHashFunctions) {
        this.numHashFunctions = numHashFunctions;
        this.hashFunctions = new ArrayList<>();
        this.buckets = new HashMap<>();

        // Generate hash functions
        for (int i = 0; i < numHashFunctions; i++) {
            hashFunctions.add(new HashFunction());
        }
    }

    public void add(int[] set) {
        // Calculate minhash signature
        int[] signature = new int[numHashFunctions];
        for (int i = 0; i < numHashFunctions; i++) {
            signature[i] = hashFunctions.get(i).minHash(set);
        }

        // Add to buckets
        for (int i = 0; i < numHashFunctions; i++) {
            int bucket = signature[i];
            if (!buckets.containsKey(bucket)) {
                buckets.put(bucket, new ArrayList<>());
            }
            buckets.get(bucket).add(i);
        }
    }

    public boolean query(int[] set) {
        // Calculate minhash signature
        int[] signature = new int[numHashFunctions];
        for (int i = 0; i < numHashFunctions; i++) {
            signature[i] = hashFunctions.get(i).minHash(set);
        }

        // Check if any buckets match
        for (int i = 0; i < numHashFunctions; i++) {
            int bucket = signature[i];
            if (buckets.containsKey(bucket) && buckets.get(bucket).contains(i)) {
                return true;
            }
        }
        return false;
    }
}

class HashFunction {

    private int a;
    private int b;
    private int p;

    public HashFunction() {
        this.a = (int) (Math.random() * Integer.MAX_VALUE);
        this.b = (int) (Math.random() * Integer.MAX_VALUE);
        this.p = 2147483647; // Prime number
    }

    public int minHash(int[] set) {
        int minHash = Integer.MAX_VALUE;
        for (int x : set) {
            int hash = (a * x + b) % p;
            if (hash < minHash) {
                minHash = hash;
            }
        }
        return minHash;
    }
}

