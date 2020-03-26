package geometry;

import java.util.List;

public interface Model {
    /**
     * returns the triangles that make up this model
     */
    List<Triangle> getTriangles();

    /**
     * returns the triangles that make up this model
     */
    List<Vertex> getVerticies();


    default Model rotateY(int rotY) {
        getVerticies().forEach(tile -> tile.rotateY(rotY));
        return this;
    }

    default Model rotateX(int rotX) {
        getVerticies().forEach(tile -> tile.rotateX(rotX));
        return this;
    }

    default Model translateX(int i) {
        getVerticies().forEach(tile -> tile.translateX(i));
        return this;
    }

    default Model translateY(int i) {
        getVerticies().forEach(tile -> tile.translateY(i));
        return this;
    }

    default Model translateZ(int i) {
        getVerticies().forEach(tile -> tile.translateZ(i));
        return this;
    }
    
}
