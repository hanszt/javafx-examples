package hzt;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public final class MouseEventFilterExample extends Application {

    private final BooleanProperty dragModeActiveProperty =
            new SimpleBooleanProperty(this, "dragModeActive", true);

    @Override
    public void start(final Stage stage) {
        final Node loginPanel = makeDraggable(createLoginPanel());
        final Node confirmationPanel = makeDraggable(createConfirmationPanel());
        final Node progressPanel = makeDraggable(createProgressPanel());

        loginPanel.relocate(0, 0);
        confirmationPanel.relocate(0, 67);
        progressPanel.relocate(0, 106);

        final Pane panelsPane = new Pane();
        panelsPane.getChildren().addAll(loginPanel, confirmationPanel, progressPanel);

        final BorderPane sceneRoot = new BorderPane();

        BorderPane.setAlignment(panelsPane, Pos.TOP_LEFT);
        sceneRoot.setCenter(panelsPane);

        final CheckBox dragModeCheckbox = new CheckBox("Drag mode");
        BorderPane.setMargin(dragModeCheckbox, new Insets(6));
        sceneRoot.setBottom(dragModeCheckbox);

        dragModeActiveProperty.bind(dragModeCheckbox.selectedProperty());

        final Scene scene = new Scene(sceneRoot, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Draggable Panels Example");
        stage.show();
    }

    private Node makeDraggable(final Node node) {
        final DragContext dragContext = new DragContext();
        final Group wrapGroup = new Group(node);


        wrapGroup.addEventFilter(MouseEvent.ANY, this::consumeIfDragMode);
        wrapGroup.addEventFilter(MouseEvent.MOUSE_PRESSED, mouseEvent -> setDragContextIfInDragMode(node, dragContext, mouseEvent));
        wrapGroup.addEventFilter(MouseEvent.MOUSE_DRAGGED, mouseEvent -> translateIfInDragMode(node, dragContext, mouseEvent));


        return wrapGroup;
    }

    private void consumeIfDragMode(MouseEvent mouseEvent) {
        if (dragModeActiveProperty.get()) {
            // disable mouse events for all children
            mouseEvent.consume();
        }
    }

    private void setDragContextIfInDragMode(Node node, DragContext dragContext, MouseEvent mouseEvent) {
        if (dragModeActiveProperty.get()) {
            // remember initial mouse cursor coordinates
            // and node position
            dragContext.mouseAnchorX = mouseEvent.getX();
            dragContext.mouseAnchorY = mouseEvent.getY();
            dragContext.initialTranslateX = node.getTranslateX();
            dragContext.initialTranslateY = node.getTranslateY();
        }
    }

    private void translateIfInDragMode(Node node, DragContext dragContext, MouseEvent mouseEvent) {
        if (dragModeActiveProperty.get()) {
            // shift node from its initial position by delta
            // calculated from mouse cursor movement
            node.setTranslateX(dragContext.initialTranslateX
                            + mouseEvent.getX()
                            - dragContext.mouseAnchorX);
            node.setTranslateY(dragContext.initialTranslateY
                            + mouseEvent.getY()
                            - dragContext.mouseAnchorY);
        }
    }

    private static Node createLoginPanel() {
        final ToggleGroup toggleGroup = new ToggleGroup();

        final TextField textField = new TextField();
        textField.setPrefColumnCount(10);
        textField.setPromptText("Your name");

        final PasswordField passwordField = new PasswordField();
        passwordField.setPrefColumnCount(10);
        passwordField.setPromptText("Your password");

        final ChoiceBox<String> choiceBox = new ChoiceBox<>(
                FXCollections.observableArrayList(
                        "English", "\u0420\u0443\u0441\u0441\u043a\u0438\u0439",
                        "Fran\u00E7ais"));
        choiceBox.setTooltip(new Tooltip("Your language"));
        choiceBox.getSelectionModel().select(0);

        final HBox panel = createHBox(
                        createVBox(createRadioButton("High", toggleGroup, true),
                                createRadioButton("Medium", toggleGroup,
                                        false),
                                createRadioButton("Low", toggleGroup, false)),
                        createVBox(textField, passwordField),
                        choiceBox);
        panel.setAlignment(Pos.BOTTOM_LEFT);
        configureBorder(panel);

        return panel;
    }

    private static Node createConfirmationPanel() {
        final Label acceptanceLabel = new Label("Not Available");

        final Button acceptButton = new Button("Accept");
        acceptButton.setOnAction(event -> acceptanceLabel.setText("Accepted"));

        final Button declineButton = new Button("Decline");
        declineButton.setOnAction(event -> acceptanceLabel.setText("Declined"));

        final HBox panel = createHBox(acceptButton,
                declineButton,
                acceptanceLabel);
        panel.setAlignment(Pos.CENTER_LEFT);
        configureBorder(panel);

        return panel;
    }

    private static Node createProgressPanel() {
        final Slider slider = new Slider();

        final ProgressIndicator progressIndicator = new ProgressIndicator(0);
        progressIndicator.progressProperty().bind(
                Bindings.divide(slider.valueProperty(),
                        slider.maxProperty()));

        final HBox panel = createHBox(new Label("Progress:"),
                slider,
                progressIndicator);
        configureBorder(panel);

        return panel;
    }

    private static void configureBorder(final Region region) {
        region.setStyle("-fx-background-color: white;"
                + "-fx-border-color: black;"
                + "-fx-border-width: 1;"
                + "-fx-border-radius: 6;"
                + "-fx-padding: 6;");
    }

    private static RadioButton createRadioButton(final String text,
                                                 final ToggleGroup toggleGroup,
                                                 final boolean selected) {
        final RadioButton radioButton = new RadioButton(text);
        radioButton.setToggleGroup(toggleGroup);
        radioButton.setSelected(selected);

        return radioButton;
    }

    private static HBox createHBox(final Node... children) {
        final HBox hbox = new HBox(6);
        hbox.getChildren().addAll(children);
        return hbox;
    }

    private static VBox createVBox(final Node... children) {
        final VBox vbox = new VBox(2);
        vbox.getChildren().addAll(children);
        return vbox;
    }

    public static void main(final String[] args) {
        launch(args);
    }

    private static final class DragContext {

        private double mouseAnchorX;
        private double mouseAnchorY;
        private double initialTranslateX;
        private double initialTranslateY;
    }
}
