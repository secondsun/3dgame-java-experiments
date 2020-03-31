package geometry;

import java.util.List;

public interface Model {
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

    default Model scale(int v) {
        getTriangles().forEach(tile -> {
            tile.scale(v);
        });
        return this;
    }
}
