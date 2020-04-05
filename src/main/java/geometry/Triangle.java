package geometry;

import game.Resources;

public class Triangle{

    public Vertex v1, v2,v3;
    public float du;
    public float dv;
    public float idu;
    public float idv;
    public  int textureId;
    public  Texture texture;

    public Triangle(Vertex v1, Vertex v2, Vertex v3, int textureId, Texture texture) {
        this.v1 = new Vertex(v1);
        this.v2 = new Vertex(v2);
        this.v3 = new Vertex(v3);
        this.du = texture.u()/(v2.x-v3.x);
        this.dv = texture.v()/(v2.y-v1.y);
        this.idu = 1/this.du;
        this.idv = 1/this.dv;

        this.textureId = textureId;
        this.texture =texture;

    }

    public Triangle(Vertex v1, Vertex v2, Vertex v3, int textureId) {
        this(new Vertex(v1),new Vertex(v2),new Vertex(v3),
                textureId,
                Resources.getTexture(textureId));
        if (texture != null) {
            this.du = texture.u()/(v2.x-v3.x);
            this.dv = texture.v()/(v2.y-v1.y);
            this.idu = 1/this.du;
            this.idv = 1/this.dv;
        }

    }

    public Triangle scale(int factor) {

        this.v1 = v1.scale(factor);
        this.v2 = v2.scale(factor);
        this.v3 = v3.scale(factor);
        this.du = texture.u()/(v2.x-v3.x);
        this.dv = texture.v()/(v2.y-v1.y);
        this.idu = 1/this.du;
        this.idv = 1/this.dv;
        return this;
    }

    public Triangle translateX(int translate) {
        var newTri = new Triangle(v1.translateX(translate), v2.translateX(translate), v3.translateX(translate),
                textureId,texture);
        this.v1 = newTri.v1;
        this.v2 = newTri.v2;
        this.v3 = newTri.v3;
        this.du = texture.u()/(v2.x-v3.x);
        this.dv = texture.v()/(v2.y-v1.y);
        this.idu = 1/this.du;
        this.idv = 1/this.dv;
        return this;
    }

    public Triangle translateZ(int translate) {
        var newTri = new Triangle(v1.translateZ(translate), v2.translateZ(translate), v3.translateZ(translate),
                textureId,texture);
        this.v1 = newTri.v1;
        this.v2 = newTri.v2;
        this.v3 = newTri.v3;
        this.du = texture.u()/(v2.x-v3.x);
        this.dv = texture.v()/(v2.y-v1.y);
        this.idu = 1/this.du;
        this.idv = 1/this.dv;
        return this;
    }

    public Triangle translateY(int translate) {
        var newTri =  new Triangle(v1.translateY(translate), v2.translateY(translate), v3.translateY(translate),
                textureId,texture);
        this.v1 = newTri.v1;
        this.v2 = newTri.v2;
        this.v3 = newTri.v3;
        this.du = texture.u()/(v2.x-v3.x);
        this.dv = texture.v()/(v2.y-v1.y);
        this.idu = 1/this.du;
        this.idv = 1/this.dv;
        return this;
    }

    public Triangle rotateY(int rotY) {
        this.v1 = v1.rotateY(rotY);
        this.v2 = v2.rotateY(rotY);
        this.v3 = v3.rotateY(rotY);
        if (texture != null) {
            this.du = texture.u()/(v2.x-v3.x);
            this.dv = texture.v()/(v2.y-v1.y);
            this.idu = 1/this.du;
            this.idv = 1/this.dv;
        }

        return this;
    }

    public Triangle rotateZ(int rotZ) {
        this.v1 = v1.rotateZ(rotZ);
        this.v2 = v2.rotateZ(rotZ);
        this.v3 = v3.rotateZ(rotZ);
        if (texture != null) {
            this.du = texture.u()/(v2.x-v3.x);
            this.dv = texture.v()/(v2.y-v1.y);
            this.idu = 1/this.du;
            this.idv = 1/this.dv;
        }


        return this;
    }

    public Triangle rotateX(int rotX) {
        this.v1 = v1.rotateX(rotX);
        this.v2 = v2.rotateX(rotX);
        this.v3 = v3.rotateX(rotX);
        if (texture != null) {
            this.du = texture.u()/(v2.x-v3.x);
            this.dv = texture.v()/(v2.y-v1.y);
            this.idu = 1/this.du;
            this.idv = 1/this.dv;
        }

        return this;
    }

    public Vertex normal() {
        var line1 = new Vertex(v2.x - v1.x,
                v2.y - v1.y,
                v2.z - v1.z);

        var line2 = new Vertex(v3.x - v1.x,
                v1.y - v1.y,
                v3.z - v1.z);

        return line1.cross(line2);

    }

    public Vertex center() {
        return new Vertex((v1.x + v2.x + v3.x) / 3, (v1.y + v2.y + v1.y) / 3, (v1.z + v2.z + v3.z) / 3);
    }


}

