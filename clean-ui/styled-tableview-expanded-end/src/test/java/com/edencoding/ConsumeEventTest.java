package com.edencoding;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.util.function.Consumer;

import static com.edencoding.ConsumeEventTest.RichEventHandler.consumer;
import static com.edencoding.ConsumeEventTest.RichEventHandler.handler;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(ApplicationExtension.class)
class ConsumeEventTest {

    private Rectangle rect;
    private StackPane root;

    @Start
    void onStartup(Stage stage) {
        rect = new Rectangle(50, 50);

        root = new StackPane(rect);

        Scene scene = new Scene(root, 200, 200);

        stage.setScene(scene);
        stage.show();
    }

    @Test
    void testMouseListenerWithoutConsume(FxRobot robot) {
        final var stringBuilder = new StringBuilder();
        final EventHandler<MouseEvent> mouseEventEventHandlerRect = evt -> stringBuilder.append("rect click(filter)");
        final EventHandler<MouseEvent> mouseEventEventHandlerRoot = evt -> stringBuilder.append("root click(filter)");
        try {
            rect.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEventEventHandlerRect);
            root.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEventEventHandlerRoot);
            robot.clickOn(rect);
            robot.clickOn(root);
            assertEquals("root click(filter)rect click(filter)root click(filter)rect click(filter)", stringBuilder.toString());
        } finally {
            rect.removeEventFilter(MouseEvent.MOUSE_CLICKED, mouseEventEventHandlerRect);
            root.removeEventFilter(MouseEvent.MOUSE_CLICKED, mouseEventEventHandlerRoot);
        }
    }

    @Test
    void testMouseListenerWithConsume(FxRobot robot) {
        final var stringBuilder = new StringBuilder();
        final EventHandler<Event> mouseEventEventHandlerRect = handler(evt -> stringBuilder.append("rect click(filter)")).andThen(Event::consume);
        final EventHandler<Event> mouseEventEventHandlerRoot = handler(evt -> stringBuilder.append("root click(filter)")).andThen(Event::consume);
        try {
            rect.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEventEventHandlerRect);
            root.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEventEventHandlerRoot);
            robot.clickOn(rect);
            robot.clickOn(root);
            assertEquals("root click(filter)root click(filter)", stringBuilder.toString());
        } finally {
            rect.removeEventFilter(MouseEvent.MOUSE_CLICKED, mouseEventEventHandlerRect);
            root.removeEventFilter(MouseEvent.MOUSE_CLICKED, mouseEventEventHandlerRoot);
        }
    }

    @Test
    void testMouseClickWithoutConsume(FxRobot robot) {
        final var stringBuilder = new StringBuilder();
        rect.setOnMouseClicked(evt -> stringBuilder.append("rect click(filter)"));
        root.setOnMouseClicked(evt -> stringBuilder.append("root click(filter)"));
        robot.clickOn(rect);
        robot.clickOn(root);
        assertEquals("rect click(filter)root click(filter)rect click(filter)root click(filter)", stringBuilder.toString());
    }

    @Test
    void testMouseClickWithConsume(FxRobot robot) {
        final var stringBuilder = new StringBuilder();
        rect.setOnMouseClicked(consumer(mouseEvent -> stringBuilder.append("rect click(filter)")).andThen(Event::consume));
        root.setOnMouseClicked(consumer(mouseEvent -> stringBuilder.append("root click(filter)")).andThen(Event::consume));
        robot.clickOn(rect);
        robot.clickOn(root);
        assertEquals("rect click(filter)rect click(filter)", stringBuilder.toString());
    }

    interface RichEventHandler<T extends Event> extends EventHandler<T> {

        static <T extends Event> RichEventHandler<T> handler(EventHandler<? super T> eventHandler) {
            return eventHandler::handle;
        }

        static <T extends Event> RichEventHandler<T> consumer(Consumer<? super T> consumer) {
            return consumer::accept;
        }

        default RichEventHandler<T> andThen(EventHandler<? super T> eventHandler) {
            return t -> {
                handle(t);
                eventHandler.handle(t);
            };
        }

    }

}
