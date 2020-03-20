package geometry;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

public record Quad(Vertex v1, Vertex v2, Vertex v3, Vertex v4, int color) {

    private static final Random RANDOM = new SecureRandom();

    public Quad scale(int factor) {
        v1.scale(factor);
        v2.scale(factor);
        v3.scale(factor);
        v4.scale(factor);
        return this;
    }

    public Quad translateX(int translate) {
        v1.translateX(translate);
        v2.translateX(translate);
        v3.translateX(translate);
        v4.translateX(translate);
        return this;
    }

    public Quad translateY(int translate) {
        v1.translateY(translate);
        v2.translateY(translate);
        v3.translateY(translate);
        v4.translateY(translate);
        return this;
    }

    public Quad rotateY(int rotY) {
        v1.rotateY(rotY);
        v2.rotateY(rotY);
        v3.rotateY(rotY);
        v4.rotateY(rotY);
        return this;
    }

    public Quad rotateX(int rotX) {
        v1.rotateX(rotX);
        v2.rotateX(rotX);
        v3.rotateX(rotX);
        v4.rotateX(rotX);
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

    public Pair<List<Edge>, List<Edge>> edges() {
        System.out.println(this);
        var polyId = RANDOM.nextInt();
        Edge e1 = new Edge(v3,v1, polyId);
        Edge e2 = new Edge(v3,v2, polyId);
        Edge e3 = new Edge(v1,v2, polyId);

        polyId = RANDOM.nextInt();
        Edge e4 = new Edge(v3,v2, polyId);
        Edge e5 = new Edge(v3,v4, polyId);
        Edge e6 = new Edge(v4,v2, polyId);
        return Pair.of(List.of(e1,e2,e3), List.of(e4, e5,e6));
    }
}
