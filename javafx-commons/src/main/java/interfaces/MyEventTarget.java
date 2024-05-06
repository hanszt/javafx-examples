package interfaces;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.event.EventType;

public interface MyEventTarget extends EventTarget, EventFilterTarget, EventHandlerTarget, EventDispatcherTarget {

    @Override
    default <E extends Event> void addEventHandler(EventType<E> eventType, EventHandler<? super E> eventHandler) {
        EventTarget.super.addEventHandler(eventType, eventHandler);
    }

    @Override
    default <E extends Event> void removeEventHandler(EventType<E> eventType, EventHandler<? super E> eventHandler) {
        EventTarget.super.removeEventHandler(eventType, eventHandler);
    }

    @Override
    default <E extends Event> void addEventFilter(EventType<E> eventType, EventHandler<? super E> eventHandler) {
        EventTarget.super.addEventFilter(eventType, eventHandler);
    }

    @Override
    default <E extends Event> void removeEventFilter(EventType<E> eventType, EventHandler<? super E> eventHandler) {
        EventTarget.super.removeEventFilter(eventType, eventHandler);
    }
}
