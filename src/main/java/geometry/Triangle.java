package geometry;

public record Triangle(Vertex v1,Vertex v2,Vertex v3, int color) {
    public Triangle scale(int factor) {
        return new Triangle(v1.scale(factor),v2.scale(factor),v3.scale(factor), color);
    }

    public Triangle translateX(int translate) {
        return new Triangle(v1.translateX(translate),v2.translateX(translate),v3.translateX(translate), color);
    }

    public Triangle translateY(int translate) {
        return new Triangle(v1.translateY(translate),v2.translateY(translate),v3.translateY(translate), color);
    }

    public Triangle rotateY(int rotY) {
        return new Triangle(v1.rotateY(rotY),v2.rotateY(rotY),v3.rotateY(rotY), color);
    }

    public Triangle rotateX(int rotX) {
        return new Triangle(v1.rotateX(rotX),v2.rotateX(rotX),v3.rotateX(rotX), color);
    }

    public Vertex normal() {
        var line1= new Vertex(v2.x - v1.x,
         v2.y - v1.y,
         v2.z - v1.z);

        var line2= new Vertex( v3.x - v1.x,
         v3.y - v1.y,
         v3.z - v1.z);

        return line1.cross(line2);

    }
}
