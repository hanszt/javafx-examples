
package com.asgteach;

import com.asgteach.modelview.LetterLabel;
import com.asgteach.modelview.LetterStyle.DisplayType;
import javafx.fxml.FXML;

import java.io.IOException;


public class HowToController {

    @FXML
    private LetterLabel letterLabelPartial;
    @FXML
    private LetterLabel letterLabelNoMatch;
    @FXML
    private LetterLabel letterLabelMatching;
    private final WordishApp wordishApp;

    public HowToController(WordishApp wordishApp) {
        this.wordishApp = wordishApp;
    }
    
    public void initialize() {
        letterLabelMatching.setLetterDisplay(DisplayType.MATCHING);
        letterLabelPartial.setLetterDisplay(DisplayType.PARTIALMATCH);
        letterLabelNoMatch.setLetterDisplay(DisplayType.NOMATCH);
    }

    @FXML
    private void switchToWordish() throws IOException {
        wordishApp.setRoot("wordish");
    }
    
}
