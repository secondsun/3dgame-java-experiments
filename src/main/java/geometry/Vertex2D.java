package geometry;

import org.la4j.Matrix;

import static java.lang.Math.toRadians;

public record Vertex2D(int x, int y) {
    public Vertex2D scale(int factor) {
        return new Vertex2D(x*factor, y*factor);
    }

    public Vertex2D translateX(int translate) {
        return new Vertex2D(x+translate, y);
    }

    public Vertex2D translateY(int translate) {
        return new Vertex2D(x, y+translate);
    }

    public Vertex2D rotateY(int rotY) {
        var rotYRad = toRadians(rotY);
        int newX = (int)(x * Math.cos(rotYRad) - Math.sin(rotYRad));
        int newY = y;
        return new Vertex2D(newX, newY);
    }

    public Vertex2D rotateX(int rotX) {
        var rotXRad = toRadians(rotX);
        int newX = x;
        int newY = (int)( Math.sin(rotXRad) + y*Math.cos(rotXRad));
        return new Vertex2D(newX, newY);
    }

    public double[][] toMatrix() {
        return new double[][]{
            {x,1},
            {1,y}
        };
    }

    public int length() {
        return (int)Math.sqrt(x*x+y*y);
    }
}
