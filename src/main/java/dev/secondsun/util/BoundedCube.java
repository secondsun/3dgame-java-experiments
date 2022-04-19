package dev.secondsun.util;

import dev.secondsun.geometry.Model;
import dev.secondsun.geometry.Triangle;
/**
 * This class will create an axis aligned BSP tree bounding box around a model.
 * <p>
 * Top/Bottom is positive y axis aligned
 * Right/Left is positive x axis aligned
 * Near/Far is positive z axis aligned
 */
public class BoundedCube {

    public final Model model;
    public float top = -1000000f, bottom = 1000000f, left = 1000000f, right = -1000000f, near = -1000000f, far = 1000000f;

    public BoundedCube(Model model) {
        this.model = model;

        for (Triangle tri : model.getTriangles()) {
            float maxX = Maths.max(tri.v1.x, tri.v2.x, tri.v3.x);
            float minX = Maths.min(tri.v1.x, tri.v2.x, tri.v3.x);

            float maxY = Maths.max(tri.v1.y, tri.v2.y, tri.v3.y);
            float minY = Maths.min(tri.v1.y, tri.v2.y, tri.v3.y);

            float maxZ = Maths.max(tri.v1.z, tri.v2.z, tri.v3.z);
            float minZ = Maths.min(tri.v1.z, tri.v2.z, tri.v3.z);

            if (maxX > right) {
                right = maxX;
            }
            if (minX < left) {
                left = minX;
            }


            if (maxY > top) {
                top = maxY;
            }
            if (minY < bottom) {
                bottom = minY;
            }


            if (maxZ >= near) {
                near = maxZ;
            }
            if (minZ < far) {
                far = minZ;
            }

        }

    }


    public boolean isBehind(Plane partition) {

        for (Triangle tri : model.getTriangles()) {

            if (!(tri.v1.isBehind(partition)
                    && tri.v2.isBehind(partition)
                    && tri.v3.isBehind(partition))) {
                return false;
            }

        }
        return true;


    }

    public boolean isInFront(Plane partition) {
        for (Triangle tri : model.getTriangles()) {

            if (!(tri.v1.isInFront(partition)
                    && tri.v2.isInFront(partition)
                    && tri.v3.isInFront(partition))) {
                return false;
            }

        }
        return true;

    }


    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(bottom);
        result = prime * result + Float.floatToIntBits(far);
        result = prime * result + Float.floatToIntBits(left);
        result = prime * result + ((model == null) ? 0 : model.hashCode());
        result = prime * result + Float.floatToIntBits(near);
        result = prime * result + Float.floatToIntBits(right);
        result = prime * result + Float.floatToIntBits(top);
        return result;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BoundedCube other = (BoundedCube) obj;
        if (Float.floatToIntBits(bottom) != Float.floatToIntBits(other.bottom))
            return false;
        if (Float.floatToIntBits(far) != Float.floatToIntBits(other.far))
            return false;
        if (Float.floatToIntBits(left) != Float.floatToIntBits(other.left))
            return false;
        if (model == null) {
            if (other.model != null)
                return false;
        } else if (!model.equals(other.model))
            return false;
        if (Float.floatToIntBits(near) != Float.floatToIntBits(other.near))
            return false;
        if (Float.floatToIntBits(right) != Float.floatToIntBits(other.right))
            return false;
        if (Float.floatToIntBits(top) != Float.floatToIntBits(other.top))
            return false;
        return true;
    }
}
