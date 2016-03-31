package com.bigdumbmustache.markovchain;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Chain {
    public static final char terminator = '$';
    private final int chainLen;

    private final Map<String, Node<Character>> nodes = new HashMap<>();
    private final Set<String> learnedWords = new HashSet<>();


    public Chain(final int len) {
        chainLen = len;
    }

    public void learn(BufferedReader reader, int weight) throws IOException {
        while (true) {
            final String line = reader.readLine();
            if (line == null || line.isEmpty()) {
                break;
            }

            learnWord(line.toLowerCase(), weight);
        }
    }

    public String generateNovelWord() {
        while (true) {
            final String word = generateWord();
            if (!learnedWords.contains(word)) {
                return word;
            }
        }
    }

    public String generateWord() {
        if (learnedWords.isEmpty()) {
            throw new IllegalStateException("Have not learned any words yet!");
        }

        final StringBuffer wordBuf = new StringBuffer();
        final LinkedList<Character> chars = new LinkedList<>();
        while (true) {
            final char newChar = nodes.get(toString(chars)).pickChar();
            if (newChar == terminator) {
                break;
            }

            wordBuf.append(newChar);
            chars.addLast(newChar);
            if (chars.size() > chainLen) {
                chars.removeFirst();
            }
        }

        return wordBuf.toString();
    }

    private void learnWord(String word, int weight) {
        learnedWords.add(word);

        final LinkedList<Character> chars = new LinkedList<>();
        for (char c : (word+terminator).toCharArray()) {
            //update based on the last (up-to) `chainLen` characters
            final String key = toString(chars);
            nodes.compute(key, (k, node) -> node == null ? new Node<>() : node).add(c, weight);

            chars.addLast(c);
            if (chars.size() > chainLen) {
                chars.removeFirst();
            }
        }
    }

    private String toString(List<Character> chars) {
        final StringBuffer sb = new StringBuffer();
        chars.forEach(sb::append);
        return sb.toString();
    }
}
