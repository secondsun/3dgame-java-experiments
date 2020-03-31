package geometry;

import org.la4j.Matrix;

import static java.lang.Math.toRadians;

public record Vertex2D(float x, float y) {

    public Vertex2D(float x, float y){
        if (Float.isFinite(x)) {
            this.x = x;
        } else {
            this.x = 0;
        }
        if (Float.isFinite(y)) {
            this.y = y;
        } else {
            this.y = 0;
        }
    }

    public Vertex2D scale(float factor) {
        return new Vertex2D(x*factor, y*factor);
    }

    public Vertex2D translateX(float translate) {
        return new Vertex2D(x+translate, y);
    }

    public Vertex2D translateY(float translate) {
        return new Vertex2D(x, y+translate);
    }

    public Vertex2D rotateY(float rotY) {
        var rotYRad = toRadians(rotY);
        float newX = (float)(x * Math.cos(rotYRad) - Math.sin(rotYRad));
        float newY = y;
        return new Vertex2D(newX, newY);
    }

    public Vertex2D rotateX(float rotX) {
        var rotXRad = toRadians(rotX);
        float newX = x;
        float newY = (float)( Math.sin(rotXRad) + y*Math.cos(rotXRad));
        return new Vertex2D(newX, newY);
    }

    public double[][] toMatrix() {
        return new double[][]{
            {x,1},
            {1,y}
        };
    }

    public float length() {
        return (float)Math.sqrt(x*x+y*y);
    }
}
