package com.asgteach;

import com.asgteach.model.GameStatus;
import com.asgteach.model.LetterState;
import com.asgteach.model.WordStats;
import com.asgteach.model.WordTally;
import com.asgteach.modelview.KeyButton;
import com.asgteach.modelview.LetterLabel;
import com.asgteach.modelview.LetterStyle;
import com.asgteach.modelview.LetterStyle.DisplayType;

import static com.asgteach.modelview.LetterStyle.DisplayType.MATCHING;
import static com.asgteach.modelview.LetterStyle.DisplayType.NOMATCH;
import static com.asgteach.modelview.LetterStyle.DisplayType.PARTIALMATCH;

import com.asgteach.modelview.WordPopup;
import com.asgteach.service.WordData;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.fxml.FXML;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.TilePane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WordishController {

    private static final Logger LOGGER = LoggerFactory.getLogger(WordishController.class);

    private static final int ROW_FILLED = 5;

    private static final WordData wordData = WordData.getInstance();
    private static final GameStatus gameStatus = GameStatus.getInstance();
    @FXML
    private Button statsButton;
    @FXML
    private Button infoButton;
    @FXML
    private KeyButton enterButton;
    @FXML
    private KeyButton deleteButton;
    @FXML
    private TilePane letterTilePane;
    @FXML
    private FlowPane buttonFlowPane;

    private List<LetterLabel> letters;
    private Map<String, KeyButton> keyLetters;
    private final List<String> messages = Arrays.asList(
            "W O W . . . Did you cheat?",
            "G E N I U S",
            "I M P R E S S I V E",
            "S P L E N D I D",
            "G R E A T",
            "P H E W");
    private int rownum = 0;
    private final IntegerProperty squarenum = new SimpleIntegerProperty(0);
    private final BooleanProperty processingWord = new SimpleBooleanProperty(false);
    private final BooleanProperty gameOver = new SimpleBooleanProperty(false);
    private final BooleanProperty gameReset = new SimpleBooleanProperty(true);
    private final WordTally wordTally;
    private int rowSize;
    private final WordishApp wordishApp;

    public WordishController(WordishApp wordishApp) {
        this.wordishApp = wordishApp;
        this.wordTally = new WordTally();
    }

    public void initialize() {
        letterTilePane.setAlignment(Pos.TOP_CENTER);
        letterTilePane.setPrefRows(6);
        letters = letterTilePane.getChildren()
                .stream()
                .map(LetterLabel.class::cast)
                .toList();
        rowSize = letterTilePane.getPrefColumns();
        keyLetters = buttonFlowPane.getChildren()
                .stream()
                .map(KeyButton.class::cast)
                .filter(button -> button.getText().length() == 1)
                .collect(Collectors.toMap(KeyButton::getText, Function.identity()));

        statsButton.disableProperty().bind(gameReset.not());
        infoButton.disableProperty().bind(gameReset.not());

        enterButton.disableProperty().bind(squarenum.lessThan(ROW_FILLED)
                .or(gameOver));
        deleteButton.disableProperty().bind(squarenum.isEqualTo(0)
                .or(gameOver));
        keyLetters.values()
                .forEach(button -> button.disableProperty()
                        .bind(processingWord
                                .or(squarenum.isEqualTo(ROW_FILLED))
                                .or(gameOver)));

        if (gameStatus.getWordStats().getGamesPlayed() > 0
                && gameReset.get()) {
            restoreGame();
        } else {
            doResetGame();
        }
    }

    private void updateGameState(boolean win) {
        gameOver.set(true);

        WordStats wordStats = gameStatus.getWordStats();
        wordStats.setGamesPlayed(wordStats.getGamesPlayed() + 1);
        if (win) {
            LOGGER.info("WIN!");
            wordStats.setTotalWins(wordStats.getTotalWins() + 1);
            wordStats.setCurrentStreak(wordStats.getCurrentStreak() + 1);
            wordStats.setMaxStreak(Math.max(wordStats.getCurrentStreak(), wordStats.getMaxStreak()));
            int key = rownum + 1;
            LOGGER.info("Guess win is {} tries.", key);
            // Only store the guess distribution when you win
            wordStats.updateGuessDistribution(key);
            wordStats.setThisGameGuesses(key);
        } else {
            LOGGER.info("LOSE!");
            wordStats.setCurrentStreak(0);
            wordStats.setThisGameGuesses(0);
        }

        // update with scene graph nodes
        gameStatus.setLetterState(letters.stream()
                .map(letterLabel -> new LetterState(letterLabel.getLetterDisplay(), letterLabel.getText()))
                .toList());
        gameStatus.setKeyBoardState(keyLetters.values().stream()
                .collect(Collectors.toMap(
                        KeyButton::getText, KeyButton::getLetterDisplay)));
        gameReset.set(true);
    }

    @FXML
    private void resetGame() {
        gameReset.set(false);
        doResetGame();
    }

    private void restoreGame() {
        List<LetterState> savedLetters = gameStatus.getLetterState();
        Map<String, LetterStyle.DisplayType> savedKeys = gameStatus.getKeyBoardState();
        IntStream.range(0, savedLetters.size())
                .forEach(i -> resetLetterState(savedLetters, i));

        savedKeys.keySet().forEach(key -> keyLetters.get(key).setLetterDisplay(savedKeys.get(key)));
        processingWord.set(true);
        squarenum.set(0);
    }

    private void resetLetterState(List<LetterState> savedLetters, int i) {
        letters.get(i).setText(savedLetters.get(i).letter());
        letters.get(i).setMatchResult(savedLetters.get(i).displayType());
        letters.get(i).setLetterDisplay(savedLetters.get(i).displayType());
    }

    private void doResetGame() {
        wordData.setNewWord();
        gameOver.set(false);
        processingWord.set(false);
        squarenum.set(0);
        rownum = 0;
        letters.forEach(ll -> {
            ll.setText("");
            ll.setLetterDisplay(DisplayType.PLAIN);
        });
        keyLetters.values().forEach(ll -> ll.setLetterDisplay(DisplayType.DISPLAYING));
        // update with scene graph nodes
        gameStatus.setLetterState(letters.stream()
                .map(ll -> new LetterState(ll.getLetterDisplay(), ll.getText()))
                .toList());
        gameStatus.setKeyBoardState(keyLetters.values().stream()
                .collect(Collectors.toMap(
                        KeyButton::getText, KeyButton::getLetterDisplay)));
    }

    @FXML
    private void switchToStats() throws IOException {
        wordishApp.setRoot("stats");
    }

    @FXML
    private void selectLetter(ActionEvent event) {
        String letter = ((KeyButton) event.getSource()).getText();
        doSelectLetter(letter);
    }

    private void doSelectLetter(String letter) {
        gameReset.set(false);
        int index = squarenum.get() + (rowSize * rownum);
        keyLetters.get(letter).requestFocus();
        letters.get(index).setText(letter);
        letters.get(index).setLetterDisplay(DisplayType.DISPLAYING);
        squarenum.set(squarenum.get() + 1);
        if (squarenum.get() >= ROW_FILLED) {
            infoButton.getScene().getRoot().requestFocus();
        }
    }

    @FXML
    private void processEnter() {
        doProcessEnter();
    }

    private void doProcessEnter() {
        processingWord.set(true);
        gameReset.set(false);
        List<LetterLabel> workingLetters = letters.stream()
                .skip((long) rownum * rowSize)
                .limit(rowSize)
                .toList();
        processWord(workingLetters);
    }

    private void processWord(List<LetterLabel> list) {
        // Is it a valid word?
        String guess = list.stream()
                .map(Labeled::getText)
                .collect(Collectors.joining());
        wordTally.setGuess(guess);
        wordTally.setTarget(wordData.getTheWord());

        if (!wordData.isAWord(guess)) {
            // bad word
            WordPopup.show("Word is not in the valid word list.", enterButton);
            animateBadWord(list);
            processingWord.set(false);
            return;
        }
        doProcessMatching(list);
        doProcessPartial(list);
        doProcessNoMatch(list);
        animateLabelGroup(list);
    }

    private static void animateBadWord(List<LetterLabel> list) {
        ParallelTransition para = new ParallelTransition();
        para.getChildren().addAll(list.stream()
                .map(WordishController::getTranslateTransition)
                .toList());
        para.play();
    }

    private static TranslateTransition getTranslateTransition(LetterLabel letterLabel) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(100), letterLabel);
        translateTransition.setByX(20);
        translateTransition.setAutoReverse(true);
        translateTransition.setCycleCount(6);
        return translateTransition;
    }

    private void doProcessMatching(List<LetterLabel> list) {
        list.stream()
                .filter(ll -> isMatchingLetter(list, ll))
                .forEach(ll -> setMatchingResult(list, ll));
    }

    private boolean isMatchingLetter(List<LetterLabel> list, LetterLabel letterLabel) {
        return wordTally.getGuess().charAt(list.indexOf(letterLabel)) == wordTally.getTarget().charAt(list.indexOf(letterLabel));
    }

    private void setMatchingResult(List<LetterLabel> list, LetterLabel letterLabel) {
        letterLabel.setMatchResult(MATCHING);
        int index = list.indexOf(letterLabel);
        final var guess = wordTally.getGuess();
        wordTally.setGuess(guess.substring(0, index) +
                "-" + guess.substring(index + 1));
        wordTally.setTarget(wordTally.getTarget().substring(0, index) +
                "-" + wordTally.getTarget().substring(index + 1));
    }

    private void doProcessPartial(List<LetterLabel> list) {
        list.stream()
                .filter(ll -> wordTally.getGuess().charAt(list.indexOf(ll)) != '-')
                .forEach(ll -> setPartialMatch(list, ll));
    }

    private void setPartialMatch(List<LetterLabel> list, LetterLabel ll) {
        final var guess = wordTally.getGuess();
        char c = guess.charAt(list.indexOf(ll));
        final var target = wordTally.getTarget();
        if (target.contains(Character.toString(c))) {
            ll.setMatchResult(PARTIALMATCH);
            int index = target.indexOf(c);
            int index2 = list.indexOf(ll);
            wordTally.setTarget(target.substring(0, index) + "-" + target.substring(index + 1));
            wordTally.setGuess(guess.substring(0, index2) + "-" + guess.substring(index2 + 1));
        }
    }

    private void doProcessNoMatch(List<LetterLabel> list) {
        list.stream()
                .filter(ll -> wordTally.getGuess().charAt(list.indexOf(ll)) != '-')
                .forEach(ll -> ll.setMatchResult(NOMATCH));
    }

    private void animateLabelGroup(List<LetterLabel> list) {
        SequentialTransition seq = new SequentialTransition();
        seq.getChildren().addAll(list.stream()
                .map(WordishController::getParallelTransition)
                .toList());
        seq.setOnFinished(x -> gameHouseKeeping(list));
        seq.play();
    }

    private static ParallelTransition getParallelTransition(LetterLabel letterLabel) {
        String letterText = letterLabel.getText();
        FadeTransition fade = new FadeTransition(
                Duration.millis(100), letterLabel);
        fade.setAutoReverse(true);
        fade.setCycleCount(2);
        fade.setToValue(0);

        fade.setDelay(Duration.millis(20));
        fade.setOnFinished(x -> {
            letterLabel.setText("");
            letterLabel.setLetterDisplay(letterLabel.getMatchResult());
        });

        RotateTransition rotate = new RotateTransition(
                Duration.millis(400), letterLabel);
        rotate.setAxis(Rotate.X_AXIS);
        rotate.setByAngle(180);
        rotate.setOnFinished(x -> {
            letterLabel.setRotate(0);
            letterLabel.setText(letterText);
        });

        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().addAll(fade, rotate);
        return parallelTransition;
    }

    private void gameHouseKeeping(List<LetterLabel> list) {
        processingWord.set(false);
        list.forEach(ll -> keyLetters.get(ll.getText()).setLetterDisplay(ll.getMatchResult()));

        if ((list.stream().allMatch(letterLabel -> letterLabel.getMatchResult() == MATCHING))) {   //match!
            LOGGER.info("Winner! You guessed the word {}", wordData.getTheWord());
            WordPopup.show(messages.get(rownum), enterButton);
            animateSuccessGroup(list);
            updateGameState(true);
        } else if (rownum + 1 >= letterTilePane.getPrefRows()) {
            // You took too many guesses
            WordPopup.show("Sorry! You lose this time!\nThe word is " + wordData.getTheWord(), enterButton);
            updateGameState(false);
        } else if (squarenum.get() >= rowSize) {
            squarenum.set(0);
            rownum++;
            if (rownum >= letterTilePane.getPrefRows()) {
                rownum = 0;
            }
        } else {
            LOGGER.trace("No housekeeping action to take...");
        }
    }

    private static void animateSuccessGroup(List<LetterLabel> list) {
        SequentialTransition seq = new SequentialTransition();
        seq.getChildren().addAll(list.stream()
                .map(WordishController::getRotateTransition)
                .toList());
        seq.setDelay(Duration.millis(100));
        seq.play();
    }

    private static RotateTransition getRotateTransition(LetterLabel letterLabel) {
        RotateTransition rotate = new RotateTransition(Duration.millis(100), letterLabel);
        rotate.setAxis(Rotate.Z_AXIS);
        rotate.setByAngle(60);
        rotate.setAutoReverse(true);
        rotate.setCycleCount(4);
        return rotate;
    }

    @FXML
    private void processDelete() {
        doProcessDelete();
    }

    private void doProcessDelete() {
        int index = squarenum.get() - 1 + (rowSize * rownum);
        squarenum.set(squarenum.get() - 1);
        infoButton.getScene().getRoot().requestFocus();
        letters.get(index).setText("");
        letters.get(index).setLetterDisplay(DisplayType.PLAIN);
    }

    // allow keyboard input if available
    @FXML
    private void handleKeyPressed(KeyEvent event) {
        // ignore key input if certain buttons are disabled
        if (event.getCode().isLetterKey()) {
            if (keyLetters.get("A").isDisabled()) {
                return;
            }
            // process letter
            doSelectLetter(event.getCode().getChar());
        } else if (event.getCode() == KeyCode.ENTER) {
            if (enterButton.isDisabled()) {
                return;
            }
            // process Enter
            doProcessEnter();
        } else if (event.getCode() == KeyCode.DELETE || event.getCode() == KeyCode.BACK_SPACE) {
            if (deleteButton.isDisabled()) {
                return;
            }
            // process Delete
            doProcessDelete();
        } else {
            LOGGER.trace("Something else: {}", event.getCode().getChar());
        }
    }

    @FXML
    private void displayInfo() throws IOException {
        wordishApp.setRoot("howto");
    }

}
