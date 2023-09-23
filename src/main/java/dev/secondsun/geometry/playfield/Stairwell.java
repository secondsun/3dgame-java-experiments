package dev.secondsun.geometry.playfield;

import dev.secondsun.geometry.Model;
import dev.secondsun.geometry.Triangle;
import dev.secondsun.geometry.Vertex;
import dev.secondsun.geometry.Vertex2D;
import dev.secondsun.util.BSPTree;
import dev.secondsun.util.BoundedCube;
import dev.secondsun.util.CubeBuilder;
import dev.secondsun.util.Resources;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Stairwell implements Model {

    private static final int TILE_LENGTH = 16;
    private final BSPTree root;
    private List<Triangle> triangles;
    private  final Integer woodId1,woodId2,booksId1,booksId2;
    private final Resources resources;
    private  final int floorId2, floorId1,bedId2, bedId1,bedUpId2, bedUpId1;

     public Stairwell(Resources resources) {
         this.resources = resources;
        //loadTextures and resources
        try {
            int woodID = resources.setImage(ImageIO.read(Stairwell.class.getClassLoader().getResourceAsStream("dormroom/wood.png")));
            int booksID = resources.setImage(ImageIO.read(Stairwell.class.getClassLoader().getResourceAsStream("dormroom/books.png")));
            int floorID = resources.setImage(ImageIO.read(Stairwell.class.getClassLoader().getResourceAsStream("dormroom/floor.png")));
            int bedID = resources.setImage(ImageIO.read(Stairwell.class.getClassLoader().getResourceAsStream("dormroom/bed.png")));
            int bedUpID = resources.setImage(ImageIO.read(Stairwell.class.getClassLoader().getResourceAsStream("dormroom/bedUp.png")));

            woodId1 = resources.setTexture(woodID, new Vertex2D(0,0),15,15);
            woodId2 = resources.setTexture(woodID, new Vertex2D(15,15),-15,-15);

            booksId1 = resources.setTexture(booksID, new Vertex2D(0,0),32,47);
            booksId2 = resources.setTexture(booksID, new Vertex2D(32,47),-32,-47);
            floorId1 = resources.setTexture(floorID, new Vertex2D(0,0),100,100);
            floorId2 = resources.setTexture(floorID, new Vertex2D(100,100),-100,-100);


            bedId1 = resources.setTexture(bedID, new Vertex2D(0,0),15,31);
            bedId2 = resources.setTexture(bedID, new Vertex2D(15,31),-15,-31);
            bedUpId1 = resources.setTexture(bedUpID, new Vertex2D(0,0),31,15);
            bedUpId2 = resources.setTexture(bedUpID, new Vertex2D(31,15),-31,-15);

            triangles = new ArrayList<>();
            bookshelf();
            bed();
            floor();
    
            this.root = new BSPTree();
            root.add(new BoundedCube(floorModel()));
            root.add(new BoundedCube(bookshelfModel()));
            root.add(new BoundedCube(bedModel()));
    

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<Triangle> floorTris;
    private ArrayList<Triangle> mattressTries;
    private ArrayList<Triangle> bookshelfTriangles;
    private ArrayList<Triangle> bedLegsTriangles;
    private ArrayList<Triangle> bedTries;

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
                .setRight1(booksId1)
                .setRight2(booksId2)
                .setLeft1(booksId1)
                .setLeft2(booksId2)
                .setFar1(woodId1)
                .setFar2(woodId2)
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
                .setRight1(Color.WHITE.getRGB())
                .setRight2(Color.WHITE.getRGB())
                .setLeft1(Color.cyan.getRGB())
                .setLeft2(Color.cyan.getRGB())
                .setFar1(bedUpId1)
                .setFar2(bedUpId2)
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
                .setRight1(0xA0522D)
                .setRight2(0xA0522D)
                .setLeft1(0xA0522D)
                .setLeft2(0xA0522D)
                .setFar1(0xA0522D)
                .setFar2(0xA0522D)
                .disableUp()
                .disableDown();
        bedLegsTriangles.addAll(builder.cube().getTriangles());
        builder = new CubeBuilder(new Vertex(1*TILE_LENGTH+4,TILE_LENGTH-4,0), 4,TILE_LENGTH-4,4);
        builder.setBottom1(0xA0522D)
                .setBottom2(0xA0522D)
                .setTop1(0xA0522D)
                .setTop2(0xA0522D)
                .setRight1(0xA0522D)
                .setRight2(0xA0522D)
                .setLeft1(0xA0522D)
                .setLeft2(0xA0522D)
                .setFar1(0xA0522D)
                .setFar2(0xA0522D)
                .disableUp()
                .disableDown();
        bedLegsTriangles.addAll(builder.cube().getTriangles());
        builder = new CubeBuilder(new Vertex(3*TILE_LENGTH-4,4,0), 4,TILE_LENGTH-4,4);
        builder.setBottom1(0xA0522D)
                .setBottom2(0xA0522D)
                .setTop1(0xA0522D)
                .setTop2(0xA0522D)
                .setRight1(0xA0522D)
                .setRight2(0xA0522D)
                .setLeft1(0xA0522D)
                .setLeft2(0xA0522D)
                .setFar1(0xA0522D)
                .setFar2(0xA0522D)
                .disableUp()
                .disableDown();
        bedLegsTriangles.addAll(builder.cube().getTriangles());
        builder = new CubeBuilder(new Vertex(3*TILE_LENGTH-4,TILE_LENGTH-4,0), 4,TILE_LENGTH-4,4);
        builder.setBottom1(0xA0522D)
                .setBottom2(0xA0522D)
                .setTop1(0xA0522D)
                .setTop2(0xA0522D)
                .setRight1(0xA0522D)
                .setRight2(0xA0522D)
                .setLeft1(0xA0522D)
                .setLeft2(0xA0522D)
                .setFar1(0xA0522D)
                .setFar2(0xA0522D)
                .disableUp()
                .disableDown();
        bedLegsTriangles.addAll(builder.cube().getTriangles());
        triangles.addAll(bedLegsTriangles);
    }

    @Override
    public List<Triangle> getTriangles() {
        return triangles;
    }

    public BSPTree getBSPTree() {

        return root;
    }

    public Model bedModel() {
        return new Model() {
            @Override
            public List<Triangle> getTriangles() {
                return bedTries;
            }

        };
    }
    public Model bookshelfModel() {
        return new Model() {
            @Override
            public List<Triangle> getTriangles() {
                return bookshelfTriangles;
            }

        };
    }
    public Model floorModel() {
        return new Model() {
            @Override
            public List<Triangle> getTriangles() {
                return floorTris;
            }

        };
    }

}
