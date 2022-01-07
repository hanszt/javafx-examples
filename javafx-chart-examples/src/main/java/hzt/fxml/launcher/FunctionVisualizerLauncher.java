package hzt.fxml.launcher;

import hzt.utils.FxSetup;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class FunctionVisualizerLauncher extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(FunctionVisualizerLauncher.class);

    public static void main(String[] args) {
        Application.launch(FunctionVisualizerLauncher.class, args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        LOGGER.info("Starting {}...", FunctionVisualizerLauncher.class.getSimpleName());
        String resourceLocation = "/fxml/SizeApp.fxml";
        var resource = getClass().getResource(resourceLocation);
        final Parent root = resource != null ? FXMLLoader.load(resource) :
				new Group(new Label("Error: Could not load " + resourceLocation));
        final Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Analysis functions");

        stage.setMinHeight(250);
        stage.setMinWidth(500);

        String location = "/icons/fx-icon.png";
        Optional.ofNullable(FxSetup.class.getResourceAsStream(location))
				.map(Image::new)
				.ifPresentOrElse(stage.getIcons()::add,
						() -> LOGGER.error("Resource at {} not found...", location));

        stage.show();
        LOGGER.info("{} started", FunctionVisualizerLauncher.class.getSimpleName());
    }
}
