package dev.secondsun.util;

import dev.secondsun.geometry.Model;
import dev.secondsun.geometry.Triangle;
import dev.secondsun.geometry.Vertex;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CubeBuilder {

    private Vertex start;
    private float width, height, depth;
    private int right1, right2, top1, top2, left1, left2, bottom1, bottom2, far1, far2, near1, near2;
    private boolean enableBottom, enableTop, enableFar, enableNear, enableRight, enableLeft;

    public CubeBuilder(Vertex start, float width, float height, float depth) {
        this.start = start;
        this.width = width;
        this.height = height;
        this.depth = depth;
        right1 = right2 = top1 = top2 = left1 = left2 = bottom1 = bottom2 = far1 = far2 = near1 = near2 = Color.BLACK.getRGB();
        enableBottom = enableTop = enableFar = enableNear = enableRight = enableLeft = true;
    }
@Deprecated(forRemoval = true)
    /**
     * @deprecated right instead of front
     * @return
     */
public int getFront1() {
        return right1;
    }
@Deprecated(forRemoval = true)
    /**
     * @deprecated right instead of front
     * @return
     */
    public CubeBuilder setFront1(int front1) {
        this.right1 = front1;
        return this;
    }
@Deprecated(forRemoval = true)
    /**
     * @deprecated right instead of front
     * @return
     */
    public int getFront2() {
        return right2;
    }
@Deprecated(forRemoval = true)
    /**
     * @deprecated right instead of front
     * @return
     */
    public CubeBuilder setFront2(int front2) {
        this.right2 = front2;
        return this;
    }

    public int getRight1() {
        return right1;
    }

    public CubeBuilder setRight1(int front1) {
        this.right1 = front1;
        return this;
    }

    public int getRight2() {
        return right2;
    }

    public CubeBuilder setRight2(int front2) {
        this.right2 = front2;
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
    @Deprecated(forRemoval = true)
    /**
     * @deprecated left instead of back
     * @return
     */
    public int getBack1() {
        return left1;
    }
    @Deprecated(forRemoval = true)
    /**
     * @deprecated left instead of back
     * @return
     */
    public CubeBuilder setBack1(int back1) {
        this.left1 = back1;
        return this;
    }
    @Deprecated(forRemoval = true)
    /**
     * @deprecated left instead of back
     * @return
     */
    public int getBack2() {
        return left2;
    }
    @Deprecated(forRemoval = true)
    /**
     * @deprecated left instead of back
     * @return
     */
    public CubeBuilder setBack2(int back2) {
        this.left2 = back2;
        return this;
    }

    public int getLeft1() {
        return left1;
    }

    public CubeBuilder setLeft1(int back1) {
        this.left1 = back1;
        return this;
    }

    public int getLeft2() {
        return left2;
    }

    public CubeBuilder setLeft2(int back2) {
        this.left2 = back2;
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

    public int getFar1() {
        return far1;
    }

    
    
    @Deprecated(forRemoval = true)
    /**
     * @deprecated use {@link #setFar1(int)}
     * @return
     */
    public CubeBuilder setUp1(int up1) {
        this.far1 = up1;
        return this;
    }
    
    @Deprecated(forRemoval = true)
    /**
     * @deprecated use {@link #setFar2(int)}
     * @return
     */
    public CubeBuilder setUp2(int up2) {
        this.far2 = up2;
        return this;
    }

    @Deprecated(forRemoval = true)
    /**
     * @deprecated use {@link #getFar2()}
     * @return
     */
    public int getUp2() {
        return far2;
    }

@Deprecated(forRemoval = true)
    /**
     * @deprecated use {@link #getFar1()}
     * @return
     */
    public int getUp1() {
        return far1;
    }

    public CubeBuilder setFar1(int up1) {
        this.far1 = up1;
        return this;
    }

    public int getFar2() {
        return far2;
    }

    public CubeBuilder setFar2(int up2) {
        this.far2 = up2;
        return this;
    }

    @Deprecated(forRemoval = true)
    /**
     * @deprecated use near instead of down
     * @return
     */
    public CubeBuilder setDown2(int down2) {
        this.near2 = down2;
        return this;
    }

    @Deprecated(forRemoval = true)
    /**
     * @deprecated use near instead of down
     * @return
     */
    public int getDown1() {
        return near1;
    }

    @Deprecated(forRemoval = true)
    /**
     * @deprecated use near instead of down
     * @return
     */
    public CubeBuilder setDown1(int down1) {
        this.near1 = down1;
        return this;
    }

    @Deprecated(forRemoval = true)
    /**
     * @deprecated use near instead of down
     * @return
     */
    public int getDown2() {
        return near2;
    }

    public int getNear1() {
        return near1;
    }

    public CubeBuilder setNear1(int down1) {
        this.near1 = down1;
        return this;
    }

    public int getNear2() {
        return near2;
    }

    public CubeBuilder setNear2(int down2) {
        this.near2 = down2;
        return this;
    }

    public Model cube() {
        var triangles = new ArrayList<Triangle>();
        //"bottom" side parallel to xz plane, perpendicular to y axis
        if (enableBottom) {
            triangles.add(new Triangle(
                    new Vertex(start.x + width, start.y, start.z),
                    new Vertex(start.x, start.y, start.z),
                    new Vertex(start.x, start.y, start.z + depth),
                    bottom1
            ));

            triangles.add(new Triangle(
                    new Vertex(start.x, start.y, start.z + depth),
                    new Vertex(start.x + width, start.y, start.z + depth),
                    new Vertex(start.x + width, start.y, start.z),
                    bottom2
            ));
        }
        if (enableTop) {
            //"top" side parallel to xz plane, perpendicular to y axis
            triangles.add(new Triangle(                   
                    new Vertex(start.x + width, start.y + height, start.z + depth ),
                    new Vertex(start.x,         start.y + height, start.z + depth ),
                    new Vertex(start.x,         start.y + height, start.z),
                    top1
            ));

            triangles.add(new Triangle(
                    new Vertex(start.x        , start.y + height, start.z),
                    new Vertex(start.x + width, start.y + height, start.z),
                    new Vertex(start.x + width, start.y + height, start.z + depth),
                    top2
            ));
        }

        if (enableRight) {
            //"right" side parallel to yz plane, perpendicular to x axis
            triangles.add(new Triangle(

                    new Vertex(start.x + width, start.y + height, start.z + depth),
                    new Vertex(start.x + width, start.y + height, start.z),
                    new Vertex(start.x + width, start.y, start.z),

                    right1
            ));

            triangles.add(new Triangle(
                    new Vertex(start.x + width, start.y, start.z),
                    new Vertex(start.x + width, start.y, start.z + depth),
                    new Vertex(start.x + width, start.y + height, start.z + depth),
                    right2
            ));
        }

        if (enableLeft) {
            //"left" side parallel to yz plane, perpendicular to x axis
            triangles.add(new Triangle(


                    new Vertex(start.x, start.y, start.z),
                    new Vertex(start.x, start.y + height, start.z),
                    new Vertex(start.x, start.y + height, start.z + depth),

                    left1
            ));

            triangles.add(new Triangle(

                    new Vertex(start.x, start.y + height, start.z + depth),
                    new Vertex(start.x, start.y, start.z + depth),
                    new Vertex(start.x, start.y, start.z),
                    left2
            ));
        }

        if (enableFar) {
            //"far" side parallel to xy plane, perpendicular to z axis
            triangles.add(new Triangle(
                    new Vertex(start.x, start.y, start.z + depth),
                    new Vertex(start.x, start.y + height, start.z + depth),
                    new Vertex(start.x + width, start.y + height, start.z + depth),

                    far1
            ));

            triangles.add(new Triangle(

                    new Vertex(start.x + width, start.y + height, start.z + depth),

                    new Vertex(start.x + width, start.y, start.z + depth),
                    new Vertex(start.x, start.y, start.z + depth),

                    far2
            ));
        }

        if (enableNear) {
            //"near" side parallel to xy plane, perpendicular to z axis
            triangles.add(new Triangle(

                    new Vertex(start.x + width, start.y + height, start.z),
                    new Vertex(start.x, start.y + height, start.z),
                    new Vertex(start.x, start.y, start.z),
                    near1
            ));

            triangles.add(new Triangle(


                    new Vertex(start.x, start.y, start.z),
                    new Vertex(start.x + width, start.y, start.z),
                    new Vertex(start.x + width, start.y + height, start.z),

                    near2
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
        this.enableRight = false;
        return this;
    }

    public CubeBuilder disableBack() {
        this.enableLeft = false;
        return this;
    }

    public CubeBuilder disableUp() {
        this.enableFar = false;
        return this;
    }

    public CubeBuilder disableDown() {
        this.enableNear = false;
        return this;
    }
}
