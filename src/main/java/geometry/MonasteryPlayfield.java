package geometry;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MonasteryPlayfield implements Model{

    private final List<Triangle> triangles = new ArrayList<>();
    public static final int TILE_LENGTH = 16;
    private List<Triangle> ground = new ArrayList<>();
    private List<Triangle> path = new ArrayList<>();
    private List<Triangle> castle = new ArrayList<>();
    private List<Triangle> roof = new ArrayList<>();


    public MonasteryPlayfield() {

        drawField();
        drawPath();
        drawCastle();
        drawRoof();
    }

    private void drawRoof() {

        int color = Color.CYAN.getRGB();

        Triangle tile = new Triangle(
                new Vertex(5 * TILE_LENGTH, 8 * TILE_LENGTH, TILE_LENGTH * 3),
                new Vertex(5 * TILE_LENGTH, (15) * TILE_LENGTH, TILE_LENGTH * 3),
                new Vertex((8.5f) * TILE_LENGTH, (11) * TILE_LENGTH, TILE_LENGTH * 6),
                color
        );

        triangles.add(tile);
        roof.add(tile);
        tile = new Triangle(

                new Vertex((8.5f) * TILE_LENGTH, (11) * TILE_LENGTH, TILE_LENGTH * 6),
                new Vertex(5 * TILE_LENGTH, (15) * TILE_LENGTH, TILE_LENGTH * 3),
                new Vertex(13 * TILE_LENGTH, 15 * TILE_LENGTH, TILE_LENGTH * 3),
                color
        );

        triangles.add(tile);
        roof.add(tile);
        tile = new Triangle(

                new Vertex((8.5f) * TILE_LENGTH, (11) * TILE_LENGTH, TILE_LENGTH * 6),
                new Vertex(13 * TILE_LENGTH, 15 * TILE_LENGTH, TILE_LENGTH * 3),
                new Vertex(13 * TILE_LENGTH, (8) * TILE_LENGTH, TILE_LENGTH * 3),
                color
        );

        triangles.add(tile);
        roof.add(tile);
        tile = new Triangle(
                new Vertex((8.5f) * TILE_LENGTH, (11) * TILE_LENGTH, TILE_LENGTH * 6),
                new Vertex(13 * TILE_LENGTH, (8) * TILE_LENGTH, TILE_LENGTH * 3),
                new Vertex(5 * TILE_LENGTH, 8 * TILE_LENGTH, TILE_LENGTH * 3),
                color
        );

        triangles.add(tile);
        roof.add(tile);
    }

    private void drawCastle() {
        for (int x = 6; x < 12; x++) {
            boolean isOddColumn = (x%2)==0;
            Triangle northWest;
            Triangle southEast;
            int color = 0;
            for (int y = 9; y < 14; y++) {

                boolean isOddRow = (y%2)==0;

                if (isOddColumn) {
                    if (isOddRow) {
                        color = Color.RED.getRGB();
                    } else {
                        color = Color.ORANGE.getRGB();
                    }
                } else {
                    if (isOddRow) {
                        color = Color.ORANGE.getRGB();
                    } else {
                        color = Color.RED.getRGB();
                    }
                }

                northWest = new Triangle(
                        new Vertex(x* TILE_LENGTH, y* TILE_LENGTH,TILE_LENGTH*3),
                        new Vertex(x* TILE_LENGTH, (y+1)* TILE_LENGTH,TILE_LENGTH*3),
                        new Vertex((x+1)* TILE_LENGTH, (y+1)* TILE_LENGTH,TILE_LENGTH*3),
                        color
                );

                southEast = new Triangle(
                        new Vertex((x+1)* TILE_LENGTH, (y+1)* TILE_LENGTH,TILE_LENGTH*3),
                        new Vertex((x+1)* TILE_LENGTH, (y)* TILE_LENGTH,TILE_LENGTH*3),
                        new Vertex((x)* TILE_LENGTH, (y)* TILE_LENGTH,TILE_LENGTH*3),
                        color
                );

                triangles.add(northWest);
                triangles.add(southEast);
                castle.add(northWest);
                castle.add(southEast);
                if (x==6) {//draw "wall" of path

                    northWest = new Triangle(
                            new Vertex((x)* TILE_LENGTH, (y+1)* TILE_LENGTH,TILE_LENGTH*3),
                            new Vertex(x* TILE_LENGTH, (y)* TILE_LENGTH,TILE_LENGTH*3),
                            new Vertex(x* TILE_LENGTH, y* TILE_LENGTH,0),
                            color
                    );

                    southEast = new Triangle(
                            new Vertex((x)* TILE_LENGTH, (y)* TILE_LENGTH,0),
                            new Vertex((x)* TILE_LENGTH, (y+1)* TILE_LENGTH,0),
                            new Vertex((x)* TILE_LENGTH, (y+1)* TILE_LENGTH,TILE_LENGTH*3),
                            color
                    );

                    triangles.add(northWest);
                    triangles.add(southEast);
                    castle.add(northWest);
                    castle.add(southEast);
                } else if (x==11){
                    northWest = new Triangle(
                            new Vertex((x+1)* TILE_LENGTH, (y)* TILE_LENGTH,TILE_LENGTH*3),
                            new Vertex((x+1)* TILE_LENGTH, (y+1)* TILE_LENGTH,TILE_LENGTH*3),
                            new Vertex((x+1)* TILE_LENGTH, y* TILE_LENGTH,0),
                            color
                    );

                    southEast = new Triangle(
                            new Vertex(((x+1))* TILE_LENGTH, (y+1)* TILE_LENGTH,0),
                            new Vertex(((x+1))* TILE_LENGTH, (y)* TILE_LENGTH,0),
                            new Vertex(((x+1))* TILE_LENGTH, (y+1)* TILE_LENGTH,TILE_LENGTH*3),
                            color
                    );

                    triangles.add(northWest);
                    triangles.add(southEast);
                    castle.add(northWest);
                    castle.add(southEast);
                }

            }



            northWest = new Triangle(
                    new Vertex((x)* TILE_LENGTH, (9)* TILE_LENGTH,0),
                    new Vertex(x* TILE_LENGTH, (9)* TILE_LENGTH,TILE_LENGTH*3),
                    new Vertex((x+1)* TILE_LENGTH, 9* TILE_LENGTH,TILE_LENGTH*3),
                    color
            );

            southEast = new Triangle(
                    new Vertex((x+1)* TILE_LENGTH, (9)* TILE_LENGTH,TILE_LENGTH*3),
                    new Vertex((x+1)* TILE_LENGTH, (9)* TILE_LENGTH,0),
                    new Vertex((x)* TILE_LENGTH, (9)* TILE_LENGTH,0),
                    color
            );

            triangles.add(northWest);
            triangles.add(southEast);
            castle.add(northWest);
            castle.add(southEast);
            northWest = new Triangle(
                    new Vertex((x)* TILE_LENGTH, (9)* TILE_LENGTH,0),
                    new Vertex(x* TILE_LENGTH, (9)* TILE_LENGTH,TILE_LENGTH*3),
                    new Vertex((x+1)* TILE_LENGTH, 9* TILE_LENGTH,TILE_LENGTH*3),
                    color
            );

            southEast = new Triangle(
                    new Vertex((x+1)* TILE_LENGTH, (9)* TILE_LENGTH,TILE_LENGTH*3),
                    new Vertex((x+1)* TILE_LENGTH, (9)* TILE_LENGTH,0),
                    new Vertex((x)* TILE_LENGTH, (9)* TILE_LENGTH,0),
                    color
            );

            triangles.add(northWest);
            triangles.add(southEast);
            castle.add(northWest);
            castle.add(southEast);
            northWest = new Triangle(
                    new Vertex((x+1)* TILE_LENGTH, 14* TILE_LENGTH,TILE_LENGTH*3),
                    new Vertex(x* TILE_LENGTH, (14)* TILE_LENGTH,TILE_LENGTH*3),
                    new Vertex((x)* TILE_LENGTH, (14)* TILE_LENGTH,0),
                    color
            );

            southEast = new Triangle(
                    new Vertex((x)* TILE_LENGTH, (14)* TILE_LENGTH,0),
                    new Vertex((x+1)* TILE_LENGTH, (14)* TILE_LENGTH,0),
                    new Vertex((x+1)* TILE_LENGTH, (14)* TILE_LENGTH,TILE_LENGTH*3),
                    color
            );

            triangles.add(northWest);
            triangles.add(southEast);
            castle.add(northWest);
            castle.add(southEast);
        }
    }

    private void drawPath() {
        for (int x = 8; x < 10; x++) {
            boolean isOddColumn = (x%2)==0;
            Triangle northWest;
            Triangle southEast;
            int color = 0;
            for (int y = 8; y >= 0; y--) {

                boolean isOddRow = (y%2)==0;

                if (isOddColumn) {
                    if (isOddRow) {
                        color = Color.DARK_GRAY.getRGB();
                    } else {
                        color = Color.LIGHT_GRAY.getRGB();
                    }
                } else {
                    if (isOddRow) {
                        color = Color.LIGHT_GRAY.getRGB();
                    } else {
                        color = Color.DARK_GRAY.getRGB();
                    }
                }

                northWest = new Triangle(
                        new Vertex(x* TILE_LENGTH, y* TILE_LENGTH,4),
                        new Vertex(x* TILE_LENGTH, (y+1)* TILE_LENGTH,4),
                        new Vertex((x+1)* TILE_LENGTH, (y+1)* TILE_LENGTH,4),
                        color
                );

                southEast = new Triangle(
                        new Vertex((x+1)* TILE_LENGTH, (y+1)* TILE_LENGTH,4),
                        new Vertex((x+1)* TILE_LENGTH, (y)* TILE_LENGTH,4),
                        new Vertex((x)* TILE_LENGTH, (y)* TILE_LENGTH,4),
                        color
                );

                triangles.add(northWest);
                triangles.add(southEast);
                path.add(northWest);
                path.add(southEast);
                if (isOddColumn) {//draw "wall" of path

                    northWest = new Triangle(
                            new Vertex((x)* TILE_LENGTH, (y+1)* TILE_LENGTH,4),
                            new Vertex(x* TILE_LENGTH, (y)* TILE_LENGTH,4),
                            new Vertex(x* TILE_LENGTH, y* TILE_LENGTH,0),
                            color
                    );

                    southEast = new Triangle(
                            new Vertex((x)* TILE_LENGTH, (y)* TILE_LENGTH,0),
                            new Vertex((x)* TILE_LENGTH, (y+1)* TILE_LENGTH,0),
                            new Vertex((x)* TILE_LENGTH, (y+1)* TILE_LENGTH,4),
                            color
                    );

                    triangles.add(northWest);
                    triangles.add(southEast);
                    path.add(northWest);
                    path.add(southEast);
                } else {
                    northWest = new Triangle(
                            new Vertex((x+1)* TILE_LENGTH, (y)* TILE_LENGTH,4),
                            new Vertex((x+1)* TILE_LENGTH, (y+1)* TILE_LENGTH,4),
                            new Vertex((x+1)* TILE_LENGTH, y* TILE_LENGTH,0),

                            color
                    );

                    southEast = new Triangle(
                            new Vertex(((x+1))* TILE_LENGTH, (y+1)* TILE_LENGTH,0),
                            new Vertex(((x+1))* TILE_LENGTH, (y)* TILE_LENGTH,0),
                            new Vertex(((x+1))* TILE_LENGTH, (y+1)* TILE_LENGTH,4),
                            color
                    );

                    triangles.add(northWest);
                    triangles.add(southEast);
                    path.add(northWest);
                    path.add(southEast);
                }

            }

            northWest = new Triangle(
                    new Vertex((x)* TILE_LENGTH, (0)* TILE_LENGTH,0),
                    new Vertex(x* TILE_LENGTH, (0)* TILE_LENGTH,4),
                    new Vertex((x+1)* TILE_LENGTH, 0* TILE_LENGTH,4),
                    color
            );

            southEast = new Triangle(
                    new Vertex((x+1)* TILE_LENGTH, (0)* TILE_LENGTH,4),
                    new Vertex((x+1)* TILE_LENGTH, (0)* TILE_LENGTH,0),
                    new Vertex((x)* TILE_LENGTH, (0)* TILE_LENGTH,0),
                    color
            );

            triangles.add(northWest);
            triangles.add(southEast);
            path.add(northWest);
            path.add(southEast);


        }

    }

    private void drawField() {
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                int color = 0;
                boolean isOddRow = (y%2)==0;
                boolean isOddColumn = (x%2)==0;
                if (isOddColumn) {
                    if (isOddRow) {
                        color = Color.GREEN.getRGB();
                    } else {
                        color = new Color(0,128,0).getRGB();
                    }
                } else {
                    if (isOddRow) {
                        color = new Color(0,128,0).getRGB();
                    } else {
                        color = Color.GREEN.getRGB();
                    }
                }

                Triangle northWest = new Triangle(
                        new Vertex(x* TILE_LENGTH, y* TILE_LENGTH,0),
                        new Vertex(x* TILE_LENGTH, (y+1)* TILE_LENGTH,0),
                        new Vertex((x+1)* TILE_LENGTH, (y+1)* TILE_LENGTH,0),
                        color
                );

                Triangle southEast = new Triangle(
                        new Vertex((x+1)* TILE_LENGTH, (y+1)* TILE_LENGTH,0),
                        new Vertex((x+1)* TILE_LENGTH, (y)* TILE_LENGTH,0),
                        new Vertex((x)* TILE_LENGTH, (y)* TILE_LENGTH,0),
                        color
                );

                triangles.add(northWest);
                triangles.add(southEast);
                ground.add(northWest);
                ground.add(southEast);


            }



        }
        Triangle planSortHelper = new Triangle(
                new Vertex((4) * TILE_LENGTH, (4) * TILE_LENGTH, -1),
                new Vertex((4) * TILE_LENGTH, (4) * TILE_LENGTH, -1),
                new Vertex((4) * TILE_LENGTH, (4) * TILE_LENGTH, -1),
                Color.black.getRGB()
        );
        triangles.add(planSortHelper);
        ground.add(planSortHelper);
    }

    @Override
    public List<Triangle> getTriangles() {
        return triangles;
    }

    public Model field() {
        return Model.of(this.ground);
    }

    public Model castle() {
        return Model.of(this.castle);
    }

    public Model roof() {
        return Model.of(this.roof);
    }

    public Model path() {
        return Model.of(this.path);
    }
}
