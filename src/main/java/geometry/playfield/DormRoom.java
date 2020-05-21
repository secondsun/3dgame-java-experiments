package geometry.playfield;

import geometry.Model;
import geometry.Triangle;
import geometry.Vertex;
import geometry.Vertex2D;
import util.BSPTree;
import util.BoundedCube;
import util.CubeBuilder;
import util.Resources;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DormRoom implements Model {

    private static final int TILE_LENGTH = 16;
    private final BSPTree root;
    private List<Triangle> triangles;
    private static final Integer woodId1,woodId2,booksId1,booksId2;

    private static final int floorId2, floorId1,bedId2, bedId1,bedUpId2, bedUpId1;

    static {
        //loadTextures and resources
        try {
            int woodID = Resources.setImage(ImageIO.read(DormRoom.class.getClassLoader().getResourceAsStream("dormroom/wood.png")));
            int booksID = Resources.setImage(ImageIO.read(DormRoom.class.getClassLoader().getResourceAsStream("dormroom/books.png")));
            int floorID = Resources.setImage(ImageIO.read(DormRoom.class.getClassLoader().getResourceAsStream("dormroom/floor.png")));
            int bedID = Resources.setImage(ImageIO.read(DormRoom.class.getClassLoader().getResourceAsStream("dormroom/bed.png")));
            int bedUpID = Resources.setImage(ImageIO.read(DormRoom.class.getClassLoader().getResourceAsStream("dormroom/bedUp.png")));

            woodId1 = Resources.setTexture(woodID, new Vertex2D(0,0),15,15);
            woodId2 = Resources.setTexture(woodID, new Vertex2D(15,15),-15,-15);

            booksId1 = Resources.setTexture(booksID, new Vertex2D(0,0),32,47);
            booksId2 = Resources.setTexture(booksID, new Vertex2D(32,47),-32,-47);
            floorId1 = Resources.setTexture(floorID, new Vertex2D(0,0),100,100);
            floorId2 = Resources.setTexture(floorID, new Vertex2D(100,100),-100,-100);


            bedId1 = Resources.setTexture(bedID, new Vertex2D(0,0),15,31);
            bedId2 = Resources.setTexture(bedID, new Vertex2D(15,31),-15,-31);
            bedUpId1 = Resources.setTexture(bedUpID, new Vertex2D(0,0),31,15);
            bedUpId2 = Resources.setTexture(bedUpID, new Vertex2D(31,15),-31,-15);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<Triangle> floorTris;
    private ArrayList<Triangle> mattressTries;
    private ArrayList<Triangle> bookshelfTriangles;
    private ArrayList<Triangle> bedLegsTriangles;
    private ArrayList<Triangle> bedTries;

    public DormRoom() {
        triangles = new ArrayList<>();
        bookshelf();
        bed();
        floor();

        this.root = new BSPTree();
        root.add(new BoundedCube(floorModel()));
        root.add(new BoundedCube(bookshelfModel()));
        root.add(new BoundedCube(bedModel()));

    }

    private void floor() {
        this.floorTris = new ArrayList<Triangle>();
        floorTris.add(new Triangle(
                new Vertex(0, 0*TILE_LENGTH, 0),
                new Vertex(0, 6*TILE_LENGTH, 0),
                new Vertex(6*TILE_LENGTH, 6*TILE_LENGTH, 0),

                floorId1
        ));

        floorTris.add(new Triangle(

                new Vertex(6*TILE_LENGTH, 6*TILE_LENGTH, 0),

                new Vertex(6*TILE_LENGTH, 0*TILE_LENGTH, 0),
                new Vertex(0*TILE_LENGTH, 0*TILE_LENGTH, 0),

                floorId2
        ));
        triangles.addAll(floorTris);
    }

    private void bookshelf() {
        this.bookshelfTriangles = new ArrayList<Triangle>();
        //"bottom" side (long side away from door)
        var builder = new CubeBuilder(new Vertex(0,2*TILE_LENGTH,0), 1*TILE_LENGTH,36,3*TILE_LENGTH);
        builder.setBottom1(woodId1)
                .setBottom2(woodId2)
                .setTop1(woodId1)
                .setTop2(woodId2)
                .setFront1(booksId1)
                .setFront2(booksId2)
                .setBack1(booksId1)
                .setBack2(booksId2)
                .setUp1(woodId1)
                .setUp2(woodId2)
                .disableDown();

        bookshelfTriangles.addAll(builder.cube().getTriangles());
        triangles.addAll(bookshelfTriangles);

    }

    private void bed() {
        mattress();
        legs();
        this.bedTries = new ArrayList<Triangle>();
        this.bedTries.addAll(mattressTries);
        this.bedTries.addAll(bedLegsTriangles);
    }

    private void mattress() {
        this.mattressTries = new ArrayList<Triangle>();

        var builder = new CubeBuilder(new Vertex(1*TILE_LENGTH,0,TILE_LENGTH-4), 2*TILE_LENGTH,4,TILE_LENGTH);
        builder.setBottom1(bedId1)
                .setBottom2(bedId2)
                .setTop1(bedId1)
                .setTop2(bedId2)
                .setFront1(Color.WHITE.getRGB())
                .setFront2(Color.WHITE.getRGB())
                .setBack1(Color.cyan.getRGB())
                .setBack2(Color.cyan.getRGB())
                .setUp1(bedUpId1)
                .setUp2(bedUpId2)
                .disableDown();
        mattressTries.addAll(builder.cube().getTriangles());
        triangles.addAll(mattressTries);

    }

    private void legs() {
        this.bedLegsTriangles = new ArrayList<Triangle>();
        //"bed all" side (long side away from door)
        var builder = new CubeBuilder(new Vertex(1*TILE_LENGTH+4,4,0), 4,TILE_LENGTH-4,4);
        builder.setBottom1(0xA0522D)
                .setBottom2(0xA0522D)
                .setTop1(0xA0522D)
                .setTop2(0xA0522D)
                .setFront1(0xA0522D)
                .setFront2(0xA0522D)
                .setBack1(0xA0522D)
                .setBack2(0xA0522D)
                .setUp1(0xA0522D)
                .setUp2(0xA0522D)
                .disableUp()
                .disableDown();
        bedLegsTriangles.addAll(builder.cube().getTriangles());
        builder = new CubeBuilder(new Vertex(1*TILE_LENGTH+4,TILE_LENGTH-4,0), 4,TILE_LENGTH-4,4);
        builder.setBottom1(0xA0522D)
                .setBottom2(0xA0522D)
                .setTop1(0xA0522D)
                .setTop2(0xA0522D)
                .setFront1(0xA0522D)
                .setFront2(0xA0522D)
                .setBack1(0xA0522D)
                .setBack2(0xA0522D)
                .setUp1(0xA0522D)
                .setUp2(0xA0522D)
                .disableUp()
                .disableDown();
        bedLegsTriangles.addAll(builder.cube().getTriangles());
        builder = new CubeBuilder(new Vertex(3*TILE_LENGTH-4,4,0), 4,TILE_LENGTH-4,4);
        builder.setBottom1(0xA0522D)
                .setBottom2(0xA0522D)
                .setTop1(0xA0522D)
                .setTop2(0xA0522D)
                .setFront1(0xA0522D)
                .setFront2(0xA0522D)
                .setBack1(0xA0522D)
                .setBack2(0xA0522D)
                .setUp1(0xA0522D)
                .setUp2(0xA0522D)
                .disableUp()
                .disableDown();
        bedLegsTriangles.addAll(builder.cube().getTriangles());
        builder = new CubeBuilder(new Vertex(3*TILE_LENGTH-4,TILE_LENGTH-4,0), 4,TILE_LENGTH-4,4);
        builder.setBottom1(0xA0522D)
                .setBottom2(0xA0522D)
                .setTop1(0xA0522D)
                .setTop2(0xA0522D)
                .setFront1(0xA0522D)
                .setFront2(0xA0522D)
                .setBack1(0xA0522D)
                .setBack2(0xA0522D)
                .setUp1(0xA0522D)
                .setUp2(0xA0522D)
                .disableUp()
                .disableDown();
        bedLegsTriangles.addAll(builder.cube().getTriangles());
        triangles.addAll(bedLegsTriangles);
    }

    @Override
    public List<Triangle> getTriangles() {
        return triangles;
    }

    @Override
    public BSPTree getBSPTree() {

        return root;
    }

    public Model bedModel() {
        return new Model() {
            @Override
            public List<Triangle> getTriangles() {
                return bedTries;
            }

            @Override
            public BSPTree getBSPTree() {
                return null;
            }
        };
    }
    public Model bookshelfModel() {
        return new Model() {
            @Override
            public List<Triangle> getTriangles() {
                return bookshelfTriangles;
            }

            @Override
            public BSPTree getBSPTree() {
                return null;
            }
        };
    }
    public Model floorModel() {
        return new Model() {
            @Override
            public List<Triangle> getTriangles() {
                return floorTris;
            }

            @Override
            public BSPTree getBSPTree() {
                return null;
            }
        };
    }

}
