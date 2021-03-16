package fx_eight_three_d_features.model;

import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.shape.VertexFormat;

import java.util.ArrayList;
import java.util.List;

public class Icosahedron extends MeshView {

    public Icosahedron(double radius) {
        createIcosahedronMesh((float) radius);
    }

    private void createIcosahedronMesh(float scale) {
        // create 12 vertices of a icosahedron
        // 3 point indices, 3 normal indices and 3 texCoord indices per triangle
        final int numberOfVertices = 12;
        final int pointsPerVertex = 3; // x, y, z
        final int normalsPerVertex = 3; // nx, ny, nz
        final int texCoordsPerVertex = 2; // u, v

        float[] points = new float[numberOfVertices * pointsPerVertex];
        float[] normals = new float[numberOfVertices * normalsPerVertex];
        float[] texCoords = new float[numberOfVertices * texCoordsPerVertex];

        fillThePointsAndNormalsArrays(scale, points, normals);

        // create the 20 triangles of the icosahedron
        int[] faces = buildFaces();

        TriangleMesh triangleMesh = new TriangleMesh(VertexFormat.POINT_NORMAL_TEXCOORD);
        triangleMesh.getPoints().setAll(points);
        triangleMesh.getNormals().setAll(normals);
        triangleMesh.getTexCoords().setAll(texCoords);
        triangleMesh.getFaces().setAll(faces);
        setMesh(triangleMesh);
    }

    private void fillThePointsAndNormalsArrays(float scale, float[] points, float[] normals) {
        List<FloatPoint3D> dVectorList = getVector3DList();
        int index = 0;
        for (FloatPoint3D vector : dVectorList) {
            FloatPoint3D normalizeVector = vector.normalize();
            points[index] = scale * vector.x;
            normals[index++] = normalizeVector.x;
            points[index] = scale * vector.y;
            normals[index++] = normalizeVector.y;
            points[index] = scale * vector.z;
            normals[index++] = normalizeVector.z;
        }
    }

    private List<FloatPoint3D> getVector3DList() {
        List<FloatPoint3D> pointsList = new ArrayList<>();
        float goldenRatio = (float) getGoldenRatio();
        pointsList.add(new FloatPoint3D(-1, goldenRatio, 0));
        pointsList.add(new FloatPoint3D(1, goldenRatio, 0));
        pointsList.add(new FloatPoint3D(-1, -goldenRatio, 0));
        pointsList.add(new FloatPoint3D(1, -goldenRatio, 0));

        pointsList.add(new FloatPoint3D(0, -1, goldenRatio));
        pointsList.add(new FloatPoint3D(0, 1, goldenRatio));
        pointsList.add(new FloatPoint3D(0, -1, -goldenRatio));
        pointsList.add(new FloatPoint3D(0, 1, -goldenRatio));

        pointsList.add(new FloatPoint3D(goldenRatio, 0, -1));
        pointsList.add(new FloatPoint3D(goldenRatio, 0, 1));
        pointsList.add(new FloatPoint3D(-goldenRatio, 0, -1));
        pointsList.add(new FloatPoint3D(-goldenRatio, 0, 1));
        return pointsList;
    }

    private double getGoldenRatio() {
        return (1 + Math.sqrt(5)) / 2;
    }


    private int[] buildFaces() {
        return new int[]{
                0, 0, 0, 11, 11, 0, 5, 5, 0,
                0, 0, 0, 5, 5, 0, 1, 1, 0,
                0, 0, 0, 1, 1, 0, 7, 7, 0,
                0, 0, 0, 7, 7, 0, 10, 10, 0,
                0, 0, 0, 10, 10, 0, 11, 11, 0,
                1, 1, 0, 5, 5, 0, 9, 9, 0,
                5, 5, 0, 11, 11, 0, 4, 4, 0,
                11, 11, 0, 10, 10, 0, 2, 2, 0,
                10, 10, 0, 7, 7, 0, 6, 6, 0,
                7, 7, 0, 1, 1, 0, 8, 8, 0,
                3, 3, 0, 9, 9, 0, 4, 4, 0,
                3, 3, 0, 4, 4, 0, 2, 2, 0,
                3, 3, 0, 2, 2, 0, 6, 6, 0,
                3, 3, 0, 6, 6, 0, 8, 8, 0,
                3, 3, 0, 8, 8, 0, 9, 9, 0,
                4, 4, 0, 9, 9, 0, 5, 5, 0,
                2, 2, 0, 4, 4, 0, 11, 11, 0,
                6, 6, 0, 2, 2, 0, 10, 10, 0,
                8, 8, 0, 6, 6, 0, 7, 7, 0,
                9, 9, 0, 8, 8, 0, 1, 1, 0
        };
    }

}
