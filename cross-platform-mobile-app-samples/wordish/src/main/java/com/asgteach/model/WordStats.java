package com.asgteach.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class WordStats {

    public static final int MAX_GUESS = 6;
    private static final Logger LOGGER = LoggerFactory.getLogger(WordStats.class);

    private int gamesPlayed = 0;
    private int totalWins = 0;
    private int currentStreak = 0;
    private int maxStreak = 0;
    private int thisGameGuesses = 0;

    private final Map<Integer, Integer> guessDistribution = new HashMap<>();

    WordStats() {
        LOGGER.info("Initializing guess distribution hashmap...");
        IntStream.rangeClosed(1, MAX_GUESS)
                .forEach(i -> guessDistribution.put(i, 0));
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public void setGamesPlayed(int gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }

    public int getCurrentStreak() {
        return currentStreak;
    }

    public void setCurrentStreak(int currentStreak) {
        this.currentStreak = currentStreak;
    }

    public int getMaxStreak() {
        return maxStreak;
    }

    public void setMaxStreak(int maxStreak) {
        this.maxStreak = maxStreak;
    }

    public double getWinPercentage() {
        if (gamesPlayed > 0) {
            return ((double) totalWins / gamesPlayed) * 100;
        }
        return 0;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public void setTotalWins(int totalWins) {
        this.totalWins = totalWins;
    }

    public int getThisGameGuesses() {
        return thisGameGuesses;
    }

    public void setThisGameGuesses(int thisGameGuesses) {
        this.thisGameGuesses = thisGameGuesses;
    }

    public Map<Integer, Integer> getGuessDistribution() {
        return Map.copyOf(guessDistribution);
    }

    public void updateGuessDistribution(int key) {
        guessDistribution.computeIfPresent(key, (k, value) -> value + 1);
    }

}
