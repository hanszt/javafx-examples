package org.fxyz.tests;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.ToDoubleFunction;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.AmbientLight;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import org.fxyz.cameras.CameraTransformer;
import org.fxyz.geometry.Point3D;
import org.fxyz.shapes.primitives.BezierMesh;
import org.fxyz.shapes.primitives.PrismMesh;
import org.fxyz.shapes.primitives.helper.InterpolateBezier;
import org.fxyz.utils.CameraUtils;

/**
 * @author jpereda
 */
public final class BezierTest extends Application {
    private final PerspectiveCamera camera = new PerspectiveCamera(true);
    private final CameraTransformer cameraTransform = new CameraTransformer();

    private double mousePosX;
    private double mousePosY;
    private double mouseOldX;
    private double mouseOldY;
    private double mouseDeltaX;
    private double mouseDeltaY;
    private ArrayList<BezierMesh> beziers;
    private Rotate rotateY;
    private ToDoubleFunction<Double> func = t -> (double) t;
    private long lastEffect;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group sceneRoot = new Group();
        double sceneWidth = 600;
        double sceneHeight = 600;
        Scene scene = new Scene(sceneRoot, sceneWidth, sceneHeight, true, SceneAntialiasing.BALANCED);
        scene.setFill(Color.BLACK);

        //setup camera transform for rotational support
        cameraTransform.setTranslate(0, 0, 0);
        cameraTransform.getChildren().add(camera);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setTranslateZ(-10);
        cameraTransform.rotateY.setAngle(-45.0);
        cameraTransform.rotateX.setAngle(-10.0);
        //add a Point Light for better viewing of the grid coordinate system
        PointLight light = new PointLight(Color.WHITE);
        cameraTransform.getChildren().add(light);
        cameraTransform.getChildren().add(new AmbientLight(Color.WHITE));
        light.setTranslateX(camera.getTranslateX());
        light.setTranslateY(camera.getTranslateY());
        light.setTranslateZ(camera.getTranslateZ());
        scene.setCamera(camera);

        rotateY = new Rotate(0, 0, 0, 0, Rotate.Y_AXIS);
        Group group = new Group();
        group.getChildren().add(cameraTransform);

//        List<Point3D> knots=Arrays.asList(new Point3D(0f,0f,0f),new Point3D(3f,0f,2f),
//                new Point3D(5f,2f,3f),new Point3D(7f,-3f,0f),new Point3D(6f,-1f,-4f));
        List<Point3D> knots = Arrays.asList(new Point3D(3f, 0f, 0f), new Point3D(0.77171f, 1.68981f, 0.989821f),
                new Point3D(-0.681387f, 0.786363f, -0.281733f), new Point3D(-2.31757f, -0.680501f, -0.909632f),
                new Point3D(-0.404353f, -2.81233f, 0.540641f), new Point3D(1.1316f, -0.727237f, 0.75575f),
                new Point3D(1.1316f, 0.727237f, -0.75575f), new Point3D(-0.404353f, 2.81233f, -0.540641f),
                new Point3D(-2.31757f, 0.680501f, 0.909632f), new Point3D(-0.681387f, -0.786363f, 0.281733f),
                new Point3D(0.77171f, -1.68981f, -0.989821f), new Point3D(3f, 0f, 0f));

        boolean showControlPoints = true;
        boolean showKnots = true;

        InterpolateBezier interpolate = new InterpolateBezier(knots);
        beziers = new ArrayList<>();
        if (showKnots || showControlPoints) {
            interpolate.getSplines().forEach(spline -> {
                Point3D k0 = spline.getPoints().get(0);
                Point3D k1 = spline.getPoints().get(1);
                Point3D k2 = spline.getPoints().get(2);
                Point3D k3 = spline.getPoints().get(3);
                if (showKnots) {
                    Sphere s = new Sphere(0.2d);
                    s.getTransforms().add(new Translate(k0.getX(), k0.getY(), k0.getZ()));
                    s.setMaterial(new PhongMaterial(Color.GREENYELLOW));
                    group.getChildren().add(s);
                    s = new Sphere(0.2d);
                    s.getTransforms().add(new Translate(k3.getX(), k3.getY(), k3.getZ()));
                    s.setMaterial(new PhongMaterial(Color.GREENYELLOW));
                    group.getChildren().add(s);
                }
                if (showControlPoints) {
                    PrismMesh c = new PrismMesh(0.03d, 1d, 1, k0, k1);
                    c.setTextureModeNone(Color.GREEN);
                    group.getChildren().add(c);

                    c = new PrismMesh(0.03d, 1d, 1, k1, k2);
                    c.setTextureModeNone(Color.GREEN);
                    group.getChildren().add(c);

                    c = new PrismMesh(0.03d, 1d, 1, k2, k3);
                    c.setTextureModeNone(Color.GREEN);
                    group.getChildren().add(c);

                    Sphere s = new Sphere(0.1d);
                    s.getTransforms().add(new Translate(k1.getX(), k1.getY(), k1.getZ()));
                    s.setMaterial(new PhongMaterial(Color.RED));
                    group.getChildren().add(s);
                    s = new Sphere(0.1d);
                    s.getTransforms().add(new Translate(k2.getX(), k2.getY(), k2.getZ()));
                    s.setMaterial(new PhongMaterial(Color.RED));
                    group.getChildren().add(s);
                }
            });
        }
        long time = System.currentTimeMillis();
        interpolate.getSplines().forEach(spline -> {
            BezierMesh bezier = new BezierMesh(spline, 0.1d,
                    300, 20, 0, 0);
//            bezier.setDrawMode(DrawMode.LINE);
//            bezier.setCullFace(CullFace.NONE);
//            bezier.setSectionType(SectionType.TRIANGLE);

            // NONE
//            bezier.setTextureModeNone(Color.hsb(360d*sp.getAndIncrement()/interpolate.getSplines().size(), 1, 1));
            // IMAGE
//            bezier.setTextureModeImage(getClass().getResource("res/LaminateSteel.jpg").toExternalForm());
            // PATTERN
//           bezier.setTextureModePattern(3d);
            // FUNCTION
            bezier.setTextureModeVertices1D(1530, t -> spline.getKappa(t));
//            bezier.setTextureModeVertices1D(1530,func);
            // DENSITY
//            bezier.setTextureModeVertices3D(256*256,dens);
            // FACES
//            bezier.setTextureModeFaces(256*256);

            bezier.getTransforms().addAll(new Rotate(0, Rotate.X_AXIS), rotateY);
            beziers.add(bezier);
        });
        System.out.println("time: " + (System.currentTimeMillis() - time)); //43.815->25.606->15
        group.getChildren().addAll(beziers);

        sceneRoot.getChildren().addAll(group);

        //First person shooter keyboard movement 
        scene.setOnKeyPressed(event -> CameraUtils.moveInXZPlane(camera, event));

        scene.setOnMousePressed((MouseEvent me) -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });
        scene.setOnMouseDragged((MouseEvent me) -> {
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseDeltaX = (mousePosX - mouseOldX);
            mouseDeltaY = (mousePosY - mouseOldY);

            double modifier = 10.0;
            double modifierFactor = 0.1;

            if (me.isControlDown()) {
                modifier = 0.1;
            }
            if (me.isShiftDown()) {
                modifier = 50.0;
            }
            if (me.isPrimaryButtonDown()) {
                cameraTransform.rotateY.setAngle(((cameraTransform.rotateY.getAngle() + mouseDeltaX * modifierFactor * modifier * 2.0) % 360 + 540) % 360 - 180);  // +
                cameraTransform.rotateX.setAngle(((cameraTransform.rotateX.getAngle() - mouseDeltaY * modifierFactor * modifier * 2.0) % 360 + 540) % 360 - 180);  // -
            } else if (me.isSecondaryButtonDown()) {
                double z = camera.getTranslateZ();
                double newZ = z + mouseDeltaX * modifierFactor * modifier;
                camera.setTranslateZ(newZ);
            } else if (me.isMiddleButtonDown()) {
                cameraTransform.translate.setX(cameraTransform.translate.getX() + mouseDeltaX * modifierFactor * modifier * 0.3);  // -
                cameraTransform.translate.setY(cameraTransform.translate.getY() + mouseDeltaY * modifierFactor * modifier * 0.3);  // -
            }
        });

        lastEffect = System.nanoTime();
        AtomicInteger count = new AtomicInteger();
        AnimationTimer timerEffect = new AnimationTimer() {

            @Override
            public void handle(long now) {
                if (now > lastEffect + 1_000_000_000l) {
//                    Point3D loc = knot.getPositionAt((count.get()%100)*2d*Math.PI/100d);
//                    Point3D dir = knot.getTangentAt((count.get()%100)*2d*Math.PI/100d);
//                    cameraTransform.t.setX(loc.x);
//                    cameraTransform.t.setY(loc.y);
//                    cameraTransform.t.setZ(-loc.z);
//                    javafx.geometry.Point3D axis = cameraTransform.rx.getAxis();
//                    javafx.geometry.Point3D cross = axis.crossProduct(-dir.x,-dir.y,-dir.z);
//                    double angle = axis.angle(-dir.x,-dir.y,-dir.z);
//                    cameraTransform.rx.setAngle(angle);
//                    cameraTransform.rx.setAxis(new javafx.geometry.Point3D(cross.getX(),-cross.getY(),cross.getZ()));
//                    dens = p->(float)(p.x*Math.cos(count.get()%100d*2d*Math.PI/50d)+p.y*Math.sin(count.get()%100d*2d*Math.PI/50d));
                    func = t -> Math.pow(t, (count.get() % 5d));
                    beziers.forEach(b -> b.setFunction(func));
//                    knot.setP(1+(count.get()%5));
//                    knot.setQ(2+(count.get()%15));

//                    if(count.get()%100<50){
//                        knot.setDrawMode(DrawMode.LINE);
//                    } else {
//                        knot.setDrawMode(DrawMode.FILL);
//                    }
//                    beziers.forEach(b->b.setColors((int)Math.pow(2,count.get()%16)));
//                    beziers.forEach(b->b.setWireRadius(0.1d+(count.get()%6)/10d));
//                    beziers.forEach(b->b.setPatternScale(1d+(count.get()%10)*3d));
//                    beziers.forEach(b->b.setSectionType(SectionType.values()[count.get()%SectionType.values().length]));
                    count.getAndIncrement();
                    lastEffect = now;
                }
            }
        };


        primaryStage.setTitle("F(X)yz - Bezier Splines");
        primaryStage.setScene(scene);
        primaryStage.show();

        timerEffect.start();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
