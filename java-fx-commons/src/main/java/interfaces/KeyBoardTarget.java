package interfaces;

import javafx.beans.property.ObjectProperty;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;

public interface KeyBoardTarget {

    ObjectProperty<EventHandler<? super KeyEvent>> onKeyPressedProperty();

    EventHandler<? super KeyEvent> getOnKeyPressed();

    void setOnKeyPressed(EventHandler<? super KeyEvent> var1);

    ObjectProperty<EventHandler<? super KeyEvent>> onKeyReleasedProperty();

    EventHandler<? super KeyEvent> getOnKeyReleased();

    void setOnKeyReleased(EventHandler<? super KeyEvent> var1);

    ObjectProperty<EventHandler<? super KeyEvent>> onKeyTypedProperty();

    EventHandler<? super KeyEvent> getOnKeyTyped();

    void setOnKeyTyped(EventHandler<? super KeyEvent> var1);
}
