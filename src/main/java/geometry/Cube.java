package geometry;



import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.awt.Color;

public class Cube implements Model {

    private static final int scale = 10;

    private final List<Vertex> verticies;
    private final List<Triangle> tiles = new ArrayList<>();

    public Cube() {


        var v1 = new Vertex(-scale, -scale, -scale);
        var v2 = new Vertex(-scale, scale, -scale);
        var v3 = new Vertex(scale, scale, -scale);
        var v4 = new Vertex(scale, -scale, -scale);
        var v5 = new Vertex(-scale, -scale, scale);
        var v6 = new Vertex(-scale, scale, scale);
        var v7 = new Vertex(scale, scale, scale);
        var v8 = new Vertex(scale, -scale, scale);


        var cube = new Triangle[]{
                new Triangle(v1, v2, v3, Color.WHITE.getRGB()),//SOUTH
                new Triangle(v1, v3, v4, Color.WHITE.getRGB()),//SOUTH
                new Triangle(v4, v3, v7, Color.WHITE.getRGB()),//EAST
                new Triangle(v4, v7, v8, Color.WHITE.getRGB()),//EAST
                new Triangle(v8, v7, v6, Color.WHITE.getRGB()),//NORTH
                new Triangle(v8, v6, v5, Color.WHITE.getRGB()),//NORTH
                new Triangle(v5, v6, v2, Color.WHITE.getRGB()),//WEST
                new Triangle(v5, v2, v1, Color.WHITE.getRGB()),//WEST
                new Triangle(v2, v6, v7, Color.WHITE.getRGB()),//TOP
                new Triangle(v2, v7, v3, Color.WHITE.getRGB()),//TOP
                new Triangle(v8, v5, v1, Color.WHITE.getRGB()),//BOTTOM
                new Triangle(v8, v1, v4, Color.WHITE.getRGB()),//BOTTOM

        };


        for (Triangle q : cube) {
            tiles.add(q);
        }
        verticies = List.of(v1, v2, v3, v4, v5, v6, v7, v8);

    }


    @Override
    public List<Triangle> getTriangles() {
        return tiles;
    }

    @Override
    public List<Vertex> getVerticies() {
        return verticies;
    }
}
