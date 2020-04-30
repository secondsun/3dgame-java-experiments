package geometry;

import util.Resources;

public class Triangle{

    public Vertex v1, v2,v3;
    public  int textureId;
    public  Texture texture;

    public Triangle(Vertex v1, Vertex v2, Vertex v3, int textureId, Texture texture) {
        this.v1 = new Vertex(v1);
        this.v2 = new Vertex(v2);
        this.v3 = new Vertex(v3);

        this.textureId = textureId;
        this.texture =texture;

    }
    public Triangle(Vertex v1, Vertex v2, Vertex v3, int textureId) {
        this.v1 = new Vertex(v1);
        this.v2 = new Vertex(v2);
        this.v3 = new Vertex(v3);

        this.textureId = textureId;
        this.texture =null;

    }

    public Triangle scale(float factor) {
        var newTri = new Triangle(v1.scale(factor), v2.scale(factor), v3.scale(factor), textureId,texture);
        this.v1 = newTri.v1;
        this.v2 = newTri.v2;
        this.v3 = newTri.v3;
        return this;
    }

    public Triangle translateX(int translate) {
        var newTri = new Triangle(v1.translateX(translate), v2.translateX(translate), v3.translateX(translate)
                , textureId,texture);
        this.v1 = newTri.v1;
        this.v2 = newTri.v2;
        this.v3 = newTri.v3;
        return this;
    }

    public Triangle translateZ(int translate) {
        var newTri = new Triangle(v1.translateZ(translate), v2.translateZ(translate), v3.translateZ(translate)
                , textureId,texture);
        this.v1 = newTri.v1;
        this.v2 = newTri.v2;
        this.v3 = newTri.v3;
        return this;
    }

    public Triangle translateY(int translate) {
        var newTri =  new Triangle(v1.translateY(translate), v2.translateY(translate), v3.translateY(translate)
                ,textureId,texture);
        this.v1 = newTri.v1;
        this.v2 = newTri.v2;
        this.v3 = newTri.v3;
        return this;
    }

    public Triangle rotateY(int rotY) {
        var v1New = v1.rotateY(rotY);
        var v2New = v2.rotateY(rotY);
        var v3New = v3.rotateY(rotY);

        var newTri =  new Triangle(v1New, v2New, v3New, textureId,texture);
        this.v1 = newTri.v1;
        this.v2 = newTri.v2;
        this.v3 = newTri.v3;
        return this;
    }

    public Triangle rotateZ(int rotZ) {
        var v1New = v1.rotateZ(rotZ);
        var v2New = v2.rotateZ(rotZ);
        var v3New = v3.rotateZ(rotZ);

        var newTri =  new Triangle(v1New, v2New, v3New, textureId,texture);
        this.v1 = newTri.v1;
        this.v2 = newTri.v2;
        this.v3 = newTri.v3;
        return this;
    }

    public Triangle rotateX(int rotX) {
        var v1New = v1.rotateX(rotX);
        var v2New = v2.rotateX(rotX);
        var v3New = v3.rotateX(rotX);

        var newTri =  new Triangle(v1New, v2New, v3New, textureId,texture);
        this.v1 = newTri.v1;
        this.v2 = newTri.v2;
        this.v3 = newTri.v3;
        return this;
    }

    public Vertex normal() {
        var line1 = new Vertex(v2.x - v1.x,
                v2.y - v1.y,
                v2.z - v1.z);

        var line2 = new Vertex(v3.x - v1.x,
                v3.y - v1.y,
                v3.z - v1.z);

        return line1.cross(line2);

    }

    public Vertex center() {
        return new Vertex((v1.x + v2.x + v3.x) / 3, (v1.y + v2.y + v3.y) / 3, (v1.z + v2.z + v3.z) / 3);
    }

    public Triangle transform(float[][] matrix) {

        v1.transform(matrix);
        v2.transform(matrix);
        v3.transform(matrix);

        return this;
    }

    public Triangle project(float[][] fovMatrix) {
        v1.project(fovMatrix);
        v2.project(fovMatrix);
        v3.project(fovMatrix);

        return this;
    }
}

