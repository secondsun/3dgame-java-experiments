package dev.secondsun.util;

import dev.secondsun.geometry.Model;
import dev.secondsun.geometry.Triangle;
import dev.secondsun.geometry.Vertex;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Build a Pyramid.
 *
 * Start is the near, left, bottom.
 * Use a negative height to invert the pyramid.
 *
 */
public class PyramidBuilder {
    private Vertex start;
    private float width, height, depth;
    private int right,  left, bottom, far, near;
    private boolean enableBottom, enableTop, enableFar, enableNear, enableRight, enableLeft;

    public PyramidBuilder(Vertex start, float width, float height, float depth) {
        this.start = start;
        this.width = width;
        this.height = height;
        this.depth = depth;
        right =  left = bottom  = far = near = Color.BLACK.getRGB();
        enableBottom =  enableFar = enableNear = enableRight = enableLeft = true;
    }

    public PyramidBuilder setColor(int rgb) {
        right = left = bottom = far = near = rgb;
        return this;
    }


    public Model build() {
        var triangles = new ArrayList<Triangle>();
        //"bottom" side parallel to xz plane, perpendicular to y axis
        if (enableBottom) {
            triangles.add(new Triangle(
                    new Vertex(start.x + width, start.y, start.z),
                    new Vertex(start.x, start.y, start.z),
                    new Vertex(start.x, start.y, start.z + depth),
                    bottom
            ));

            triangles.add(new Triangle(
                    new Vertex(start.x, start.y, start.z + depth),
                    new Vertex(start.x + width, start.y, start.z + depth),
                    new Vertex(start.x + width, start.y, start.z),
                    bottom
            ));
        }

        if (enableRight) {
            triangles.add(new Triangle(
                    new Vertex(start.x + width/2, start.y + height, start.z + depth/2),
                    new Vertex(start.x + width, start.y, start.z),
                    new Vertex(start.x + width, start.y, start.z + depth),

                    right
            ));

        }

        if (enableLeft) {
            //"left" side parallel to yz plane, perpendicular to x axis
            triangles.add(new Triangle(
                    new Vertex(start.x, start.y, start.z),
                    new Vertex(start.x + width/2, start.y + height, start.z + depth/2),
                    new Vertex(start.x, start.y, start.z + depth),

                    left
            ));

        }

        if (enableFar) {
            //"far" side parallel to xy plane, perpendicular to z axis
            triangles.add(new Triangle(
                    new Vertex(start.x + width/2, start.y + height, start.z + depth/2),
                    new Vertex(start.x + width, start.y, start.z + depth),
                    new Vertex(start.x, start.y, start.z + depth),

                    far
            ));

        }

        if (enableNear) {
            //"near" side parallel to xy plane, perpendicular to z axis
            triangles.add(new Triangle(
                    new Vertex(start.x + width/2, start.y + height, start.z + depth/2),
                    new Vertex(start.x, start.y, start.z),
                    new Vertex(start.x + width, start.y, start.z),

                    near
            ));

        }
        return new Model() {
            @Override
            public List<Triangle> getTriangles() {
                return triangles;
            }

        };
    }

    public void enableBottom(boolean b) {
        this.enableBottom = b;
    }

    public void enableNear(boolean b) {
        this.enableNear = b;
    }
    public void enableFar(boolean b) {
        this.enableFar = b;
    }
    public void enableRight(boolean b) {
        this.enableRight = b;
    }
    public void enableLeft(boolean b) {
        this.enableLeft = b;
    }
}
