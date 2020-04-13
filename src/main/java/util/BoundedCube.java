package util;

import geometry.Model;
import geometry.Triangle;
import geometry.Vertex;

/**
 * This class will create an axis aligned BSP tree bounding box around a model.
 *
 * Top/Bottom is positive y axis aligned
 * Right/Left is positive x axis aligned
 * Near/Far is positive z axis aligned
 */
public class BoundedCube {

    private final Model model;
    public float top,bottom,left,right,near,far;

    public BoundedCube(Model model) {
        this.model = model;

        for (Triangle tri : model.getTriangles()) {
            float maxX = Maths.max(tri.v1.x,tri.v2.x,tri.v3.x);
            float minX = Maths.min(tri.v1.x,tri.v2.x,tri.v3.x);

            float maxY = Maths.max(tri.v1.y,tri.v2.y,tri.v3.y);
            float minY = Maths.min(tri.v1.y,tri.v2.y,tri.v3.y);

            float maxZ = Maths.max(tri.v1.z,tri.v2.z,tri.v3.z);
            float minZ = Maths.min(tri.v1.z,tri.v2.z,tri.v3.z);

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


            if (maxZ > near) {
                near = maxZ;
            }
            if (minZ < far) {
                far = minZ;
            }

        }

    }



}
