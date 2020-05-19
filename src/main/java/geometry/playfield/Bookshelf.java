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

public class Bookshelf implements Model {

    private static final int TILE_LENGTH = 16;
    private List<Triangle> triangles;
    private static final Integer woodId1,woodId2,booksId1,booksId2;

    static {
        //loadTextures and resources
        try {
            int woodID = Resources.setImage(ImageIO.read(Bookshelf.class.getClassLoader().getResourceAsStream("dormroom/wood.png")));
            int booksID = Resources.setImage(ImageIO.read(Bookshelf.class.getClassLoader().getResourceAsStream("dormroom/books.png")));
            woodId1 = Resources.setTexture(woodID, new Vertex2D(0,0),15,15);
            woodId2 = Resources.setTexture(woodID, new Vertex2D(15,15),-15,-15);

            booksId1 = Resources.setTexture(booksID, new Vertex2D(0,0),32,47);
            booksId2 = Resources.setTexture(booksID, new Vertex2D(32,47),-32,-47);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Bookshelf() {
        triangles = new ArrayList<>();
        //"bottom" side (long side away from door)
        triangles.add(new Triangle(
                new Vertex(0, 2*TILE_LENGTH, 0),

                new Vertex(0, 2*TILE_LENGTH, 48),
                new Vertex(1*TILE_LENGTH, 2*TILE_LENGTH, 0),

                woodId1
        ));

        triangles.add(new Triangle(
                new Vertex(1*TILE_LENGTH, 2*TILE_LENGTH, 48),
                new Vertex(1*TILE_LENGTH, 2*TILE_LENGTH, 0),
                new Vertex(0*TILE_LENGTH, 2*TILE_LENGTH, 48),


                woodId2
        ));

        //"front" side (long side away from door)
        triangles.add(new Triangle(

                new Vertex(1*TILE_LENGTH, 5*TILE_LENGTH, 48),
                new Vertex(1*TILE_LENGTH, 5*TILE_LENGTH, 0),
                new Vertex(1*TILE_LENGTH, 2*TILE_LENGTH, 0),

                booksId1
        ));

        triangles.add(new Triangle(
                new Vertex(1*TILE_LENGTH, 2*TILE_LENGTH, 0),
                new Vertex(1*TILE_LENGTH, 2*TILE_LENGTH, 48),
                new Vertex(1*TILE_LENGTH, 5*TILE_LENGTH, 48),
                booksId2
        ));
//"bottom" side (long side away from door)
        triangles.add(new Triangle(
                new Vertex(0, 2*TILE_LENGTH, 48),
                new Vertex(0, 5*TILE_LENGTH, 48),
                new Vertex(1*TILE_LENGTH, 5*TILE_LENGTH, 48),

                woodId1
        ));

        triangles.add(new Triangle(

                new Vertex(1*TILE_LENGTH, 5*TILE_LENGTH, 48),

                new Vertex(1*TILE_LENGTH, 2*TILE_LENGTH, 48),
                new Vertex(0*TILE_LENGTH, 2*TILE_LENGTH, 48),

                woodId2
        ));
    }

    @Override
    public List<Triangle> getTriangles() {
        return triangles;
    }

    @Override
    public BSPTree getBSPTree() {
        var root = new BSPTree.Node();
        root.add(new BoundedCube(this));
        return new BSPTree(root);
    }
}
