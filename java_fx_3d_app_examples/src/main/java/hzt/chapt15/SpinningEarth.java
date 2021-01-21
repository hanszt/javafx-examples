package hzt.chapt15;

import hzt.Application3D;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

import static hzt.Application3D.HEIGHT;
import static hzt.Application3D.WIDTH;

/**
 * @author afsal villan
 * @version 1.0
 *
 * http://www.genuinecoder.com
 */
public class SpinningEarth extends Application {

  private final Sphere sphere = new Sphere(150);
  
  @Override
  public void start(Stage primaryStage) {
    Camera camera = new PerspectiveCamera(true);
    camera.setNearClip(1);
    camera.setFarClip(10_000);
    camera.translateZProperty().set(-1000);

    Group world = new Group();
    world.getChildren().add(prepareEarth());

    Scene scene = new Scene(world, WIDTH, HEIGHT, true);
    scene.setFill(Color.SILVER);
    scene.setCamera(camera);

    new Application3D().initMouseControl(world, scene);

    primaryStage.setTitle("Genuine Coder");
    primaryStage.setScene(scene);
    primaryStage.show();

    prepareAnimation();
  }

  private void prepareAnimation() {
    AnimationTimer timer = new AnimationTimer() {
      @Override
      public void handle(long now) {
        sphere.rotateProperty().set(sphere.getRotate() + 0.2);
      }
    };
    timer.start();
  }

  private Node prepareEarth() {
    PhongMaterial earthMaterial = new PhongMaterial();
    earthMaterial.setDiffuseMap(new Image(getClass().getResourceAsStream("../../earth/earth-d.jpg")));
    earthMaterial.setSelfIlluminationMap(new Image(getClass().getResourceAsStream("../../earth/earth-l.jpg")));
    earthMaterial.setSpecularMap(new Image(getClass().getResourceAsStream("../../earth/earth-s.jpg")));
    earthMaterial.setBumpMap(new Image(getClass().getResourceAsStream("../../earth/earth-n.jpg")));

    sphere.setRotationAxis(Rotate.Y_AXIS);
    sphere.setMaterial(earthMaterial);
    return sphere;
  }
}
