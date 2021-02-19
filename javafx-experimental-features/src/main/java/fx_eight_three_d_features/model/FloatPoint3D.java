package fx_eight_three_d_features.model;

class FloatPoint3D {

    final float x;
    final float y;
    final float z;

    FloatPoint3D(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    FloatPoint3D subtract(float x, float y, float z) {
        return new FloatPoint3D(this.x - x, this.y - y, this.z - z);
    }

    FloatPoint3D subtract(FloatPoint3D floatPoint3D) {
        return this.subtract(floatPoint3D.x, floatPoint3D.y, floatPoint3D.z);
    }

    FloatPoint3D normalize() {
        float magnitude = magnitude();
        return magnitude == 0 ? new FloatPoint3D(0f, 0f, 0f) :
                new FloatPoint3D(x / magnitude, y / magnitude, z / magnitude);
    }

    float magnitude() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    FloatPoint3D crossProduct(float x, float y, float z) {
        return new FloatPoint3D(this.y * z - this.z * y, this.z * x - this.x * z, this.x * y - this.y * x);
    }

    FloatPoint3D crossProduct(FloatPoint3D var1) {
        return this.crossProduct(var1.x, var1.y, var1.z);
    }
}
