package dev.secondsun.util;

import dev.secondsun.geometry.Model;
import dev.secondsun.geometry.Triangle;
import dev.secondsun.geometry.Vertex;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CubeBuilder {

    private Vertex start;
    private float width, height, length;
    private int front1, front2, top1, top2, back1, back2, bottom1, bottom2, up1, up2, down1, down2;
    private boolean enableBottom, enableTop, enableUp, enableDown, enableFront, enableBack;

    public CubeBuilder(Vertex start, float width, float height, float length) {
        this.start = start;
        this.width = width;
        this.height = height;
        this.length = length;
        front1 = front2 = top1 = top2 = back1 = back2 = bottom1 = bottom2 = up1 = up2 = down1 = down2 = Color.BLACK.getRGB();
        enableBottom = enableTop = enableUp = enableDown = enableFront = enableBack = true;
    }

    public int getFront1() {
        return front1;
    }

    public CubeBuilder setFront1(int front1) {
        this.front1 = front1;
        return this;
    }

    public int getFront2() {
        return front2;
    }

    public CubeBuilder setFront2(int front2) {
        this.front2 = front2;
        return this;
    }

    public int getTop1() {
        return top1;
    }

    public CubeBuilder setTop1(int top1) {
        this.top1 = top1;
        return this;
    }

    public int getTop2() {
        return top2;
    }

    public CubeBuilder setTop2(int top2) {
        this.top2 = top2;
        return this;
    }

    public int getBack1() {
        return back1;
    }

    public CubeBuilder setBack1(int back1) {
        this.back1 = back1;
        return this;
    }

    public int getBack2() {
        return back2;
    }

    public CubeBuilder setBack2(int back2) {
        this.back2 = back2;
        return this;
    }

    public int getBottom1() {
        return bottom1;
    }

    public CubeBuilder setBottom1(int bottom1) {
        this.bottom1 = bottom1;
        return this;
    }

    public int getBottom2() {
        return bottom2;
    }

    public CubeBuilder setBottom2(int bottom2) {
        this.bottom2 = bottom2;
        return this;
    }

    public int getUp1() {
        return up1;
    }

    public CubeBuilder setUp1(int up1) {
        this.up1 = up1;
        return this;
    }

    public int getUp2() {
        return up2;
    }

    public CubeBuilder setUp2(int up2) {
        this.up2 = up2;
        return this;
    }

    public int getDown1() {
        return down1;
    }

    public CubeBuilder setDown1(int down1) {
        this.down1 = down1;
        return this;
    }

    public int getDown2() {
        return down2;
    }

    public CubeBuilder setDown2(int down2) {
        this.down2 = down2;
        return this;
    }

    public Model cube() {
        var triangles = new ArrayList<Triangle>();
        //"bottom" side (long side away from door)
        if (enableBottom) {
            triangles.add(new Triangle(
                    new Vertex(start.x + width, start.y, start.z),
                    new Vertex(start.x, start.y, start.z),
                    new Vertex(start.x, start.y, start.z + height),
                    bottom1
            ));

            triangles.add(new Triangle(
                    new Vertex(start.x, start.y, start.z + height),
                    new Vertex(start.x + width, start.y, start.z + height),
                    new Vertex(start.x + width, start.y, start.z),
                    bottom2
            ));
        }
        if (enableTop) {
            //"top" side (long side near door)
            triangles.add(new Triangle(

                    new Vertex(start.x + width, start.y + length, start.z),
                    new Vertex(start.x, start.y + length, start.z + height),
                    new Vertex(start.x, start.y + length, start.z),

                    top1
            ));

            triangles.add(new Triangle(
                    new Vertex(start.x, start.y + length, start.z + height),
                    new Vertex(start.x + width, start.y + length, start.z),
                    new Vertex(start.x + width, start.y + length, start.z + height),
                    top2
            ));
        }

        if (enableFront) {
            //"front" side (long side away from door)
            triangles.add(new Triangle(

                    new Vertex(start.x + width, start.y + length, start.z + height),
                    new Vertex(start.x + width, start.y + length, start.z),
                    new Vertex(start.x + width, start.y, start.z),

                    front1
            ));

            triangles.add(new Triangle(
                    new Vertex(start.x + width, start.y, start.z),
                    new Vertex(start.x + width, start.y, start.z + height),
                    new Vertex(start.x + width, start.y + length, start.z + height),
                    front2
            ));
        }

        if (enableBack) {
            //"back" side (long side toward wall)
            triangles.add(new Triangle(


                    new Vertex(start.x, start.y, start.z),
                    new Vertex(start.x, start.y + length, start.z),
                    new Vertex(start.x, start.y + length, start.z + height),

                    back1
            ));

            triangles.add(new Triangle(

                    new Vertex(start.x, start.y + length, start.z + height),
                    new Vertex(start.x, start.y, start.z + height),
                    new Vertex(start.x, start.y, start.z),
                    back2
            ));
        }

        if (enableUp) {
            //"up" side (facing the ceiling)
            triangles.add(new Triangle(
                    new Vertex(start.x, start.y, start.z + height),
                    new Vertex(start.x, start.y + length, start.z + height),
                    new Vertex(start.x + width, start.y + length, start.z + height),

                    up1
            ));

            triangles.add(new Triangle(

                    new Vertex(start.x + width, start.y + length, start.z + height),

                    new Vertex(start.x + width, start.y, start.z + height),
                    new Vertex(start.x, start.y, start.z + height),

                    up2
            ));
        }

        if (enableDown) {
            //"down" side (facing the ceiling)
            triangles.add(new Triangle(

                    new Vertex(start.x + width, start.y + length, start.z),
                    new Vertex(start.x, start.y + length, start.z),
                    new Vertex(start.x, start.y, start.z),
                    down1
            ));

            triangles.add(new Triangle(


                    new Vertex(start.x, start.y, start.z),
                    new Vertex(start.x + width, start.y, start.z),
                    new Vertex(start.x + width, start.y + length, start.z),

                    down2
            ));
        }
        return new Model() {
            @Override
            public List<Triangle> getTriangles() {
                return triangles;
            }

            };
    }

    public CubeBuilder disableBottom() {
        this.enableBottom = false;
        return this;
    }

    public CubeBuilder disableTop() {
        this.enableTop = false;
        return this;
    }

    public CubeBuilder disableFront() {
        this.enableFront = false;
        return this;
    }

    public CubeBuilder disableBack() {
        this.enableBack = false;
        return this;
    }

    public CubeBuilder disableUp() {
        this.enableUp = false;
        return this;
    }

    public CubeBuilder disableDown() {
        this.enableDown = false;
        return this;
    }
}
