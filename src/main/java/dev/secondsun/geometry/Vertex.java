package dev.secondsun.geometry;

import dev.secondsun.util.Maths;
import dev.secondsun.util.Plane;

import java.util.Objects;

import static java.lang.Math.toRadians;

public class Vertex {

    public static final Vertex ZERO = new Vertex(0, 0, 0);
    public float x;
    public float y;
    public float z;
    public float w = 1;

    public Vertex(Vertex toCopy) {
        this.x = toCopy.x;
        this.y = toCopy.y;
        this.z = toCopy.z;
        this.w = toCopy.w;
    }

    public Vertex(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vertex scale(float factor) {
        var v1 = new Vertex(x * factor, y * factor, z * factor);
        this.x = v1.x;
        this.y = v1.y;
        this.z = v1.z;

        return this;
    }

    public Vertex translateX(float translate) {
        var v1 = new Vertex(x + translate, y, z);
        this.x = v1.x;
        this.y = v1.y;
        this.z = v1.z;

        return this;
    }

    public Vertex translateY(float translate) {

        var v1 = new Vertex(x, y + translate, z);
        this.x = v1.x;
        this.y = v1.y;
        this.z = v1.z;

        return this;
    }

    public Vertex rotateY(float rotY) {
        var rotYRad = toRadians(rotY);
        float newX = (float) (x * Math.cos(rotYRad) + z * Math.sin(rotYRad));
        float newY = y;
        float newZ = (float) (-x * Math.sin(rotYRad) + z * Math.cos(rotYRad));

        this.x = newX;
        this.y = newY;
        this.z = newZ;

        return this;


    }

    public Vertex rotateX(float rotX) {
        var rotXRad = toRadians(rotX);
        float newX = x;
        float newY = (float) (-z * Math.sin(rotXRad) + y * Math.cos(rotXRad));
        float newZ = (float) (y * Math.sin(rotXRad) + z * Math.cos(rotXRad));


        this.x = newX;
        this.y = newY;
        this.z = newZ;

        return this;

    }

    public Vertex cross(Vertex other) {
        var v1 = new Vertex(
                y * other.z - z * other.y,
                z * other.x - x * other.z,
                x * other.y - y * other.x
        );
//
//        this.x = A.x;
//        this.y = A.y;
//        this.z = A.z;

        return v1;

    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    public Vertex rotateAround(Vertex axis, float angle) {

        var aInB = Maths.scale(axis,(Maths.dot(this, axis)/Maths.dot(axis, axis)));
        var aOrthB = Maths.subtract(this, aInB);
        var omega = axis.cross(aOrthB);

        var x1 = (float)(Math.cos(angle)/aOrthB.length());
        var x2 =(float) Math.sin(angle)/omega.length();

        var rotatedOrtho = Maths.scale(Maths.add(Maths.scale(aOrthB,(x1)), Maths.scale(omega,(x2))), aOrthB.length());

        return Maths.add(rotatedOrtho,aInB);
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return x == vertex.x &&
                y == vertex.y &&
                z == vertex.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    public Vertex translateZ(int i) {
        var v1 = new Vertex(x, y, z + i);
        this.x = v1.x;
        this.y = v1.y;
        this.z = v1.z;

        return this;

    }

    public Vertex rotateZ(int rotZ) {

        var rotZRad = toRadians(rotZ);
        float newX = (float) (x * Math.cos(rotZRad) - y * Math.sin(rotZRad));
        float newY = (float) (x * Math.sin(rotZRad) + y * Math.cos(rotZRad));
        float newZ = z;

        this.x = newX;
        this.y = newY;
        this.z = newZ;

        return this;
//        x' = x*cos q - y*sin q
//        y' = x*sin q + y*cos q
//        z' = z
    }

    public boolean isBehind(Plane partition) {
        float d = -(partition.location.x * partition.normal.x +
                partition.location.y * partition.normal.y +
                partition.location.z * partition.normal.z);
        float res = (x * partition.normal.x +
                y * partition.normal.y +
                z * partition.normal.z) + d;
        if (res > 0) {
            return false;
        }
        return true;
    }

    public boolean isInFront(Plane partition) {
        float d = -(partition.location.x * partition.normal.x +
                partition.location.y * partition.normal.y +
                partition.location.z * partition.normal.z);
        float res = (x * partition.normal.x +
                y * partition.normal.y +
                z * partition.normal.z) + d;
        if (res < 0) {
            return false;
        }
        return true;
    }

    public Vertex   transform(float[][] matrix) {
        var nx = x*matrix[0][0] +y*matrix[0][1] +z*matrix[0][2] + w * matrix[0][3];
        var ny = x*matrix[1][0] +y*matrix[1][1] +z*matrix[1][2] + w * matrix[1][3];
        var nz = x*matrix[2][0] +y*matrix[2][1] +z*matrix[2][2] + w * matrix[2][3];
        var nw = x*matrix[3][0] +y*matrix[3][1] +z*matrix[3][2] + w * matrix[3][3];

        this.w = 1;
        this.x = nx/nw;
        this.y = ny/nw;
        this.z = nz/nw;


        return this;
    }
    public Vertex project(float[][] matrix) {
        var nx = x*matrix[0][0] + matrix[0][2];
        var ny = y*matrix[1][1]+ matrix[1][2];


        this.x = nx;
        this.y = ny;


        return this;
    }


    public Vertex normalize() {
        return new Vertex(this.x/length(),this.y/length(), this.z/length());
    }
    public Vertex subtract(Vertex other) {
        return new Vertex(this.x - other.x,
                this.y - other.y,
                this.z - other.z);
    }
}
