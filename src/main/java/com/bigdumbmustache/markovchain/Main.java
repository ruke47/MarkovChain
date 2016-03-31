package com.bigdumbmustache.markovchain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static final Map<String, Integer> corpus = new HashMap<String, Integer>() {{
        put("ospd.txt", 1);
    }};

    public static void main(String... args) throws IOException {
        final Chain chain = new Chain(4);

        corpus.forEach((name, weight) -> {
            try (final BufferedReader reader = resource(name)) {
                chain.learn(reader, weight);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

        for (int i = 0; i < 50; i++) {
            System.out.println(chain.generateNovelWord());
        }
    }

    private static BufferedReader resource(final String name) {
        final InputStream stream = Main.class.getClassLoader().getResourceAsStream(name);
        return new BufferedReader(new InputStreamReader(stream));
    }


}
