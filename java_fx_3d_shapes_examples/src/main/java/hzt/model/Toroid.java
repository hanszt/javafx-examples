/*
 * Copyright (c) 2019 Peter Lager
 * <quark(a)lagers.org.uk> http:www.lagers.org.uk
 *
 * This software is provided 'as-is', without any express or implied warranty.
 * In no event will the authors be held liable for any damages arising from
 * the use of this software.
 *
 * Permission is granted to anyone to use this software for any purpose,
 * including commercial applications, and to alter it and redistribute it freely,
 * subject to the following restrictions:
 *
 * 1. The origin of this software must not be misrepresented;
 * you must not claim that you wrote the original software.
 * If you use this software in a product, an acknowledgment in the product
 * documentation would be appreciated but is not required.
 *
 * 2. Altered source versions must be plainly marked as such,
 * and must not be misrepresented as being the original software.
 *
 * 3. This notice may not be removed or altered from any source distribution.
 */
package hzt.model;

import javafx.beans.Observable;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.*;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Arrays;

import static java.lang.Math.*;

/**
 * Class to create a Toroid shape, it extends MeshView class so it
 * will work seamlessly with JavaFX.<br>
 *
 * @author Peter Lager (2017-19) refactored by Hans Zuidervaart
 */
public class Toroid extends MeshView {

    // Default values
    private static final int NBR_RING_SEGMENTS = 64;
    private static final int NBR_TUBE_SEGMENTS = 32;
    private static final double DEFAULT_X_OFFSET = 0.0D;
    private static final double DEFAULT_Y_OFFSET = 1.0D;
    private static final double DEFAULT_Z_OFFSET = 0.0D;

    // Stores the number of segments which controls the surface 'smoothness'
    private final IntegerProperty nbrRingSegments = new SimpleIntegerProperty();
    private final IntegerProperty nbrTubeSegments = new SimpleIntegerProperty();
    private final IntegerProperty nbrRingSteps = new SimpleIntegerProperty();
    private final IntegerProperty nbrTubeSteps = new SimpleIntegerProperty();
    // The dimensions of the toroid
    private final DoubleProperty ringRad = new SimpleDoubleProperty();
    private final DoubleProperty tubeRadX = new SimpleDoubleProperty();
    private final DoubleProperty tubeRadY = new SimpleDoubleProperty();
    private final DoubleProperty tubeZeroPos = new SimpleDoubleProperty(); // radians

    private final DoubleProperty xOffset = new SimpleDoubleProperty(DEFAULT_X_OFFSET);
    private final DoubleProperty yOffset = new SimpleDoubleProperty(DEFAULT_Y_OFFSET);
    private final DoubleProperty zOffset = new SimpleDoubleProperty(DEFAULT_Z_OFFSET);

    private final PhongMaterial material;
    private final TriangleMesh mesh = new TriangleMesh();

    /**
     * Create a default Toroid using default dimensions
     */
    public Toroid() {
        this(1);
    }

    public Toroid(double radius) {
        this(radius, radius / 2);
    }

    /**
     * Create a Toroid using the default number of segments and zero angle = 0
     */
    public Toroid(double ringRad, double tubeRad) {
        this(ringRad, tubeRad, tubeRad);
    }

    /**
     * Create a Toroid using the default number of segments and zero angle = 0
     */
    public Toroid(double ringRad, double tubeRadX, double tubeRadY) {
        this(ringRad, tubeRadX, tubeRadY, 0);
    }

    /**
     * Create a Toroid using the default number of segments.
     */
    public Toroid(double ringRad, double tubeRadX, double tubeRadY, double zeroAngle) {
        this(NBR_RING_SEGMENTS, NBR_TUBE_SEGMENTS, // default 64 x 32
                ringRad, tubeRadX, tubeRadY, zeroAngle
        );
    }

    /**
     * Create a Toroid with a user defined number of segments. Using small
     * numbers can give a very angular toroid.
     *
     * @param nbrRingSegments number of segments round the ring
     * @param nbrTubeSegments number of segments round the tube
     * @param ringRad         the radius of the ring
     * @param tubeRadX        the radius of the tube along the X axis
     * @param tubeRadY        the radius of the tube along the Y axis
     * @param zeroAngle       rotates the zero-position round the tube (degrees)
     */
    public Toroid(int nbrRingSegments, int nbrTubeSegments, double ringRad, double tubeRadX, double tubeRadY, double zeroAngle) {
        this.nbrRingSegments.set(nbrRingSegments);
        this.nbrRingSteps.bind(this.nbrRingSegments.add(1));
        this.nbrTubeSegments.set(nbrTubeSegments);
        this.nbrTubeSteps.bind(this.nbrTubeSegments.add(1));
        this.ringRad.set(ringRad);
        this.tubeRadX.set(tubeRadX);
        this.tubeRadY.set(tubeRadY);
        this.tubeZeroPos.set(toRadians(zeroAngle));
        this.material = new PhongMaterial(Color.BURLYWOOD);
        addListeners();
        setMesh(buildToroidMesh());
    }

    private void addListeners() {
        Arrays.stream(Toroid.class.getDeclaredFields())
                .filter(this::ofTypeObservable)
                .forEach(this::addListener);
    }

    private boolean ofTypeObservable(Field field) {
        Type type = field.getType();
        return type.equals(DoubleProperty.class) || type.equals(IntegerProperty.class);
    }

    private void addListener(Field f) {
        try {
            var obs = (Observable) f.get(this);
            obs.addListener(o -> calculatePointsTexCoordsAndFaces(mesh));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private Mesh buildToroidMesh() {
        calculatePointsTexCoordsAndFaces(mesh);
        // Create the default texture
        setMaterial(material);
        // Start with filled shape
        setDrawMode(DrawMode.FILL);
        // Makes it easier to see the wireframe
        setCullFace(CullFace.BACK);
        return mesh;
    }

    /**
     * Set the fill colour to be used for the toroid surface. Replaces existing
     * texture/colour options.
     *
     * @param color the colour to use
     */
    public void setTexture(Color color) {
        if (color != null) {
            material.setDiffuseColor(color);
        }
    }

    /**
     * Change the image to be used to texture the surface using the current
     * number of ring and tube repeats.
     *
     * @param diffuseMap the image to be used as a texture
     * @param bumpMap    the bumpMap to be used
     */
    public void setTexture(Image diffuseMap, Image bumpMap) {
        if (diffuseMap != null) material.setDiffuseMap(diffuseMap);
        if (bumpMap != null) material.setBumpMap(bumpMap);
    }

    /**
     * Change the image to be used to texture the surface using the specified
     * number of ring and tube repeats.
     *
     * @param diffuseMap     the image to be used as a texture
     * @param nbrRingRepeats number of texture repeats round the ring
     * @param nbrTubeRepeats number of texture repeats round the tube
     */
    public void setTexture(Image diffuseMap, Image bumpMap, float nbrRingRepeats, float nbrTubeRepeats) {
        setTexture(diffuseMap, bumpMap);
        if (nbrRingRepeats > 0 && nbrTubeRepeats > 0) {
            ((TriangleMesh) getMesh()).getTexCoords().setAll(calcTexturePoints(nbrRingRepeats, nbrTubeRepeats));
        }
    }

    /**
     * Calculate the vertex points based on toroid dimensions and the tube zero
     * start angle.
     */
    private void calculatePointsTexCoordsAndFaces(TriangleMesh mesh) {
        double ringDeltaAng = (2 * PI / nbrRingSegments.get());
        double tubeDeltaAng = (2 * PI / nbrTubeSegments.get());
        calculatePointsTexCoordsAndFaces(mesh, ringDeltaAng, tubeDeltaAng);
    }

    private void calculatePointsTexCoordsAndFaces(TriangleMesh mesh, double ringDeltaAng, double tubeDeltaAng) {
        MeshData meshData = new MeshData();
        for (int t = 0; t < nbrTubeSteps.get(); t++) {
            var point0 = calculateXYCoordinateTubeInZ0Plane(tubeDeltaAng, t);
            for (int r = 0; r < nbrRingSteps.get(); r++) {
                double angle = r * ringDeltaAng;
                meshData.calculateAndAddPoint(point0, angle);
                meshData.calculateAndAddTextureCoord(r, t);
                meshData.calculateAndAddFace(t, r);
            }
        }
        meshData.setPointsTexCoordsAndFaces(mesh);
    }

    private Point3D calculateXYCoordinateTubeInZ0Plane(double tubeDeltaAng, int t) {
        double angle0 = tubeZeroPos.get() + t * tubeDeltaAng;
        double x = ringRad.get() + tubeRadX.get() * cos(angle0);
        double y = tubeRadY.get() * sin(angle0);
        return new Point3D(x, y, 0);
    }

    private static final int TWO_PER_TEXTURE_POINT = 2;

    private class MeshData {

        private static final int NR_OF_POINTS_PER_FACE_PT = 12;
        private static final int NR_OF_DIMENSIONS = 3;

        private final float[] textureCoords = new float[nbrRingSteps.get() * nbrTubeSteps.get() * TWO_PER_TEXTURE_POINT];
        private final float[] points = new float[nbrRingSteps.get() * nbrTubeSteps.get() * NR_OF_DIMENSIONS];
        private final int[] faces = new int[nbrRingSegments.get() * nbrTubeSegments.get() * NR_OF_POINTS_PER_FACE_PT];

        private final float defaultDeltaU = 1f / nbrRingSegments.get();
        private final float defaultDeltaV = 1f / nbrTubeSegments.get();

        private int coordsIndex = 0;
        private int pointsIndex = 0;
        private int facesIndex = 0;

        private void calculateAndAddPoint(Point3D point0, double angle) {
            var x = point0.getX() * (cos(angle) + xOffset.get());
            var y = point0.getY() * yOffset.get();
            var z = point0.getX() * (sin(angle) + zOffset.get());
            points[pointsIndex++] = (float) x;
            points[pointsIndex++] = (float) y;
            points[pointsIndex++] = (float) z;
        }

        private void calculateAndAddTextureCoord(float x, float y) {
            textureCoords[coordsIndex++] = x * defaultDeltaU;
            textureCoords[coordsIndex++] = y * defaultDeltaV;
        }

        /**
         * Calculate the triangle data using vertex and texture coordinate array
         * data. This code avoids a visual artefact by using 'shared' points
         * where the mesh edges meet.
         */
        private void calculateAndAddFace(int t, int r) {
            if (t < nbrTubeSegments.get() && r < nbrRingSegments.get()) {
                int r1 = r + 1;
                int t1 = t + 1;
                Indices textureIndices = calculateIndices(t, r, t1, r1);
                r1 %= nbrRingSegments.get();
                t1 %= nbrTubeSegments.get();
                Indices pointIndices = calculateIndices(t, r, t1, r1);
                setTopLeftTriangleFace(textureIndices, pointIndices);
                setBottomRightTriangleFace(textureIndices, pointIndices);
            }
        }

        private void setTopLeftTriangleFace(Indices textureIndices, Indices pointIndices) {
            faces[facesIndex++] = pointIndices.topLeft;
            faces[facesIndex++] = textureIndices.topLeft;
            faces[facesIndex++] = pointIndices.bottomLeft;
            faces[facesIndex++] = textureIndices.bottomLeft;
            faces[facesIndex++] = pointIndices.topRight;
            faces[facesIndex++] = textureIndices.topRight;
        }

        private void setBottomRightTriangleFace(Indices textureIndices, Indices pointIndices) {
            faces[facesIndex++] = pointIndices.bottomLeft;
            faces[facesIndex++] = textureIndices.bottomLeft;
            faces[facesIndex++] = pointIndices.bottomRight;
            faces[facesIndex++] = textureIndices.bottomRight;
            faces[facesIndex++] = pointIndices.topRight;
            faces[facesIndex++] = textureIndices.topRight;
        }

        private Indices calculateIndices(int t, int r, int t1, int r1) {
            Indices indices = new Indices();
            indices.topLeft = r + t * nbrRingSteps.get();
            indices.bottomLeft = r + t1 * nbrRingSteps.get();
            indices.topRight = r1 + t * nbrRingSteps.get();
            indices.bottomRight = r1 + t1 * nbrRingSteps.get();
            return indices;
        }

        public void setPointsTexCoordsAndFaces(TriangleMesh mesh) {
            mesh.getPoints().setAll(points);
            mesh.getTexCoords().setAll(textureCoords);
            mesh.getFaces().setAll(faces);
        }

        private class Indices {

            private int topLeft;
            private int topRight;
            private int bottomLeft;
            private int bottomRight;

        }
    }

    /**
     * Calculate the texture coordinates based on number of texture repeats.
     *
     * @param nbrRingRepeats number of texture repeats round the ring
     * @param nbrTubeRepeats number of texture repeats round the tube
     */
    private float[] calcTexturePoints(float nbrRingRepeats, float nbrTubeRepeats) {
        float deltaU = nbrRingRepeats / nbrRingSegments.get();
        float deltaV = nbrTubeRepeats / nbrTubeSegments.get();
        var texturePoints = new float[nbrRingSteps.get() * nbrTubeSteps.get() * TWO_PER_TEXTURE_POINT];
        int index = 0;
        for (int t = 0; t < nbrTubeSteps.get(); t++) {
            for (int r = 0; r < nbrRingSteps.get(); r++) {
                texturePoints[index++] = r * deltaU;
                texturePoints[index++] = t * deltaV;
            }
        }
        return texturePoints;
    }

    public DoubleBinding getVolume() {
        return surfaceAreaIntersection().multiply(circumference());
    }

    private DoubleBinding circumference() {
        return ringRad.multiply(2 * PI);
    }

    private DoubleBinding surfaceAreaIntersection() {
        return tubeRadX.multiply(tubeRadY).multiply(PI);
    }

    public PhongMaterial getPhongMaterial() {
        return material;
    }

    public IntegerProperty nbrRingSegmentsProperty() {
        return nbrRingSegments;
    }

    public IntegerProperty nbrTubeSegmentsProperty() {
        return nbrTubeSegments;
    }

    public DoubleProperty ringRadProperty() {
        return ringRad;
    }

    public DoubleProperty tubeRadXProperty() {
        return tubeRadX;
    }

    public DoubleProperty tubeRadYProperty() {
        return tubeRadY;
    }

    public DoubleProperty tubeZeroPosProperty() {
        return tubeZeroPos;
    }

    public DoubleProperty xOffsetProperty() {
        return xOffset;
    }

    public DoubleProperty yOffsetProperty() {
        return yOffset;
    }

    public DoubleProperty zOffsetProperty() {
        return zOffset;
    }
}
