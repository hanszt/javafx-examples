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

import javafx.geometry.Point3D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

/**
 * Simple class to create a Toroid shape, it extends MeshView class so it
 * will work seamlessly with JavaFX.<br>
 * <p>
 * NOTE: <br>
 * I would like to thank Wayne Holder for pointing out a visual artefact
 * in the torus and correctly identifying what caused it. <br>
 * The artefact looks like a seam where the mesh edges join because it is
 * wrapped round a torus. The solution was to modify the code that defined
 * the triangle so the joined edges used 'shared' vertices, see the method
 * calcFaces() for the corrected code. <br>
 * used in this program.
 * <p>
 * The original method, calcFacesPT_OpenMesh() is kept for completeness
 * and will work just fine with an open mesh.
 *
 * @author Peter Lager (2017-19)
 */
public class Toroid extends MeshView {

    // Default values
    private static final int NBR_RING_SEGMENTS = 64;
    private static final int NBR_TUBE_SEGMENTS = 32;

    // Stores the number of segments which controls the surface 'smoothness'
    private final int nbrRingSegments;
    private final int nbrTubeSegments;
    private final int nbrRingSteps;
    private final int nbrTubeSteps;
    // The dimensions of the toroid
    protected float ringRad;
    protected float tubeRadX;
    protected float tubeRadY;
    protected float tubeZeroPos; // radians
    private final TriangleMesh mesh;

    /**
     * Create a Toroid using the default number of segments.
     *
     * @param ringRad   the radius of the ring
     * @param tubeRadX  the radius of the tube along the X axis
     * @param tubeRadY  the radius of the tube along the Y axis
     * @param zeroAngle rotates the zero-position round the tube (degrees)
     */
    public Toroid(float ringRad, float tubeRadX, float tubeRadY, float zeroAngle) {
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
    public Toroid(int nbrRingSegments, int nbrTubeSegments, float ringRad, float tubeRadX, float tubeRadY, float zeroAngle) {
        this.nbrRingSegments = nbrRingSegments;
        this.nbrTubeSegments = nbrTubeSegments;
        this.ringRad = ringRad;
        this.tubeRadX = tubeRadX;
        this.tubeRadY = tubeRadY;
        this.tubeZeroPos = (float) Math.toRadians(zeroAngle);
        this.nbrRingSteps = nbrRingSegments + 1;
        this.nbrTubeSteps = nbrTubeSegments + 1;

        mesh = new TriangleMesh();
        setMesh(mesh);
        buildToroid(mesh);
    }

    private void buildToroid(TriangleMesh mesh) {
        mesh.getPoints().setAll(calcPoints());
        mesh.getTexCoords().setAll(calcTexturePoints(1, 1));
        mesh.getFaces().setAll(calcFacesPT());
        // Create the default texture
        var material = new PhongMaterial(Color.BURLYWOOD);
        setMaterial(material);
        // Start with filled shape
        setDrawMode(DrawMode.FILL);
        // Makes it easier to see the wireframe
        setCullFace(CullFace.BACK);
    }

    /**
     * Set the fill colour to be used for the toroid surface. Replaces existing
     * texture/colour options.
     *
     * @param color the colour to use
     */
    public void setTexture(Color color) {
        if (color != null) {
            var material = new PhongMaterial(color);
            setMaterial(material);
        }
    }

    /**
     * Change the image to be used to texture the surface using the current
     * number of ring and tube repeats.
     *
     * @param image the image to be used as a texture
     */
    public void setTexture(Image image) {
        if (image != null) {
            var material = new PhongMaterial();
            material.setDiffuseMap(image);
            setMaterial(material);
        }
    }

    /**
     * Change the image to be used to texture the surface using the specified
     * number of ring and tube repeats.
     *
     * @param image          the image to be used as a texture
     * @param nbrRingRepeats number of texture repeats round the ring
     * @param nbrTubeRepeats number of texture repeats round the tube
     */
    public void setTexture(Image image, float nbrRingRepeats, float nbrTubeRepeats) {
        if (image != null) {
            var material = new PhongMaterial();
            material.setDiffuseMap(image);
            setMaterial(material);
        }
        if (nbrRingRepeats > 0 && nbrTubeRepeats > 0) {
            mesh.getTexCoords().setAll(calcTexturePoints(nbrRingRepeats, nbrTubeRepeats));
        }
    }

    /**
     * Calculate the vertex points based on toroid dimensions and the tube zero
     * start angle.
     */
    private float[] calcPoints() {
        Point3D[][] coord = new Point3D[nbrRingSteps][nbrTubeSteps];
        double ringDeltaAng = (2 * Math.PI / nbrRingSegments);
        double tubeDeltaAng = (2 * Math.PI / nbrTubeSegments);

        calculateXYCoordinatesTubeInZ0Plane(coord, tubeDeltaAng);
        calculateThePointsForAllOtherRingSegments(coord, ringDeltaAng);
        return transferToFloatArray(coord);
    }

    private float[] transferToFloatArray(Point3D[][] coord) {
        var points = new float[nbrRingSteps * nbrTubeSteps * 3];
        int index = 0;
        for (int t = 0; t < nbrTubeSteps; t++) {
            for (int r = 0; r < nbrRingSteps; r++) {
                points[index++] = (float) coord[r][t].getX();
                points[index++] = (float) coord[r][t].getY();
                points[index++] = (float) coord[r][t].getZ();
            }
        }
        return points;
    }

    private void calculateThePointsForAllOtherRingSegments(Point3D[][] coord, double ringDeltaAng) {
        for (int r = 1; r < nbrRingSteps; r++) {
            double angle = r * ringDeltaAng;
            for (int t = 0; t < nbrTubeSteps; t++) {
                Point3D point0 = coord[0][t];
                coord[r][t] = new Point3D(
                        point0.getX() * Math.cos(angle),
                        point0.getY(),
                        point0.getX() * Math.sin(angle)
                );
            }
        }
    }

    private void calculateXYCoordinatesTubeInZ0Plane(Point3D[][] coord, double tubeDeltaAng) {
        for (int t = 0; t < nbrTubeSteps; t++) {
            double angle = tubeZeroPos + t * tubeDeltaAng;
            coord[0][t] = new Point3D(
                    ringRad + tubeRadX * Math.cos(angle),
                    tubeRadY * Math.sin(angle),
                    0
            );
        }
    }

    /**
     * Calculate the texture coordinates based on number of texture repeats.
     *
     * @param nbrRingRepeats number of texture repeats round the ring
     * @param nbrTubeRepeats number of texture repeats round the tube
     */
    private float[] calcTexturePoints(float nbrRingRepeats, float nbrTubeRepeats) {
        float deltaU = nbrRingRepeats / nbrRingSegments;
        float deltaV = nbrTubeRepeats / nbrTubeSegments;
        var texturePoints = new float[nbrRingSteps * nbrTubeSteps * 2];
        int index = 0;
        for (int t = 0; t < nbrTubeSteps; t++) {
            for (int r = 0; r < nbrRingSteps; r++) {
                texturePoints[index++] = r * deltaU;
                texturePoints[index++] = t * deltaV;
            }
        }
        return texturePoints;
    }

    private static final int NR_OF_POINTS_PER_FACE_PT = 12;

    /**
     * Calculate the triangle data using vertex and texture coordinate array
     * data. This code avoids a visual artefact by using 'shared' points
     * where the mesh edges meet.
     */
    private int[] calcFacesPT() {
        int index = 0;
        var faces = new int[nbrRingSegments * nbrTubeSegments * NR_OF_POINTS_PER_FACE_PT];
        for (int t = 0; t < nbrTubeSegments; t++) {
            for (int r = 0; r < nbrRingSegments; r++) {
                Indices textureIndices = calculateIndicesForTextureCoordinates(t, r);
                Indices pointIndices = calculateIndicesForControlPoints(t, r);

                index = setTopLeftTriangleFaces(index, faces, textureIndices, pointIndices);
                index = setBottomRightTriangleFaces(index, faces, textureIndices, pointIndices);
            }
        }
        return faces;
    }

    private int setBottomRightTriangleFaces(int index, int[] faces, Indices textureIndices, Indices pointIndices) {
        faces[index++] = pointIndices.bottomLeft;
        faces[index++] = textureIndices.bottomLeft;
        faces[index++] = pointIndices.bottomRight;
        faces[index++] = textureIndices.bottomRight;
        faces[index++] = pointIndices.topRight;
        faces[index++] = textureIndices.topRight;
        return index;
    }

    private int setTopLeftTriangleFaces(int index, int[] faces, Indices textureIndices, Indices pointIndices) {
        faces[index++] = pointIndices.topLeft;
        faces[index++] = textureIndices.topLeft;
        faces[index++] = pointIndices.bottomLeft;
        faces[index++] = textureIndices.bottomLeft;
        faces[index++] = pointIndices.topRight;
        faces[index++] = textureIndices.topRight;
        return index;
    }

    private Indices calculateIndicesForTextureCoordinates(int t, int r) {
        int r1 = r + 1;
        int t1 = t + 1;
        Indices textureIndices = new Indices();
        textureIndices.topLeft = r + t * nbrRingSteps;
        textureIndices.bottomLeft = r + t1 * nbrRingSteps;
        textureIndices.topRight = r1 + t * nbrRingSteps;
        textureIndices.bottomRight = r1 + t1 * nbrRingSteps;
        return textureIndices;
    }

    private Indices calculateIndicesForControlPoints(int t, int r) {
        Indices pointIndices = new Indices();
        int r1 = r + 1;
        int t1 = t + 1;
        r1 %= nbrRingSegments;
        t1 %= nbrTubeSegments;
        pointIndices.topLeft = r + t * nbrRingSteps;
        pointIndices.bottomLeft = r + t1 * nbrRingSteps;
        pointIndices.topRight = r1 + t * nbrRingSteps;
        pointIndices.bottomRight = r1 + t1 * nbrRingSteps;
        return pointIndices;
    }

    private static class Indices {

        private int topLeft;
        private int topRight;
        private int bottomLeft;
        private int bottomRight;

    }

}
