package geometry;

public record Quad(Vertex v1, Vertex v2, Vertex v3, Vertex v4, int color) {
    public Quad scale(int factor) {
        return new Quad(v1.scale(factor),v2.scale(factor),v3.scale(factor),v4.scale(factor), color);
    }

    public Quad translateX(int translate) {
        return new Quad(v1.translateX(translate),v2.translateX(translate),v3.translateX(translate),v4.translateX(translate), color);
    }

    public Quad translateY(int translate) {
        return new Quad(v1.translateY(translate),v2.translateY(translate),v3.translateY(translate),v4.translateY(translate), color);
    }

    public Quad rotateY(int rotY) {
        return new Quad(v1.rotateY(rotY),v2.rotateY(rotY),v3.rotateY(rotY),v4.rotateY(rotY), color);
    }

    public Quad rotateX(int rotX) {
        return new Quad(v1.rotateX(rotX),v2.rotateX(rotX),v3.rotateX(rotX),v4.rotateX(rotX), color);
    }

    public Vertex normal() {
        var line1= new Vertex(v2.x() - v1.x(),
         v2.y() - v1.y(),
         v2.z() - v1.z());

        var line2= new Vertex( v3.x() - v1.x(),
         v3.y() - v1.y(),
         v3.z() - v1.z());

        return line1.cross(line2);

    }
}
