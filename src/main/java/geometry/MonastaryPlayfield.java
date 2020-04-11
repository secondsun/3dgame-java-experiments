package geometry;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MonastaryPlayfield  implements Model{

    private final List<Triangle> triangles = new ArrayList<>();
    private static final int TILE_LENGTH = 16;


    public MonastaryPlayfield() {

        drawField();
        drawPath();

    }

    private void drawPath() {
        for (int x = 8; x < 10; x++) {
            for (int y = 11; y > 0; y--) {
                int color = 0;
                boolean isOddRow = (y%2)==0;
                boolean isOddColumn = (x%2)==0;
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

                Triangle northWest = new Triangle(
                        new Vertex(x* TILE_LENGTH, y* TILE_LENGTH,1),
                        new Vertex(x* TILE_LENGTH, (y+1)* TILE_LENGTH,1),
                        new Vertex((x+1)* TILE_LENGTH, (y+1)* TILE_LENGTH,1),
                        color
                );

                Triangle southEast = new Triangle(
                        new Vertex((x+1)* TILE_LENGTH, (y+1)* TILE_LENGTH,1),
                        new Vertex((x+1)* TILE_LENGTH, (y)* TILE_LENGTH,1),
                        new Vertex((x)* TILE_LENGTH, (y)* TILE_LENGTH,1),
                        color
                );

                triangles.add(northWest);
                triangles.add(southEast);

            }
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


            }



        }
    }

    @Override
    public List<Triangle> getTriangles() {
        return triangles;
    }

}
