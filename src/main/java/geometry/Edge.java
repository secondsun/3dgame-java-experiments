package geometry;

import java.util.Objects;

import static java.lang.Math.toRadians;

public class Edge {

    public final Vertex v1;
    public final Vertex v2;


    public Edge(Vertex v1, Vertex v2) {
        this.v1 = v1;
        this.v2 = v2;
        if (v1 == null || v2 == null) {
            throw new NullPointerException("v1 or v2 was null");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return v1.equals(edge.v1) &&
                v2.equals(edge.v2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(v1, v2);
    }
}
