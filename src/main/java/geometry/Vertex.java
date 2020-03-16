package geometry;

import java.util.Objects;

import static java.lang.Math.toRadians;

public class Vertex {

    public int x;
    public int y;
    public int z;

    public Vertex(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vertex scale(int factor) {
        var v1 = new Vertex(x * factor, y * factor, z * factor);
        this.x = v1.x;
        this.y = v1.y;
        this.z = v1.z;

        return this;
    }

    public Vertex translateX(int translate) {
        var v1 = new Vertex(x + translate, y, z);
        this.x = v1.x;
        this.y = v1.y;
        this.z = v1.z;

        return this;
    }

    public Vertex translateY(int translate) {

        var v1 = new Vertex(x, y + translate, z);
        this.x = v1.x;
        this.y = v1.y;
        this.z = v1.z;

        return this;
    }

    public Vertex rotateY(int rotY) {
        var rotYRad = toRadians(rotY);
        int newX = (int) (x * Math.cos(rotYRad) - z * Math.sin(rotYRad));
        int newY = y;
        int newZ = (int) (x * Math.sin(rotYRad) + z * Math.cos(rotYRad));

        this.x = newX;
        this.y = newY;
        this.z = newZ;

        return this;


    }

    public Vertex rotateX(int rotX) {
        var rotXRad = toRadians(rotX);
        int newX = x;
        int newY = (int) (z * Math.sin(rotXRad) + y * Math.cos(rotXRad));
        int newZ = (int) (-y * Math.sin(rotXRad) + z * Math.cos(rotXRad));


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

    public int length() {
        return (int) Math.sqrt(x * x + y * y + z * z);
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
