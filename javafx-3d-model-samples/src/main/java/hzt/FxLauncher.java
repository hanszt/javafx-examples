package hzt;

import fx_customs.MyStage;
import javafx.application.Application;
import javafx.stage.Stage;

import static hzt.ModelLoadAppFactory.createScene;

/**
 * The JavaFX runtime components are missing, and are required to run this application is omitted by launching the app via this class
 * @see <a href="https://stackoverflow.com/questions/61033077/how-do-i-install-and-forget-java-9-modules-on-windows">
 * How do I install and forget java 9 modules</a>
 */
public class FxLauncher {

    public static void main(String[] args) {
        ModelLoadApp.main(args);
    }

    public static class ModelLoadApp extends Application {

        @Override
        public void start(Stage stage) {
            final var myStage = new MyStage();
            myStage.setScene(createScene());
            myStage.show();
        }

        public static void main(String[] args) {
            launch(args);
        }
    }
}
