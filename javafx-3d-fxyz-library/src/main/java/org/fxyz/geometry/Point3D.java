package org.fxyz.geometry;

import java.util.stream.DoubleStream;

/**
 *
 * @author Sean
 * description Just a useful data structure for X,Y,Z triplets.
 */
public class Point3D {
    
    private float x;
    private float y;
    private float z;
    private float f = 0; // for function evaluation
    /* 
    * @param X,Y,Z are all floats to align with TriangleMesh needs 
    */
    public Point3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public DoubleStream getCoordinates() { return DoubleStream.of(x,y,z); }
    public DoubleStream getCoordinates(float factor) { return DoubleStream.of(factor*x,factor*y,factor*z); }
    
    public Point3D add(Point3D point) {
        return add(point.x, point.y, point.z);
    }

    public Point3D add(float x, float y, float z) {
        return new Point3D(this.x + x, this.y + y, this.z + z);
    }
    
    public Point3D subtract(Point3D point) {
        return subtract(point.x, point.y, point.z);
    }
    
    public Point3D subtract(float x, float y, float z) {
        return new Point3D(this.x - x, this.y - y, this.z - z);
    }
    
    public Point3D multiply(float factor) {
        return new Point3D(this.x * factor, this.y * factor, this.z * factor);
    }
    
    public Point3D normalize() {
        final float mag = magnitude();
        return Float.compare(mag, 0.0F) == 0 ?
                new Point3D(0.0F, 0.0F, 0.0F) :
                new Point3D(x / mag, y / mag, z / mag);

    }
    
    public float magnitude() {
        return (float)Math.sqrt(x * x + y * y + z * z);
    }

    public float dotProduct(Point3D point) {
        return dotProduct(point.x, point.y, point.z);
    }
    
    public float dotProduct(float x, float y, float z) {
        return this.x * x + this.y * y + this.z * z;
    }
    
    public Point3D crossProduct(Point3D point) {
        return crossProduct(point.x, point.y, point.z);
    }
    
    public Point3D crossProduct(float x, float y, float z) {
        return new Point3D(-this.z * y + this.y * z, 
                            this.z * x - this.x * z, 
                           -this.y * x + this.x * y);
    }
    
    @Override
    public String toString() {
        return "Point3D{" + "x=" + x + ", y=" + y + ", z=" + z + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Float.floatToIntBits(this.x);
        hash = 79 * hash + Float.floatToIntBits(this.y);
        hash = 79 * hash + Float.floatToIntBits(this.z);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Point3D other = (Point3D) obj;
        if (Float.floatToIntBits(this.x) != Float.floatToIntBits(other.x)) {
            return false;
        }
        if (Float.floatToIntBits(this.y) != Float.floatToIntBits(other.y)) {
            return false;
        }
        return Float.floatToIntBits(this.z) == Float.floatToIntBits(other.z);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getF() {
        return f;
    }

    public void setF(float f) {
        this.f = f;
    }
}
