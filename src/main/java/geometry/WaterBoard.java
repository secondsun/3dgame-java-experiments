package geometry;

import game.Resources;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class WaterBoard implements Model {


    private final int boardWidth;
    private final int boardHeight;

    private List<Triangle> tiles;

    private static BufferedImage waterTexture;

    private static final int imageId;

    static {
        try {
            waterTexture = ImageIO.read(Model.class.getResourceAsStream("/water_texture.png"));
            imageId = Resources.setImage(waterTexture);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public WaterBoard(int columns, int rows, int scale, int[] palette) {
        var random = new Random(columns);


        int tileSize = 16 * scale;
        this.boardWidth = columns * tileSize;
        this.boardHeight = rows * tileSize;

        tiles = new ArrayList<>(rows * columns);

        Map<Vertex, Vertex> vertexMap = new HashMap<>();

        for (int x = 0; x < columns/2; x++) {
            for (int y = 0; y < rows/2; y++) {
                vertexMap = new HashMap<>();

                int paletteSize = palette.length;
                int paletteColorIndex = ((y * tileSize + x) % paletteSize);
                int zOff = 1;//random.nextInt(7);
                var v1 = vertexMap.computeIfAbsent(new Vertex((x * tileSize), (y * tileSize), zOff * tileSize),
                        point -> new Vertex(point.x - boardWidth / 2, point.y - boardHeight / 2, point.z));
                var v2 = vertexMap.computeIfAbsent(new Vertex((x * tileSize), (y * tileSize) + tileSize, zOff * tileSize),
                        point -> new Vertex(point.x - boardWidth / 2, point.y - boardHeight / 2, point.z));
                var v3 = vertexMap
                        .computeIfAbsent(new Vertex((x * tileSize) + tileSize, (y * tileSize) + tileSize, zOff * tileSize),
                                point -> new Vertex(point.x - boardWidth / 2, point.y - boardHeight / 2, point.z));
                var v4 = vertexMap.computeIfAbsent(new Vertex((x * tileSize) + tileSize, (y * tileSize), zOff * tileSize),
                        point -> new Vertex(point.x - boardWidth / 2, point.y - boardHeight / 2, point.z));

                var v5 = vertexMap.computeIfAbsent(new Vertex((x * tileSize), (y * tileSize), (zOff + 1) * tileSize),
                        point -> new Vertex(point.x - boardWidth / 2, point.y - boardHeight / 2, point.z));
                var v6 = vertexMap.computeIfAbsent(new Vertex((x * tileSize), (y * tileSize) + tileSize, (zOff + 1) * tileSize),
                        point -> new Vertex(point.x - boardWidth / 2, point.y - boardHeight / 2, point.z));
                var v7 = vertexMap
                        .computeIfAbsent(new Vertex((x * tileSize) + tileSize, (y * tileSize) + tileSize, (zOff + 1) * tileSize),
                                point -> new Vertex(point.x - boardWidth / 2, point.y - boardHeight / 2, point.z));
                var v8 = vertexMap.computeIfAbsent(new Vertex((x * tileSize) + tileSize, (y * tileSize), (zOff + 1) * tileSize),
                        point -> new Vertex(point.x - boardWidth / 2, point.y - boardHeight / 2, point.z));


                var texId1 = Resources.setTexture(imageId, new Vertex(0, 16, 0), 16, -16);
                var texId2 = Resources.setTexture(imageId, new Vertex(16, 0, 0), -16, 16);
                var texId3 = Resources.setTexture(imageId, new Vertex(16, 16, 0), -16, -16);
                var texId4 = Resources.setTexture(imageId, new Vertex(0, 16, 0), 16, -16);


                var cube = new Triangle[]{
                        new Triangle(v1, v2, v3, texId1),//SOUTH
                        new Triangle( v3,v4, v1,  texId2),//SOUTH
//                        new Triangle(v4, v3, v7, Color.DARK_GRAY.getRGB()),//EAST
//                        new Triangle(v4, v7, v8, Color.DARK_GRAY.getRGB()),//EAST
                        new Triangle(v8, v7, v6, texId3),//NORTH
                        new Triangle(v6,v5,v8,   texId4),//NORTH
//                        new Triangle(v5, v6, v2, Color.DARK_GRAY.getRGB()),//WEST
//                        new Triangle(v5, v2, v1, Color.DARK_GRAY.getRGB()),//WEST
//                        new Triangle(v2, v6, v7, Color.DARK_GRAY.getRGB()),//TOP
//                        new Triangle(v2, v7, v3, Color.DARK_GRAY.getRGB()),//TOP
//                        new Triangle(v8, v5, v1, Color.DARK_GRAY.getRGB()),//BOTTOM
//                        new Triangle(v8, v1, v4, Color.DARK_GRAY.getRGB()),//BOTTOM

                };


                for (Triangle q : cube) {
                    tiles.add(q);
                }


            }

        }


    }

    @Override
    public List<Triangle> getTriangles() {
        return new ArrayList<>(tiles);
    }

}

