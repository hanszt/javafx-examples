
package com.asgteach.model;

import com.asgteach.modelview.LetterStyle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class GameStatus {

    private List<LetterState> letterState = new ArrayList<>();
    private Map<String, LetterStyle.DisplayType> keyBoardState = new HashMap<>();
    private final WordStats wordStats = new WordStats();

    private GameStatus() {
    }

    private static class Holder {

        private static final GameStatus instance = new GameStatus();
    }

    public static GameStatus getInstance() {
        return Holder.instance;
    }

    public List<LetterState> getLetterState() {
        return List.copyOf(letterState);
    }

    public Map<String, LetterStyle.DisplayType> getKeyBoardState() {
        return Map.copyOf(keyBoardState);
    }

    public WordStats getWordStats() {
        return this.wordStats;
    }

    public void setLetterState(List<LetterState> letterState) {
        this.letterState = List.copyOf(letterState);
    }

    public void setKeyBoardState(Map<String, LetterStyle.DisplayType> keyBoardState) {
        this.keyBoardState = Map.copyOf(keyBoardState);
    }

}


