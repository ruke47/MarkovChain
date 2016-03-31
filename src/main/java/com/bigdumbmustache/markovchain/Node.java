package com.bigdumbmustache.markovchain;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class Node<T> {
    private static final Random random = new Random();
    Map<T, Integer> nextChars = new TreeMap<>();
    transient int totalWeight = 0;

    public void add(T next, int weight) {
        final Integer curVal = nextChars.getOrDefault(next, 0);
        nextChars.put(next, curVal+weight);
        totalWeight+=weight;
    }

    public T pickChar() {
        final int n = random.nextInt(totalWeight+1);
        int sum = 0;
        for (Map.Entry<T, Integer> entry : nextChars.entrySet()) {
            sum += entry.getValue();
            if (sum >= n) {
                return entry.getKey();
            }
        }
        throw new IllegalStateException(n + " appearantly greater than totalWeight " + totalWeight);
    }
}
