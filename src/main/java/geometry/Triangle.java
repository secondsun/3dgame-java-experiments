package geometry;

import game.Resources;

public class Triangle{

    public Vertex v1, v2,v3;
    public Vertex2D du;
    public Vertex2D dv;
    public  int textureId;
    public  Texture texture;

    public Triangle(Vertex v1, Vertex v2, Vertex v3, Vertex2D du, Vertex2D dv, int textureId, Texture texture) {
        this.v1 = new Vertex(v1);
        this.v2 = new Vertex(v2);
        this.v3 = new Vertex(v3);
        this.du = du;
        this.dv = dv;
        this.textureId = textureId;
        this.texture =texture;

    }

    public Triangle(Vertex v1, Vertex v2, Vertex v3, int textureId) {
        this(new Vertex(v1),new Vertex(v2),new Vertex(v3),
                new Vertex2D(1,1),new Vertex2D(1,1),
                textureId,
                Resources.getTexture(textureId));
        if (texture != null) {
            this.du = new Vertex2D((this.texture.u()/Math.abs(v2.x - v3.x)),(this.texture.u())/Math.abs(v2.y - v3.y));
            this.dv = new Vertex2D((this.texture.v())/Math.abs(v2.x - v1.x),(this.texture.v())/Math.abs(v2.y - v1.y));
        }

    }

    public Triangle scale(int factor) {
        var newTri = new Triangle(v1.scale(factor), v2.scale(factor), v3.scale(factor),du.scale(1/factor),dv.scale(1/factor), textureId,texture);
        this.v1 = newTri.v1;
        this.v2 = newTri.v2;
        this.v3 = newTri.v3;
        this.du = newTri.du;
        this.dv = newTri.dv;
        return this;
    }

    public Triangle translateX(int translate) {
        var newTri = new Triangle(v1.translateX(translate), v2.translateX(translate), v3.translateX(translate)
                ,du, dv, textureId,texture);
        this.v1 = newTri.v1;
        this.v2 = newTri.v2;
        this.v3 = newTri.v3;
        this.du = newTri.du;
        this.dv = newTri.dv;
        return this;
    }

    public Triangle translateZ(int translate) {
        var newTri = new Triangle(v1.translateZ(translate), v2.translateZ(translate), v3.translateZ(translate)
                ,du, dv, textureId,texture);
        this.v1 = newTri.v1;
        this.v2 = newTri.v2;
        this.v3 = newTri.v3;
        this.du = newTri.du;
        this.dv = newTri.dv;
        return this;
    }

    public Triangle translateY(int translate) {
        var newTri =  new Triangle(v1.translateY(translate), v2.translateY(translate), v3.translateY(translate)
                ,du, dv, textureId,texture);
        this.v1 = newTri.v1;
        this.v2 = newTri.v2;
        this.v3 = newTri.v3;
        this.du = newTri.du;
        this.dv = newTri.dv;
        return this;
    }

    public Triangle rotateY(int rotY) {
        var v1New = v1.rotateY(rotY);
        var v2New = v2.rotateY(rotY);
        var v3New = v3.rotateY(rotY);
        if (texture != null) {
            this.du = new Vertex2D((this.texture.u()/Math.abs(v2.x - v3.x)),(this.texture.u())/Math.abs(v2.y - v3.y));
            this.dv = new Vertex2D((this.texture.v())/Math.abs(v2.x - v1.x),(this.texture.v())/Math.abs(v2.y - v1.y));
        }
        var newTri =  new Triangle(v1New, v2New, v3New,du,dv, textureId,texture);
        this.v1 = newTri.v1;
        this.v2 = newTri.v2;
        this.v3 = newTri.v3;
        this.du = newTri.du;
        this.dv = newTri.dv;
        return this;
    }

    public Triangle rotateZ(int rotZ) {
        var v1New = v1.rotateZ(rotZ);
        var v2New = v2.rotateZ(rotZ);
        var v3New = v3.rotateZ(rotZ);
        if (texture != null) {
            this.du = new Vertex2D((this.texture.u()/Math.abs(v2.x - v3.x)),(this.texture.u())/Math.abs(v2.y - v3.y));
            this.dv = new Vertex2D((this.texture.v())/Math.abs(v2.x - v1.x),(this.texture.v())/Math.abs(v2.y - v1.y));
        }
        var newTri =  new Triangle(v1New, v2New, v3New,du,dv, textureId,texture);
        this.v1 = newTri.v1;
        this.v2 = newTri.v2;
        this.v3 = newTri.v3;
        this.du = newTri.du;
        this.dv = newTri.dv;
        return this;
    }

    public Triangle rotateX(int rotX) {
        var v1New = v1.rotateX(rotX);
        var v2New = v2.rotateX(rotX);
        var v3New = v3.rotateX(rotX);
        if (texture != null) {
            this.du = new Vertex2D((this.texture.u()/Math.abs(v2.x - v3.x)),(this.texture.u())/Math.abs(v2.y - v3.y));
            this.dv = new Vertex2D((this.texture.v())/Math.abs(v2.x - v1.x),(this.texture.v())/Math.abs(v2.y - v1.y));
        }
        var newTri =  new Triangle(v1New, v2New, v3New,du,dv, textureId,texture);
        this.v1 = newTri.v1;
        this.v2 = newTri.v2;
        this.v3 = newTri.v3;
        this.du = newTri.du;
        this.dv = newTri.dv;
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


}

