package com.asgteach.modelview;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Label;

import static com.asgteach.modelview.LetterStyle.*;
import static com.asgteach.modelview.LetterStyle.DisplayType.DISPLAYING;
import static com.asgteach.modelview.LetterStyle.DisplayType.PLAIN;

@SuppressWarnings(LetterLabel.INHERITANCE_TREE_SHOULD_NOT_BE_TO_SEEP)
public class LetterLabel extends Label {

    static final String INHERITANCE_TREE_SHOULD_NOT_BE_TO_SEEP = "squid:S110";

    private final ObjectProperty<DisplayType> matchResult = new SimpleObjectProperty<>(DISPLAYING);

    private final ObjectProperty<DisplayType> letterDisplay = new SimpleObjectProperty<>(PLAIN) {
        @Override
        public void invalidated() {
            // Turn off all the custom PseudoClasses in the LetterLabel,
            // and then uses a switch statement to turn on a
            // specific PseudoClass depending on the value
            // in the letterDisplay property
            pseudoClassStateChanged(PLAIN_PSEUDO_CLASS, false);
            pseudoClassStateChanged(DISPLAYING_PSEUDO_CLASS, false);
            pseudoClassStateChanged(NOMATCH_PSEUDO_CLASS, false);
            pseudoClassStateChanged(PARTIALMATCH_PSEUDO_CLASS, false);
            pseudoClassStateChanged(MATCHING_PSEUDO_CLASS, false);
            switch (get()) {
                case PLAIN -> pseudoClassStateChanged(PLAIN_PSEUDO_CLASS, true);
                case DISPLAYING -> pseudoClassStateChanged(DISPLAYING_PSEUDO_CLASS, true);
                case NOMATCH -> pseudoClassStateChanged(NOMATCH_PSEUDO_CLASS, true);
                case PARTIALMATCH -> pseudoClassStateChanged(PARTIALMATCH_PSEUDO_CLASS, true);
                case MATCHING -> pseudoClassStateChanged(MATCHING_PSEUDO_CLASS, true);
            }
        }
    };

    public LetterLabel() {
        super();
        getStyleClass().addAll("displaying-letter", "matching-letter",
                "nomatch-letter", "partialmatch-letter", "plain-letter");
    }

    public void setLetterDisplay(DisplayType labelType) {
        this.letterDisplay.set(labelType);
    }
    public DisplayType getLetterDisplay() {
        return letterDisplay.get();
    }
    
    public void setMatchResult(DisplayType labelType) {
        this.matchResult.set(labelType);
    }
    public DisplayType getMatchResult() {
        return matchResult.get();
    }

}
