/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.fxyz.extras;

import javafx.animation.AnimationTimer;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.transform.Affine;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;

/**
 * @author Dub
 */
public class Skybox extends Group {

    public enum SkyboxImageType {
        MULTIPLE, SINGLE
    }

    private final Affine affine = new Affine();

    private final ImageView top = new ImageView();
    private final ImageView bottom = new ImageView();
    private final ImageView left = new ImageView();
    private final ImageView right = new ImageView();
    private final ImageView back = new ImageView();
    private final ImageView front = new ImageView();

    private final ImageView[] views = new ImageView[]{
            top,
            left,
            back,
            right,
            front,
            bottom
    };

    private final PerspectiveCamera camera;
    private final SkyboxImageType imageType;

    public Skybox(double size, PerspectiveCamera camera, SkyboxImageType imageType) {
        super();
        top.setId("top ");
        bottom.setId("bottom ");
        left.setId("left ");
        right.setId("right ");
        back.setId("back ");
        front.setId("front ");
        this.imageType = imageType;

        this.size.set(size);
        this.camera = camera;

        getTransforms().add(affine);

        loadImageViews();

        super.getChildren().addAll(views);
        startTimer();
    }

    private void loadImageViews() {
        for (ImageView iv : views) {
            iv.setSmooth(true);
            iv.setPreserveRatio(true);
        }
        validateImageType();
    }

    private void layoutViews() {

        for (ImageView v : views) {
            v.setFitWidth(getSize());
            v.setFitHeight(getSize());
        }

        back.setTranslateX(-0.5 * getSize());
        back.setTranslateY(-0.5 * getSize());
        back.setTranslateZ(-0.5 * getSize());

        front.setTranslateX(-0.5 * getSize());
        front.setTranslateY(-0.5 * getSize());
        front.setTranslateZ(0.5 * getSize());
        front.setRotationAxis(Rotate.Z_AXIS);
        front.setRotate(-180);
        front.getTransforms().add(new Rotate(180, front.getFitHeight() / 2, 0, 0, Rotate.X_AXIS));
        front.setTranslateY(front.getTranslateY() - getSize());

        top.setTranslateX(-0.5 * getSize());
        top.setTranslateY(-1 * getSize());
        top.setRotationAxis(Rotate.X_AXIS);
        top.setRotate(-90);

        bottom.setTranslateX(-0.5 * getSize());
        bottom.setTranslateY(0);
        bottom.setRotationAxis(Rotate.X_AXIS);
        bottom.setRotate(90);

        left.setTranslateX(-1 * getSize());
        left.setTranslateY(-0.5 * getSize());
        left.setRotationAxis(Rotate.Y_AXIS);
        left.setRotate(90);

        right.setTranslateX(0);
        right.setTranslateY(-0.5 * getSize());
        right.setRotationAxis(Rotate.Y_AXIS);
        right.setRotate(-90);

    }

    /**
     * for single image creates viewports and sets all views(image) to singleImg
     * for multiple... sets images per view.
     */
    private void validateImageType() {
        if (imageType == SkyboxImageType.MULTIPLE) {
            setMultipleImages();
        }
    }

    /**
     * this will layout the viewports to this style pattern
     * ____
     * |top |
     * ____|____|____ ____
     * |left|fwd |rght|back|
     * |____|____|____|____|
     * |bot |
     * |____|
     * @param singleImg the loaded image
     */
    private void loadSingleImageViewports(Image singleImg) {
        layoutViews();
        double width = singleImg.getWidth();
        double height = singleImg.getHeight();

        //simple chack to see if cells will be square
        if (width / 4 != height / 3) {
            throw new UnsupportedOperationException("Image does not comply with size constraints");
        }
        double cellSize = singleImg.getWidth() - singleImg.getHeight();

        recalculateSize(cellSize);

        double topx = cellSize;
        double topy = 0;
        double botx = cellSize;
        double boty = cellSize * 2;
        double leftx = 0;
        double lefty = cellSize;
        double rightx = cellSize * 2;
        double righty = cellSize;
        double fwdx = cellSize;
        double fwdy = cellSize;
        double backx = cellSize * 3;
        double backy = cellSize;

        //add top padding x+, y+, width-, height
        top.setViewport(new Rectangle2D(topx, topy, cellSize, cellSize));

        //add left padding x, y+, width, height-
        left.setViewport(new Rectangle2D(leftx, lefty, cellSize - 1, cellSize - 1));

        //add front padding x+, y+, width-, height
        back.setViewport(new Rectangle2D(fwdx, fwdy, cellSize, cellSize));

        //add right padding x, y+, width, height-
        right.setViewport(new Rectangle2D(rightx, righty, cellSize, cellSize));

        //add back padding x, y+, width, height-
        front.setViewport(new Rectangle2D(backx + 1, backy - 1, cellSize - 1, cellSize - 1));

        //add bottom padding x+, y, width-, height-
        bottom.setViewport(new Rectangle2D(botx, boty, cellSize, cellSize));

    }

    private void recalculateSize(double cell) {
        double factor = Math.floor(getSize() / cell);
        setSize(cell * factor);
    }

    private void setMultipleImages() {
        layoutViews();
    }

    private void startTimer() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                Transform ct = (camera != null) ? camera.getLocalToSceneTransform() : null;
                if (ct != null) {
                    affine.setTx(ct.getTx());
                    affine.setTy(ct.getTy());
                    affine.setTz(ct.getTz());
                }
            }
        };
        timer.start();
    }

    /*
        Properties
    */
    private final DoubleProperty size = new SimpleDoubleProperty() {
        @Override
        protected void invalidated() {
            switch (imageType) {
                case SINGLE:
                    layoutViews();
                    break;
                case MULTIPLE:
                    break;
            }

        }
    };

    public void setImageTop(Image image) {
        top.setImage(image);
    }

    public void setImageBottom(Image image) {
        top.setImage(image);
    }

    public void setImageLeft(Image image) {
        top.setImage(image);
    }

    public void setImageRight(Image image) {
        top.setImage(image);
    }

    public void setImageFront(Image image) {
        top.setImage(image);
    }

    public void setImageBack(Image image) {
        top.setImage(image);
    }

    public void setImageAllSides(Image image) {
        for (ImageView v : views) {
            v.setImage(image);
        }
        loadSingleImageViewports(image);
    }

    public final double getSize() {
        return size.get();
    }

    public final void setSize(double value) {
        size.set(value);
    }

    public DoubleProperty sizeProperty() {
        return size;
    }

}
