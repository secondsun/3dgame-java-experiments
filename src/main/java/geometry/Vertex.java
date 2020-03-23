package geometry;

import java.util.Objects;

import static java.lang.Math.toRadians;

public class Vertex {

    public float x;
    public float y;
    public float z;

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

        this.x = v1.x;
        this.y = v1.y;
        this.z = v1.z;

        return this;

    }

    public float length() {
        return (float) Math.sqrt(x * x + y * y + z * z);
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
}
