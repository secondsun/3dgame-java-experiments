package geometry.playfield;



import geometry.Model;
import geometry.Triangle;
import geometry.Vertex;
import geometry.Vertex2D;
import util.BSPTree;
import util.BoundedCube;
import util.Resources;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Cube implements Model {

    private static final int scale = 10;

    private final List<Vertex> verticies;
    private final List<Triangle> tiles = new ArrayList<>();
    private final int textureId1;
    private final int textureId2;

    public Cube() {


        try {
            int imageID = Resources.setImage(ImageIO.read(MonasteryPlayfield.class.getClassLoader().getResourceAsStream("water_texture.png")));
            textureId1 = Resources.setTexture(imageID, new Vertex2D(0,0),1,1);
            textureId2 = Resources.setTexture(imageID, new Vertex2D(1,1),-1,-1);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        var v1 = new Vertex(-scale, -scale, -scale);
        var v2 = new Vertex(-scale, scale, -scale);
        var v3 = new Vertex(scale, scale, -scale);
        var v4 = new Vertex(scale, -scale, -scale);
        var v5 = new Vertex(-scale, -scale, scale);
        var v6 = new Vertex(-scale, scale, scale);
        var v7 = new Vertex(scale, scale, scale);
        var v8 = new Vertex(scale, -scale, scale);


        var cube = new Triangle[]{
                new Triangle(v1, v2, v3, textureId1),//SOUTH
                new Triangle(v1, v3, v4, textureId2),//SOUTH
                new Triangle(v4, v3, v7, textureId1),//EAST
                new Triangle(v4, v7, v8, textureId2),//EAST
                new Triangle(v8, v7, v6, textureId1),//NORTH
                new Triangle(v8, v6, v5, textureId2),//NORTH
                new Triangle(v5, v6, v2, textureId1),//WEST
                new Triangle(v5, v2, v1, textureId2),//WEST
                new Triangle(v2, v6, v7, textureId1),//TOP
                new Triangle(v2, v7, v3, textureId2),//TOP
                new Triangle(v8, v5, v1, textureId1),//BOTTOM
                new Triangle(v8, v1, v4, textureId2),//BOTTOM

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
    public BSPTree getBSPTree() {
        var node = new BSPTree.Node();
        node.bounds=new BoundedCube(this);
        return new BSPTree(node);
    }

}