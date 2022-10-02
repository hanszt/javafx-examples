package hzt;

import hzt.model.Toroid;
import hzt.model.ToroidSliderPane;
import hzt.service.IMouseControlService;
import hzt.service.MouseControlService;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.FxUtils;

import static java.util.function.Predicate.not;

public class Fx3DToroidSample extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(Fx3DToroidSample.class);

    private Scene createScene() {
        var toroid = getToroid();
        var toroidContainer = new Group(toroid);
        toroidContainer.setTranslateX(-50);
        var toroidSliderPane = new ToroidSliderPane(toroid);
        toroidSliderPane.setTranslateX(50);
        toroidSliderPane.setTranslateY(-200);
        toroidSliderPane.setTranslateZ(750);
        Group root = new Group(toroidContainer, toroidSliderPane);
        return buildScene(root);
    }

    private static Scene buildScene(Group root) {
        Scene scene = new Scene(root, 1280, 720, true);
        scene.setOnKeyTyped(keyEvent -> keyTypedAction(keyEvent, root));
        root.getChildren().stream()
                .filter(not(Slider.class::isInstance))
                .forEach(model -> addMouseControl(model, scene));

        PerspectiveCamera camera = getPerspectiveCamera();
        camera.setTranslateZ(-300);
        scene.setCamera(camera);
        return scene;
    }

    private static PerspectiveCamera getPerspectiveCamera() {
        PerspectiveCamera camera = new PerspectiveCamera(true);
        camera.setFarClip(10_000);
        camera.setTranslateZ(-100);
        return camera;
    }

    private static void addMouseControl(Node model3D, Scene scene) {
        IMouseControlService mouseControlService = new MouseControlService();
        mouseControlService.initMouseControl(model3D, scene);
        mouseControlService.setOrientation(20, 20);
    }

    private static void keyTypedAction(KeyEvent keyEvent, Parent root) {
        if ("w".equals(keyEvent.getCharacter())) {
            FxUtils.allDescendents(root).stream()
                    .filter(MeshView.class::isInstance)
                    .map(MeshView.class::cast)
                    .forEach(Fx3DToroidSample::setDrawMode);
        } else if ("r".equals(keyEvent.getCharacter())) {
            root.getChildrenUnmodifiable().stream()
                    .filter(ToroidSliderPane.class::isInstance)
                    .map(ToroidSliderPane.class::cast)
                    .forEach(ToroidSliderPane::reset);
        } else {
            LOGGER.info("{} is not a registered key", keyEvent.getCharacter());
        }
    }

    private static void setDrawMode(MeshView m) {
        m.setDrawMode(m.getDrawMode() == DrawMode.LINE ? DrawMode.FILL : DrawMode.LINE);
    }


    private Toroid getToroid() {
        var toroid = new Toroid();
        var material = toroid.getPhongMaterial();
        material.setSpecularColor(Color.WHITE);
        material.setSpecularPower(64);
        var diffuseMapInputStream = getClass().getResourceAsStream("/images/cup_diffuseMap_1024.png");
        var bumpMapInputStream = getClass().getResourceAsStream("/images/cup_normalMap_1024.png");
        if (diffuseMapInputStream != null && bumpMapInputStream != null) {
            toroid.setTexture(new Image(diffuseMapInputStream), new Image(bumpMapInputStream));
        }
        toroid.setRotationAxis(Rotate.Z_AXIS);
        toroid.setRotate(90);
        return toroid;
    }

    @Override
    public void start(Stage stage) {
        LOGGER.info("Starting {}...", Fx3DToroidSample.class.getSimpleName());
        stage.setScene(createScene());
        stage.show();
        LOGGER.info("{} started", Fx3DToroidSample.class.getSimpleName());
    }
}
