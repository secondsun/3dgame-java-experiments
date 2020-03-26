package geometry;

import java.util.*;

public class BoardNew implements Model{
    private static final int scale = 1;
    private static int tileSize = 8 * scale;

    private final int boardWidth;
    private final int boardHeight;

    private List<Triangle> tiles;
    private List<Vertex> verticies;

    public BoardNew(int columns, int rows, int[] palette) {
        var random = new Random(columns);
        this.boardWidth = columns * tileSize;
        this.boardHeight = rows * tileSize;

        tiles = new ArrayList<>(rows * columns);

        final Map<Vertex, Vertex> vertexMap = new HashMap<>();

        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {


                int paletteSize = palette.length;
                int paletteColorIndex = ((y * tileSize + x) % paletteSize);
                int zOff = random.nextInt(5);
                var v1 = vertexMap.computeIfAbsent(new Vertex((x * tileSize), (y * tileSize), zOff * tileSize),
                        point -> new Vertex(point.x - boardWidth/2, point.y - boardHeight/2, point.z));
                var v2 = vertexMap.computeIfAbsent(new Vertex((x * tileSize), (y * tileSize) + tileSize, zOff * tileSize),
                        point -> new Vertex(point.x - boardWidth/2, point.y - boardHeight/2, point.z));
                var v3 = vertexMap
                        .computeIfAbsent(new Vertex((x * tileSize) + tileSize, (y * tileSize) + tileSize, zOff * tileSize),
                                point -> new Vertex(point.x - boardWidth/2, point.y - boardHeight/2, point.z));
                var v4 = vertexMap.computeIfAbsent(new Vertex((x * tileSize) + tileSize, (y * tileSize), zOff * tileSize),
                        point -> new Vertex(point.x - boardWidth/2, point.y - boardHeight/2, point.z));

                var v5 = vertexMap.computeIfAbsent(new Vertex((x * tileSize), (y * tileSize), (zOff+1) * tileSize),
                        point -> new Vertex(point.x - boardWidth/2, point.y - boardHeight/2, point.z));
                var v6 = vertexMap.computeIfAbsent(new Vertex((x * tileSize), (y * tileSize) + tileSize, (zOff+1) * tileSize),
                        point -> new Vertex(point.x - boardWidth/2, point.y - boardHeight/2, point.z));
                var v7 = vertexMap
                        .computeIfAbsent(new Vertex((x * tileSize) + tileSize, (y * tileSize) + tileSize, (zOff+1) * tileSize),
                                point -> new Vertex(point.x - boardWidth/2, point.y - boardHeight/2, point.z));
                var v8 = vertexMap.computeIfAbsent(new Vertex((x * tileSize) + tileSize, (y * tileSize), (zOff+1) * tileSize),
                        point -> new Vertex(point.x - boardWidth/2, point.y - boardHeight/2, point.z));



                var cube = new Triangle[]{
                        new Triangle(v1, v2, v3, palette[paletteColorIndex]),//SOUTH
                        new Triangle(v1, v3, v4, palette[paletteColorIndex]),//SOUTH
                        new Triangle(v4, v3, v7, palette[paletteColorIndex]),//EAST
                        new Triangle(v4, v7, v8, palette[paletteColorIndex]),//EAST
                        new Triangle(v8, v7, v6, palette[paletteColorIndex]),//NORTH
                        new Triangle(v8, v6, v5, palette[paletteColorIndex]),//NORTH
                        new Triangle(v5, v6, v2, palette[paletteColorIndex]),//WEST
                        new Triangle(v5, v2, v1, palette[paletteColorIndex]),//WEST
                        new Triangle(v2, v6, v7, palette[paletteColorIndex]),//TOP
                        new Triangle(v2, v7, v3, palette[paletteColorIndex]),//TOP
                        new Triangle(v8, v5, v1, palette[paletteColorIndex]),//BOTTOM
                        new Triangle(v8, v1, v4, palette[paletteColorIndex]),//BOTTOM

                };


                for (Triangle q : cube) {
                    tiles.add(q);
                }

            }
        }
        verticies = new ArrayList<>(vertexMap.values());

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

