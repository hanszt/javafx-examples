package fx_eight_three_d_features.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.VertexFormat;

public abstract class PlaneZMesh extends MeshView {
    // 3 point indices, 3 normal indices and 3 texCoords indices per triangle
    private static final int THREE_PER_TRIANGLE = 3;
    private static final int NINE_PER_TRIANGLE = 9;
    private static final int TWO_TRIANGLES_PER_PIXEL = 2;

    private final TriangleMesh triangleMesh = new TriangleMesh(VertexFormat.POINT_NORMAL_TEXCOORD);

    private final IntegerProperty resolutionX = new SimpleIntegerProperty();
    private final IntegerProperty resolutionY = new SimpleIntegerProperty();

    private final DoubleProperty amplitude = new SimpleDoubleProperty();
    private final DoubleProperty dampening = new SimpleDoubleProperty();
    private final DoubleProperty waveFrequency = new SimpleDoubleProperty();
    private final DoubleProperty meshSizeX = new SimpleDoubleProperty();
    private final DoubleProperty meshSizeY = new SimpleDoubleProperty();

    private Image diffuseMap;
    private Image bumpMap;

    protected PlaneZMesh() {
        setMesh(triangleMesh);
        buildTriangleMesh();
        resolutionX.addListener((o, c, n) -> buildTriangleMesh());
        resolutionY.addListener((o, c, n) -> buildTriangleMesh());
        amplitude.addListener((o, c, n) -> buildTriangleMesh());
        dampening.addListener((o, c, n) -> buildTriangleMesh());
        waveFrequency.addListener((o, c, n) -> buildTriangleMesh());
        meshSizeX.addListener((o, c, n) -> buildTriangleMesh());
        meshSizeY.addListener((o, c, n) -> buildTriangleMesh());
    }

    void buildTriangleMesh() {
        int faceCount = resolutionX.get() * resolutionY.get() * TWO_TRIANGLES_PER_PIXEL;
        int numberOfVertices = (resolutionX.get() + 1) * (resolutionY.get() + 1);

        float[] points = new float[numberOfVertices * THREE_PER_TRIANGLE];
        float[] texCoords = new float[numberOfVertices * TWO_TRIANGLES_PER_PIXEL];
        float[] normals = new float[faceCount * THREE_PER_TRIANGLE];
        int[] faces = new int[faceCount * NINE_PER_TRIANGLE];

        fillPointsAndTexCoordsArrays(points, texCoords);
        fillFacesAndNormalsArraysByPointsArray(points, normals, faces);

        triangleMesh.getPoints().setAll(points);
        triangleMesh.getNormals().setAll(normals);
        triangleMesh.getTexCoords().setAll(texCoords);
        triangleMesh.getFaces().setAll(faces);
    }

    private void fillPointsAndTexCoordsArrays(float[] points, float[] texCoords) {
        int indexPointsArray = 0;
        int indexTexCoordsArray = 0;
        for (int y = 0; y <= resolutionY.get(); y++) {
            float dy = (float) y / resolutionY.get();
            double minY = -meshSizeY.get() / 2;
            double maxY = meshSizeY.get() / 2;
            double pY = (1 - dy) * minY + dy * maxY;

            for (int x = 0; x <= resolutionX.get(); x++) {
                float dx = (float) x / resolutionX.get();
                double minX = -meshSizeX.get() / 2;
                double maxX = meshSizeX.get() / 2;
                double pX = (1 - dx) * minX + dx * maxX;

                points[indexPointsArray++] = (float) pX;
                points[indexPointsArray++] = (float) pY;
                points[indexPointsArray++] = computeZValue(new Point2D(pX, pY));

                texCoords[indexTexCoordsArray++] = dx;
                texCoords[indexTexCoordsArray++] = dy;
            }
        }
    }

    protected abstract float computeZValue(Point2D point2D);


    private void fillFacesAndNormalsArraysByPointsArray(float[] points, float[] normals, int[] faces) {
        int counter = 0;
        int resolutionXPlusOne = resolutionX.get() + 1;
        for (int y = 0; y < resolutionY.get(); y++) {
            for (int x = 0; x < resolutionX.get(); x++) {

                int p00 = y * resolutionXPlusOne + x;
                int p01 = p00 + 1;
                int p10 = p00 + resolutionXPlusOne;
                int p11 = p10 + 1;

                FloatPoint3D normal = computeNormal(points, p00, p10, p11);
                setNormal(normals, counter, normal);
                setFace(faces, p00, p10, p11, counter);
                counter++;

                normal = computeNormal(points, p11, p01, p00);
                setNormal(normals, counter, normal);
                setFace(faces, p11, p01, p00, counter);
                counter++;
            }
        }
    }

    private void setFace(int[] faces, int p1, int p2, int p3, int counter) {
        int index = counter * NINE_PER_TRIANGLE;
        faces[index++] = p1;
        faces[index++] = counter;
        faces[index++] = p1;
        faces[index++] = p2;
        faces[index++] = counter;
        faces[index++] = p2;
        faces[index++] = p3;
        faces[index++] = counter;
        faces[index] = p3;
    }

    private void setNormal(float[] normals, int counter, FloatPoint3D normal) {
        int index = counter * THREE_PER_TRIANGLE;
        normals[index++] = normal.x;
        normals[index++] = normal.y;
        normals[index] = normal.z;
    }

    private FloatPoint3D computeNormal(float[] points, int p00, int p10, int p11) {
        int index = p00 * 3;
        FloatPoint3D point1 = new FloatPoint3D(points[index], points[index + 1], points[index + 2]);
        index = p10 * 3;
        FloatPoint3D point2 = new FloatPoint3D(points[index], points[index + 1], points[index + 2]);
        index = p11 * 3;
        FloatPoint3D point3 = new FloatPoint3D(points[index], points[index + 1], points[index + 2]);
        Triangle3D triangle3D = new Triangle3D(point1, point2, point3);
        return triangle3D.computeNormal();
    }

    public void setDiffuseAndBumpMapMaterial() {
        PhongMaterial material = (PhongMaterial) getMaterial();
        if (material.getDiffuseMap() == null && material.getBumpMap() == null) {
            material.setDiffuseMap(diffuseMap);
            material.setBumpMap(bumpMap);
            material.setDiffuseColor(Color.WHITE);
        } else {
            material.setDiffuseMap(null);
            material.setBumpMap(null);
            material.setDiffuseColor(Color.LIGHTGRAY);
        }
    }

    public void setScale(double scale) {
        setScaleX(scale);
        setScaleY(scale);
        setScaleZ(scale);
    }

    public IntegerProperty resolutionXProperty() {
        return resolutionX;
    }

    public void setResolutionX(int resolutionX) {
        this.resolutionX.set(resolutionX);
    }

    public IntegerProperty resolutionYProperty() {
        return resolutionY;
    }

    public void setResolutionY(int resolutionY) {
        this.resolutionY.set(resolutionY);
    }

    public DoubleProperty amplitudeProperty() {
        return amplitude;
    }

    public void setAmplitude(double amplitude) {
        this.amplitude.set(amplitude);
    }

    public DoubleProperty dampeningProperty() {
        return dampening;
    }

    public void setDampening(double dampening) {
        this.dampening.set(dampening);
    }

    public DoubleProperty waveFrequencyProperty() {
        return waveFrequency;
    }

    public void setWaveFrequency(double waveFrequency) {
        this.waveFrequency.set(waveFrequency);
    }

    public DoubleProperty meshSizeXProperty() {
        return meshSizeX;
    }

    public void setMeshSizeX(double meshSizeX) {
        this.meshSizeX.set(meshSizeX);
    }

    public DoubleProperty meshSizeYProperty() {
        return meshSizeY;
    }

    public void setMeshSizeY(double meshSizeY) {
        this.meshSizeY.set(meshSizeY);
    }

    public void setDiffuseMap(Image diffuseMap) {
        this.diffuseMap = diffuseMap;
    }

    public void setBumpMap(Image bumpMap) {
        this.bumpMap = bumpMap;
    }

    private static class Triangle3D {

        private final FloatPoint3D point3D1;
        private final FloatPoint3D point3D2;
        private final FloatPoint3D point3D3;

        private Triangle3D(FloatPoint3D point3D1, FloatPoint3D point3D2, FloatPoint3D point3D3) {
            this.point3D1 = point3D1;
            this.point3D2 = point3D2;
            this.point3D3 = point3D3;
        }

        private FloatPoint3D computeNormal() {
            final FloatPoint3D ySubtractX = point3D2.subtract(point3D1);
            final FloatPoint3D zSubtractX = point3D3.subtract(point3D1);
            FloatPoint3D normal = ySubtractX.crossProduct(zSubtractX);
            return normal.normalize();
        }

    }
}
