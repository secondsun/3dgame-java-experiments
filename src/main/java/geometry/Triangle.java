package geometry;

import game.Resources;

public record Triangle(Vertex v1, Vertex v2, Vertex v3, float du, float dv, int textureId, Texture texture) {


    public Triangle(Vertex v1, Vertex v2, Vertex v3, int textureId) {
        this(v1,v2,v3,
            (Resources.getTexture(textureId).u())/Math.abs(v2.x - v3.x),
            (Resources.getTexture(textureId).v())/Math.abs(v2.y - v1.y),
            textureId,
            Resources.getTexture(textureId));
    }

    public Triangle scale(int factor) {
        return new Triangle(v1.scale(factor), v2.scale(factor), v3.scale(factor),(texture.origin().x-texture.u())/factor,(texture.origin().y-texture.v())/factor, textureId,texture);
    }

    public Triangle translateX(int translate) {
        return new Triangle(v1.translateX(translate), v2.translateX(translate), v3.translateX(translate)
            ,du, dv, textureId,texture);
    }

    public Triangle translateY(int translate) {
        return new Triangle(v1.translateY(translate), v2.translateY(translate), v3.translateY(translate)
            ,du, dv, textureId,texture);
    }

    public Triangle rotateY(int rotY) {
        var v1New = v1.rotateY(rotY);
        var v2New = v2.rotateY(rotY);
        var v3New = v3.rotateY(rotY);
        var du = Math.abs(v2New.x - v3New.x)/(texture.origin().x-texture.u());
        var dy = Math.abs(v2New.y - v1New.y)/(texture.origin().y-texture.v());
        return new Triangle(v1New, v2New, v3New,du,dy, textureId,texture);
    }

    public Triangle rotateX(int rotX) {
        var v1New = v1.rotateX(rotX);
        var v2New = v2.rotateX(rotX);
        var v3New = v3.rotateX(rotX);
        var du = Math.abs(v2New.x - v3New.x)/(texture.origin().x-texture.u());
        var dy = Math.abs(v2New.y - v1New.y)/(texture.origin().y-texture.v());
        return new Triangle(v1New, v2New, v3New,du,dy, textureId,texture);
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
