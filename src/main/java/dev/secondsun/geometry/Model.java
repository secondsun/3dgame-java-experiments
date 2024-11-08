package dev.secondsun.geometry;

import dev.secondsun.util.BSPTree;
import dev.secondsun.util.BoundedCube;

import java.util.ArrayList;
import java.util.List;

public interface Model {
    @Deprecated
    static Model of(List<Triangle> tris) {

        return new Model() {
            @Override
            public List<Triangle> getTriangles() {
                return tris;
            }

            @Override
            public BSPTree tree() {
                BSPTree tree = new BSPTree();
                tree.add(new BoundedCube(this));
                return tree;
            }
        };
    }


    static Model ofModels(List<Model> models) {

        BSPTree tree = new BSPTree();

        List<Triangle> tris = new ArrayList<>();
        for (var model:models) {
            tris.addAll(model.getTriangles());
            tree.add(new BoundedCube(model));
        }

        return new Model() {
            @Override
            public List<Triangle> getTriangles() {
                return tris;
            }

            @Override
            public BSPTree tree() {
                return tree;
            }
        };
    }




    /**
     * returns the triangles that make up this model
     */
    List<Triangle> getTriangles();

    default Model rotateY(int rotY) {
        getTriangles().forEach(tile -> tile.rotateY(rotY));
        return this;
    }

    default Model rotateX(int rotX) {
        getTriangles().forEach(tile -> tile.rotateX(rotX));
        return this;
    }

    default Model translateX(int i) {
        getTriangles().forEach(tile -> tile.translateX(i));
        return this;
    }

    default Model translateY(int i) {
        getTriangles().forEach(tile -> tile.translateY(i));
        return this;
    }

    default Model translateZ(int i) {
        getTriangles().forEach(tile -> tile.translateZ(i));
        return this;
    }

    default Model scale(float v) {
        getTriangles().forEach(tile -> {
            tile.scale(v);
        });
        return this;
    }

    default Model rotateZ(int i) {
        getTriangles().forEach(tile -> tile.rotateZ(i));
        return this;
    }

    default BSPTree tree() {return null;}

    default Model lookAt(Camera camera, Vertex2D foV, Vertex2D worldCenter) {
        var lookAt = camera.lookAt();
        float[][] fovMatrix = {{foV.x,0,0, worldCenter.x},
                               {0, foV.y,0, worldCenter.y},
                               {0,0,-200,-1},
                               {0,0,0,1}};
        getTriangles().forEach(tile -> tile.transform(lookAt));
        getTriangles().forEach(tile -> tile.transform(fovMatrix));
        return this;
    }
}
