/*
 * Copyright (C) 2014 F(Y)zx :
 * Authored by : Jason Pollastrini aka jdub1581, 
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fxyz.shapes.complex.cloth;

import javafx.beans.property.*;
import javafx.collections.ObservableFloatArray;
import javafx.collections.ObservableIntegerArray;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.ObservableFaceArray;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.transform.Affine;
import javafx.util.Duration;
import org.fxyz.geometry.Point3D;
import org.fxyz.utils.FloatCollector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import static java.lang.Math.sqrt;

/**
 *
 * @author Jason Pollastrini aka jdub1581
 */
public class ClothMesh extends MeshView {

    /**
     * Static Default Variables
     */
    private static final int DEFAULT_DIVISIONS_X = 75;
    private static final int DEFAULT_DIVISIONS_Y = 35;

    private static final int DEFAULT_WIDTH = 600;
    private static final int DEFAULT_HEIGHT = 200;

    private static final double DEFAULT_BEND_STRENGTH = 0.85;
    private static final double DEFAULT_SHEAR_STRENGTH = 0.75;
    private static final double DEFAULT_STRETCH_STRENGTH = 0.55;

    private static final int DEFAULT_CONSTRAINT_ACCURACY = 8;
    private static final int DEFAULT_ITERATIONS = 5;
    
    private static final double DEFAULT_POINT_MASS = 1.0;
    private static final int DIVS_BOUNDARY = 4;

    private final ClothTimer timer = new ClothTimer();
    private final TriangleMesh mesh = new TriangleMesh();
    private final PhongMaterial material = new PhongMaterial();
    private final List<WeightedPoint> points = new ArrayList<>();

    private int[] calculateFaceValues(int index, TriangleMesh mesh) {
        if (index > ((mesh.getFaces().size() - 1) - mesh.getFaceElementSize())) {
            return new int[0];
        }
        if (index > 0) {
            index = (index * 6);
            return mesh.getFaces().toArray(index, null, 6);
        }
        return mesh.getFaces().toArray(index, null, index + 6);
    }

    /**
     * Builds a ClothMesh with default settings
     */
    public ClothMesh() {
        this(
                DEFAULT_DIVISIONS_X,
                DEFAULT_DIVISIONS_Y,
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT,
                DEFAULT_BEND_STRENGTH,
                DEFAULT_SHEAR_STRENGTH,
                DEFAULT_STRETCH_STRENGTH
        );
    }

    /**
     * Builds a ClothMesh with width and height; defaults others
     *
     * @param width the width of the cloth
     * @param height the height of the cloth
     */
    public ClothMesh(double width, double height) {
        this(
                DEFAULT_DIVISIONS_X,
                DEFAULT_DIVISIONS_Y,
                width,
                height,
                DEFAULT_BEND_STRENGTH,
                DEFAULT_SHEAR_STRENGTH,
                DEFAULT_STRETCH_STRENGTH
        );
    }

    /**
     * Builds a ClothMesh with divsX, divsY; defaults others
     *
     * @param dx dx
     * @param dy dy
     */
    public ClothMesh(int dx, int dy) {
        this(
                dx,
                dy,
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT,
                DEFAULT_BEND_STRENGTH,
                DEFAULT_SHEAR_STRENGTH,
                DEFAULT_STRETCH_STRENGTH
        );

    }

    /**
     * Builds a ClothMesh with divsX, divsY, width and height; defaults others
     *
     * @param dx dx
     * @param dy dy
     * @param width the width
     * @param height the height
     */
    public ClothMesh(int dx, int dy, double width, double height) {
        this(
                dx,
                dy,
                width,
                height,
                DEFAULT_BEND_STRENGTH,
                DEFAULT_SHEAR_STRENGTH,
                DEFAULT_STRETCH_STRENGTH
        );
    }

    /**
     * Builds a ClothMesh with divsX, divsY, width and height, stretchStrength;
     * defaults others
     *
     * @param dx dx
     * @param dy dy
     * @param width the width
     * @param height the height
     * @param stretch the stretch
     */
    public ClothMesh(int dx, int dy, double width, double height, double stretch) {
        this(
                dx,
                dy,
                width,
                height,
                DEFAULT_BEND_STRENGTH,
                DEFAULT_SHEAR_STRENGTH,
                stretch
        );
    }

    /**
     * Builds a ClothMesh using settings
     *
     * @param divsX divisions along X axis
     * @param divsY divisions along Y axis
     * @param width requested width
     * @param height requested height
     * @param bendStr strength of bend links
     * @param shearStr strength of shear links
     * @param stretchStr strength of stretch links
     */
    public ClothMesh(int divsX, int divsY, double width, double height, double bendStr, double shearStr, double stretchStr) {
        if (divsX >= DIVS_BOUNDARY) {
            setDivisionsX(divsX);
        }
        if (divsY >= DIVS_BOUNDARY) {
            setDivisionsY(divsY);
        }

        this.setWidth(width);
        this.setHeight(height);

        this.setStretchStrength(stretchStr);
        this.setBendStrength(bendStr);
        this.setShearStrength(shearStr);
        this.getTransforms().add(new Affine());
        
        this.buildMesh(getDivisionsX(), getDivisionsY(), getWidth(), getHeight(), isUsingShearLinks(), isUsingBendingLinks());        
        
        this.setOnMousePressed(this::mousePressedAction);
    }

    private void mousePressedAction(MouseEvent me) {
        if (me.isPrimaryButtonDown()) {
            PickResult pr = me.getPickResult();
            if (pr.getIntersectedFace() != -1) {
                int[] vals = calculateFaceValues(pr.getIntersectedFace(), mesh);
                if (me.isControlDown()) {
                    points.get(vals[0]).setOldPosition(points.get(vals[0]).getOldPosition().add(0, 0, 25));
                    points.get(vals[2]).setOldPosition(points.get(vals[2]).getOldPosition().add(0, 0, 25));
                    points.get(vals[4]).setOldPosition(points.get(vals[4]).getOldPosition().add(0, 0, 25));
                } else {
                    points.get(vals[0]).setOldPosition(points.get(vals[0]).getOldPosition().add(0, 0, -25));
                    points.get(vals[2]).setOldPosition(points.get(vals[2]).getOldPosition().add(0, 0, -25));
                    points.get(vals[4]).setOldPosition(points.get(vals[4]).getOldPosition().add(0, 0, -25));
                }
            }
        }
    }

    /*==========================================================================
     Updating Methods
     */

    /**
     *
     */
    private void updatePoints() {
        float[] pts = this.points.stream()
                .flatMapToDouble(wp -> wp.getPosition().getCoordinates())
                .collect(() -> new FloatCollector(this.points.size() * 3), FloatCollector::add, FloatCollector::join)
                .toArray();

        mesh.getPoints().setAll(pts, 0, pts.length);
    }

    /**
     *
     */
    public void updateUI() {
        updatePoints();
    }
    /*==========================================================================
     Mesh Creation
     *///=======================================================================

    /**
     * @param divsX number of points along X axis
     * @param divsY number of points along Y axis
     * @param width desired Width of Mesh
     * @param height desired Height of Mesh
     * @param shear constraint elasticity / stiffness
     */
    private void buildMesh(int divsX, int divsY, double width, double height, boolean shear, boolean bend) {
        int sDivX = (divsX - 1);
        int sDivY = (divsY - 1);
        double xDist = (width / divsX);
        double yDist = (height / divsY);
        buildPointsAndTexCoords(divsX, width, height, sDivX, sDivY, xDist, yDist);
        if (shear) {
            createShearLinks(divsX, divsY, sDivX, sDivY, xDist, yDist);
        }
        if (bend) {
            createBendLinks(divsX, divsY, sDivX, sDivY, xDist);
        }
        buildFaces(sDivX, sDivY);
        setMesh(mesh);
        setMaterial(material);
    }

    private void buildPointsAndTexCoords(int divsX, double width, double height, int sDivX, int sDivY, double xDist, double yDist) {
        float minX = (float) (-width / 2f);
        float maxX = (float) (width / 2f);
        float minY = (float) (-height / 2f);
        float maxY = (float) (height / 2f);
        for (int Y = 0; Y <= sDivY; Y++) {

            float currY = (float) Y / sDivY;
            float fy = (1 - currY) * minY + currY * maxY;

            for (int X = 0; X <= sDivX; X++) {

                float currX = (float) X / sDivX;
                float fx = (1 - currX) * minX + currX * maxX;

                var point = pinPointsInPlace(sDivY, Y, X, fx, fy);
                stabilizeLinks(divsX, xDist, yDist, Y, X, point);

                points.add(point);
                mesh.getPoints().addAll(point.getPosition().getX(), point.getPosition().getY(), point.getPosition().getZ());
                mesh.getTexCoords().addAll(currX, currY);
            }
        }
    }

    private void stabilizeLinks(int divsX, double xDist, double yDist, int y, int x, WeightedPoint p) {
        if (x != 0) {
            p.attatchTo((points.get(points.size() - 1)), xDist, getStretchStrength());
        }
        if (y != 0) {
            p.attatchTo((points.get((y - 1) * divsX + x)), yDist, getStretchStrength());
        }
    }

    private WeightedPoint pinPointsInPlace(int sDivY, int y, int x, float fx, float fy) {
        boolean setMass = ((y < 5) && (x == 0)) || ((y > sDivY - 5) && x == 0);
        double mass = setMass ? 100 : getPerPointMass();
        var weightedPoint = new WeightedPoint(mass, fx, fy, Math.random());
        if (y == 0 && x == 0 || (x == 0 && y == sDivY)) {
            weightedPoint.setAnchored(true);
            weightedPoint.setForceAffected(false);
        } else {
            weightedPoint.setForceAffected(true);
        }
        return weightedPoint;
    }

    private void buildFaces(int sDivX, int sDivY) {
        for (int Y = 0; Y < sDivY; Y++) {
            for (int X = 0; X < sDivX; X++) {
                int p00 = Y * (sDivX + 1) + X;
                int p01 = p00 + 1;
                int p10 = p00 + (sDivX + 1);
                int p11 = p10 + 1;
                int tc00 = Y * (sDivX + 1) + X;
                int tc01 = tc00 + 1;
                int tc10 = tc00 + (sDivX + 1);
                int tc11 = tc10 + 1;
                mesh.getFaces().addAll(p00, tc00, p10, tc10, p11, tc11);
                mesh.getFaces().addAll(p11, tc11, p01, tc01, p00, tc00);
            }
        }
    }

    private void createBendLinks(int divsX, int divsY, int sDivX, int sDivY, double xDist) {
        for (int Y = 0; Y <= sDivY; Y++) {
            for (int X = 0; X <= sDivX; X++) {
                WeightedPoint p = points.get(Y * divsX + X);
                //skip every other
                if (X < (divsX - 2)) {
                    p.attatchTo((points.get((Y * divsX + (X + 2)))), xDist * 2, getBendStrength());
                }
                if (Y < (divsY - 2)) {
                    p.attatchTo((points.get((Y + 2) * divsX + X)), xDist * 2, getBendStrength());
                }
                p.setOldPosition(p.getPosition());
            }
        }
    }

    private void createShearLinks(int divsX, int divsY, int sDivX, int sDivY, double xDist, double yDist) {
        for (int Y = 0; Y <= sDivY; Y++) {
            for (int X = 0; X <= sDivX; X++) {
                WeightedPoint p = points.get(Y * divsX + X);
                // top left(xy) to right(xy + 1)
                final double sqrt = sqrt((xDist * xDist) + (yDist * yDist));
                if (X < (divsX - 1) && Y < (divsY - 1)) {
                    p.attatchTo((points.get(((Y + 1) * divsX + (X + 1)))), sqrt, getShearStrength());
                }
                // index(xy) to left(x - 1(y + 1))
                if (Y != 0 && X != (divsX - 1)) {
                    p.attatchTo((points.get(((Y - 1) * divsX + (X + 1)))), sqrt, getShearStrength());
                }
            }
        }
    }

    /*==========================================================================
     Properties
     *///=======================================================================
    private final DoubleProperty width = new SimpleDoubleProperty(this, "width", DEFAULT_WIDTH) {
        @Override
        protected void invalidated() {
            //must not be invalidated
        }
    };

    public final double getWidth() {
        return width.get();
    }

    public final void setWidth(double value) {
        width.set(value);
    }

    public DoubleProperty widthProperty() {
        return width;
    }
    //==========================================================================
    private final DoubleProperty height = new SimpleDoubleProperty(this, "height", DEFAULT_HEIGHT) {
        @Override
        protected void invalidated() {
            //must not be invalidated
        }
    };

    public final double getHeight() {
        return height.get();
    }

    public final void setHeight(double value) {
        height.set(value);
    }

    public DoubleProperty heightProperty() {
        return height;
    }
    //==========================================================================
    private final IntegerProperty divisionsX = new SimpleIntegerProperty(this, "divisionsX", DEFAULT_DIVISIONS_X) {
        @Override
        protected void invalidated() {
            //must not be invalidated
        }
    };

    public final int getDivisionsX() {
        return divisionsX.get();
    }

    public final void setDivisionsX(int value) {
        divisionsX.set(value);
    }

    //==========================================================================
    private final IntegerProperty divisionsY = new SimpleIntegerProperty(this, "divisionsY", DEFAULT_DIVISIONS_Y) {
        @Override
        protected void invalidated() {
            //must not be invalidated
        }
    };

    public final int getDivisionsY() {
        return divisionsY.get();
    }

    public final void setDivisionsY(int value) {
        divisionsY.set(value);
    }

    public final IntegerProperty divisionsYProperty() {
        return divisionsY;
    }
    /*==========================================================================
     Constraint Strengths
     */
    private final DoubleProperty stretchStrength = new SimpleDoubleProperty(this, "stretchStrength", DEFAULT_STRETCH_STRENGTH) {
        @Override
        protected void invalidated() {
            //must not be invalidated
        }
    };

    public final double getStretchStrength() {
        return stretchStrength.get();
    }

    public final void setStretchStrength(double value) {
        stretchStrength.set(value);
    }

    public final DoubleProperty stretchStrengthProperty() {
        return stretchStrength;
    }
    //==========================================================================
    private final DoubleProperty shearStrength = new SimpleDoubleProperty(this, "shearStrength", DEFAULT_SHEAR_STRENGTH) {
        @Override
        protected void invalidated() {
            //must not be invalidated
        }
    };

    public final double getShearStrength() {
        return shearStrength.get();
    }

    public final void setShearStrength(double value) {
        shearStrength.set(value);
    }

    public final DoubleProperty shearStrengthProperty() {
        return shearStrength;
    }
    //==========================================================================
    private final DoubleProperty bendStrength = new SimpleDoubleProperty(this, "bendStrength", DEFAULT_BEND_STRENGTH) {
        @Override
        protected void invalidated() {
            //must not be invalidated
        }
    };

    public final double getBendStrength() {
        return bendStrength.get();
    }

    public final void setBendStrength(double value) {
        bendStrength.set(value);
    }

    public final DoubleProperty bendStrengthProperty() {
        return bendStrength;
    }
    /*==========================================================================
     Use Constraints?
     */
    private final BooleanProperty useBendingLinks = new SimpleBooleanProperty(this, "useBendingLinks", true) {
        @Override
        protected void invalidated() {
            //must not be invalidated
        }
    };

    public final boolean isUsingBendingLinks() {
        return useBendingLinks.get();
    }

    public final void setUseBendingLinks(boolean value) {
        useBendingLinks.set(value);
    }

    public final BooleanProperty usingBendingLinksProperty() {
        return useBendingLinks;
    }
    //==========================================================================
    private final BooleanProperty useShearLinks = new SimpleBooleanProperty(this, "useShearLinks", true) {
        @Override
        protected void invalidated() {
            //must not be invalidated
        }
    };

    public final boolean isUsingShearLinks() {
        return useShearLinks.get();
    }

    public final void setUseShearLinks(boolean value) {
        useShearLinks.set(value);
    }

    public final BooleanProperty usingShearLinksProperty() {
        return useShearLinks;
    }

    /*==========================================================================
     Timer related Properties
     */
    private final IntegerProperty constraintAccuracy = new SimpleIntegerProperty(this, "constraintAccuracy", DEFAULT_CONSTRAINT_ACCURACY);

    public final int getConstraintAccuracy() {
        return constraintAccuracy.get();
    }

    public final void setConstraintAccuracy(int value) {
        constraintAccuracy.set(value);
    }

    public final IntegerProperty constraintAccuracyProperty() {
        return constraintAccuracy;
    }
    //==========================================================================
    private final IntegerProperty iterations = new SimpleIntegerProperty(this, "iterations", DEFAULT_ITERATIONS);

    public final int getIterations() {
        return iterations.get();
    }

    public final void setIterations(int value) {
        iterations.set(value);
    }

    public final IntegerProperty iterationsProperty() {
        return iterations;
    }
    
    /**=========================================================================
     * Starts the Cloth Simulation
     */
    public final void startSimulation() {
        if (!timer.isRunning()) {
            timer.start();
        }
    }

    /**
     * Pauses the Cloth Simulation
     */
    public final void pauseSimulation() {
        timer.pause();
    }

    /**
     * Stops the Cloth Simulation
     */
    public final void stopSimulation() {
        timer.cancel();
    }

    /*==========================================================================
     *   Material Delegates                                                     *
     *///========================================================================
    public final void setDiffuseColor(Color value) {
        material.setDiffuseColor(value);
    }

    public final Color getDiffuseColor() {
        return material.getDiffuseColor();
    }

    public final ObjectProperty<Color> diffuseColorProperty() {
        return material.diffuseColorProperty();
    }

    public final void setSpecularColor(Color value) {
        material.setSpecularColor(value);
    }

    public final Color getSpecularColor() {
        return material.getSpecularColor();
    }

    public final ObjectProperty<Color> specularColorProperty() {
        return material.specularColorProperty();
    }

    public final void setSpecularPower(double value) {
        material.setSpecularPower(value);
    }

    public final double getSpecularPower() {
        return material.getSpecularPower();
    }

    public final DoubleProperty specularPowerProperty() {
        return material.specularPowerProperty();
    }

    public final void setDiffuseMap(Image value) {
        material.setDiffuseMap(value);
    }

    public final Image getDiffuseMap() {
        return material.getDiffuseMap();
    }

    public final ObjectProperty<Image> diffuseMapProperty() {
        return material.diffuseMapProperty();
    }

    public final void setSpecularMap(Image value) {
        material.setSpecularMap(value);
    }

    public final Image getSpecularMap() {
        return material.getSpecularMap();
    }

    public final ObjectProperty<Image> specularMapProperty() {
        return material.specularMapProperty();
    }

    public final void setBumpMap(Image value) {
        material.setBumpMap(value);
    }

    public final Image getBumpMap() {
        return material.getBumpMap();
    }

    public final ObjectProperty<Image> bumpMapProperty() {
        return material.bumpMapProperty();
    }

    public final void setSelfIlluminationMap(Image value) {
        material.setSelfIlluminationMap(value);
    }

    public final Image getSelfIlluminationMap() {
        return material.getSelfIlluminationMap();
    }

    public final ObjectProperty<Image> selfIlluminationMapProperty() {
        return material.selfIlluminationMapProperty();
    }

    /*==========================================================================
     *   TriangleMesh Data
     */
    public final ObservableFloatArray getPoints() {
        return mesh.getPoints();
    }

    public final ObservableFloatArray getTexCoords() {
        return mesh.getTexCoords();
    }

    public final ObservableFaceArray getFaces() {
        return mesh.getFaces();
    }

    public final ObservableIntegerArray getFaceSmoothingGroups() {
        return mesh.getFaceSmoothingGroups();
    }   
    /*==========================================================================
    *   Point Properties
    */
    private final DoubleProperty perPointMass = new SimpleDoubleProperty(this, "perPointMass", DEFAULT_POINT_MASS);

    public double getPerPointMass() {
        return perPointMass.get();
    }

    public void setPerPointMass(double value) {
        perPointMass.set(value);
    }

    public DoubleProperty perPointMassProperty() {
        return perPointMass;
    }
    /*==========================================================================
        Force for Points
    */
    private final ObjectProperty<Point3D> accumulatedForces = new SimpleObjectProperty<>(this, "accumulatedForces");

    public Point3D getAccumulatedForces() {
        return accumulatedForces.get();
    }
    
    public void addForce(Point3D f){
        setAccumulatedForces(getAccumulatedForces().add(f));
    }
    
    public void setAccumulatedForces(Point3D value) {
        accumulatedForces.set(value);
    }

    public ObjectProperty<Point3D> accumulatedForcesProperty() {
        return accumulatedForces;
    }
    
    
    /*==========================================================================
     Points List
     */
    protected final List<WeightedPoint> getPointList() {
        return points;
    }

    /**
     * *************************************************************************
     * ClothTimer is a simple timer class for updating points * * @author Jason
     * Pollastrini aka jdub1581 *
     *************************************************************************
     *
     * Timer to handle Cloth updates
     */
    private class ClothTimer extends ScheduledService<Void> {

        private static final long ONE_NANO = 1000000000L;
        private static final double ONE_NANO_INV = 1f / 1000000000L;

        private long startTime;
        private long previousTime;
        private double deltaTime;
        private final double fixedDeltaTime = 0.16;
        private int leftOverDeltaTime;

        private boolean paused;

        public ClothTimer() {
            super();

            this.setPeriod(Duration.millis(16));

            ClothTimerThreadFactory tf = new ClothTimerThreadFactory();
            this.setExecutor(Executors.newSingleThreadExecutor(tf));
        }

        /**
         * @return elapsed time as a double
         */
        public double getTimeAsSeconds() {
            return getTime() * ONE_NANO_INV;
        }

        /**
         *
         * @return elapsed time as a long
         */
        public long getTime() {
            return System.nanoTime() - startTime;
        }

        /**
         *
         * @return deltaTime
         */
        public double getDeltaTime() {
            return deltaTime;
        }

        /**
         *
         */
        private void updateTimer() {
            deltaTime = (getTime() - previousTime) * (10.0f / ONE_NANO);
            previousTime = getTime();
            var timeStepAmt = (int) ((deltaTime + leftOverDeltaTime) / fixedDeltaTime);
            timeStepAmt = Math.min(timeStepAmt, 5);
            leftOverDeltaTime = (int) (deltaTime - (timeStepAmt * fixedDeltaTime));
        }

        @Override
        protected Task<Void> createTask() {
            return new Task<>() {

                private void updatePoint(WeightedPoint point) {
                    point.applyForce(new Point3D(4.8f, 1, -1));
                    point.updatePhysics(deltaTime);
                }

                @Override
                protected Void call() {
                    updateTimer();
                    points.parallelStream()
                            .filter(p -> points.indexOf(p) % (getDivisionsX() - 1) == 0)
                            .forEach(p -> p.applyForce(new Point3D(5, -1, 1)));
                    for (int i = 0; i < getConstraintAccuracy(); i++) {
                        points.parallelStream()
                                .forEach(WeightedPoint::solveConstraints);
                    }
                    points.parallelStream().forEach(this::updatePoint);
                    return null;
                }
            };
        }

        @Override
        protected void failed() {
            getException().printStackTrace();
        }

        @Override
        protected void succeeded() {
            super.succeeded();
            updateUI();
        }

        @Override
        protected void cancelled() {
            super.cancelled();
            reset();
        }

        @Override
        public void start() {
            if (isRunning()) {
                return;
            }
            super.start();
            if (startTime <= 0) {
                startTime = System.nanoTime();
            }

        }

        protected void pause() {
            paused = true;
            if (isRunning() && cancel()) {
                    cancelled();
            }
        }

        @Override
        public void reset() {
            super.reset();
            if (!paused) {
                startTime = System.nanoTime();
                previousTime = getTime();
            }
        }

        @Override
        public String toString() {
            return "ClothTimer{" + "startTime=" + startTime + ", previousTime=" + previousTime + ", deltaTime=" + deltaTime + ", fixedDeltaTime=" + fixedDeltaTime + ", leftOverDeltaTime=" + leftOverDeltaTime + '}';
        }

        private class ClothTimerThreadFactory implements ThreadFactory {

            @Override
            public Thread newThread(@NotNull Runnable runnable) {
                Thread thread = new Thread(runnable, "ClothTimerThread");
                thread.setDaemon(true);
                return thread;
            }

        }
    }

}
