package dev.secondsun.geometry;

import java.util.Objects;

import static java.lang.Math.toRadians;

import java.io.Serializable;

public class Vertex2D implements Serializable {

    public float x;
    public float y;

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
        this.x *= factor;
        this.y += factor;
        return this;
    }

    public Vertex2D translateX(float translate) {
        this.x+=translate;return this;
    }

    public Vertex2D translateY(float translate) {
        this.y += translate; return this;
    }

    public Vertex2D rotateY(float rotY) {
        var rotYRad = toRadians(rotY);
        float newX = (float)(x * Math.cos(rotYRad) - Math.sin(rotYRad));

        this.x = newX;
        return  this;
    }

    public Vertex2D rotateX(float rotX) {
        var rotXRad = toRadians(rotX);
        float newY = (float)( Math.sin(rotXRad) + y*Math.cos(rotXRad));
        this.y = newY;
        return this;
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

    public Vertex2D rotateZ(int rotZ) {
        var rotZRad = toRadians(rotZ);
        float newX = (float) (x * Math.cos(rotZRad) - y * Math.sin(rotZRad));
        float newY = (float) (x*Math.sin(rotZRad) + y*Math.cos(rotZRad));

        this.x = newX;
        this.y = newY;

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex2D vertex2D = (Vertex2D) o;
        return Float.compare(vertex2D.x, x) == 0 &&
                Float.compare(vertex2D.y, y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Vertex2D{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
