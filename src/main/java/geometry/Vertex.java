package geometry;

import static java.lang.Math.toRadians;

public record Vertex(int x, int y, int z) {
    public Vertex scale(int factor) {
        return new Vertex(x*factor, y*factor, z*factor);
    }

    public Vertex translateX(int translate) {
        return new Vertex(x+translate, y, z);
    }

    public Vertex translateY(int translate) {
        return new Vertex(x, y+translate, z);
    }

    public Vertex rotateY(int rotY) {
        var rotYRad = toRadians(rotY);
        int newX = (int)(x * Math.cos(rotYRad) - z*Math.sin(rotYRad));
        int newY = y;
        int newZ = (int)(x * Math.sin(rotYRad) + z*Math.cos(rotYRad));
        return new Vertex(newX, newY, newZ);
    }

    public Vertex rotateX(int rotX) {
        var rotXRad = toRadians(rotX);
        int newX = x;
        int newY = (int)(z * Math.sin(rotXRad) + y*Math.cos(rotXRad));
        int newZ = (int)(-y * Math.sin(rotXRad) + z*Math.cos(rotXRad));
        return new Vertex(newX, newY, newZ);
    }

    public Vertex cross(Vertex other) {
        return new Vertex(
                y * other.z - z * other.y,
                z * other.x - x * other.z,
                x * other.y - y * other.x
        );
    }

    public int length() {
        return (int)Math.sqrt(x*x+y*y+z*z);
    }
}
