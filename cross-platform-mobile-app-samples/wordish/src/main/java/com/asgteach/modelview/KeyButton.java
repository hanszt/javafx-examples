package com.asgteach.modelview;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;

import static com.asgteach.modelview.LetterStyle.DisplayType.PLAIN;
import static com.asgteach.modelview.LetterStyle.*;

@SuppressWarnings(KeyButton.INHERITANCE_TREE_SHOULD_NOT_BE_TO_SEEP)
public class KeyButton extends Button {

    static final String INHERITANCE_TREE_SHOULD_NOT_BE_TO_SEEP = "squid:S110";

    private final ObjectProperty<DisplayType> letterDisplay = new SimpleObjectProperty<>(PLAIN) {

        @Override
        public void invalidated() {
            // Turn off all the custom PseudoClasses in the LetterLabel,
            // and then uses a switch statement to turn on a
            // specific PseudoClass depending on the value
            // in the labelDisplay property
            pseudoClassStateChanged(PLAIN_PSEUDO_CLASS, false);
            pseudoClassStateChanged(NOMATCH_PSEUDO_CLASS, false);
            pseudoClassStateChanged(PARTIALMATCH_PSEUDO_CLASS, false);
            pseudoClassStateChanged(MATCHING_PSEUDO_CLASS, false);
            switch (get()) {
                case NOMATCH -> pseudoClassStateChanged(NOMATCH_PSEUDO_CLASS, true);
                case PARTIALMATCH -> pseudoClassStateChanged(PARTIALMATCH_PSEUDO_CLASS, true);
                case MATCHING -> pseudoClassStateChanged(MATCHING_PSEUDO_CLASS, true);
                default -> pseudoClassStateChanged(PLAIN_PSEUDO_CLASS, true);
            }
        }
    };

    public KeyButton() {
        super();
        getStyleClass().add("matching-letter");
        getStyleClass().add("nomatch-letter");
        getStyleClass().add("partialmatch-letter");
        getStyleClass().add("plain-letter");
    }

    public void setLetterDisplay(LetterStyle.DisplayType labelType) {
        // this invokes invalidated()
        this.letterDisplay.set(labelType);
    }

    public LetterStyle.DisplayType getLetterDisplay() {
        return letterDisplay.get();
    }
    

}
