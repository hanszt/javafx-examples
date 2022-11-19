package com.asgteach.service;

import com.asgteach.WordishApp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public final class WordData {

    private static final Logger LOGGER = LoggerFactory.getLogger(WordData.class);

    private static final Random rand = new Random();
    private String theWord = "";
    private final Set<Integer> usedIndices = new HashSet<>();
    private final List<String> words;

    private WordData() {
        words = readWords();
    }

    private static List<String> readWords() {
        LOGGER.info("building word data...");
        final var name = "/com/asgteach/service/wordlist.txt";
        try (InputStream is = WordishApp.class.getResourceAsStream(name);
             final var bufferedReader = new BufferedReader(
                     new InputStreamReader(Objects.requireNonNull(is, "Resource " + name + " not found...")))) {
            return bufferedReader
                    .lines()
                    .toList();
        } catch (IOException ex) {
            throw new IllegalStateException("Could not load resource at " + name, ex);
        }
    }

    private static class Holder {

        private static final WordData instance = new WordData();
    }

    public static WordData getInstance() {
        return Holder.instance;
    }

    public String getTheWord() {
        return theWord;
    }

    public boolean isAWord(String guess) {
        return words.stream().anyMatch(guess::equals);
    }

    public void setNewWord() {
        int index;
        do {
            index = rand.nextInt(words.size());
        } while (!usedIndices.add(index));
        theWord = words.get(index);
        LOGGER.debug("theWord = {}, index = {}", theWord, index);
    }

}
