package game;

import geometry.Constants;
import geometry.Quad;
import geometry.Vertex;
import org.la4j.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Board {

    private static int tileSize = 8;

    private final int screenWidth;
    private final int screenHeight;
    private final int[] palette;

    private List<Quad> tiles;
    private final int boardHeight;
    private final int boardWidth;
    private int rotY;
    private int rotX;

    private Map<Point, Matrix> precalcs = Constants.getPrecalcs();
    private Map<Point, Matrix> reversePrecalcs = Constants.getReversePrecalcs();
    public Board(int screenWidth, int screenHeight, int[] palette) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

        this.boardHeight = screenHeight / tileSize;
        this.boardWidth = screenWidth / tileSize;

        this.palette = palette;
        tiles = new ArrayList<>(boardHeight * boardWidth);

        for (int x = 0; x < boardWidth; x++) {
            for (int y = 0; y < boardHeight; y++) {

                var v1 = new Vertex(x, y, 0);
                var v2 = new Vertex(x + tileSize, y, 0);
                var v3 = new Vertex(x, y + tileSize, 0);
                var v4 = new Vertex(x + tileSize, y + tileSize, 0);

                int tileX = (x / tileSize);
                int tileY = (y / tileSize);

                int paletteSize = palette.length;
                int paletteColorIndex = ((tileY * tileSize + tileX) % paletteSize);

                tiles.add(new Quad(v1, v2, v3, v4, palette[paletteColorIndex]));

            }
        }

    }

    public int draw(int screenX, int screenY, int defaultColor) {

        Matrix m = new Basic2DMatrix(new double[][]{
                {screenX<<7},
                {screenY<<7},
                {128}
        });

        Matrix rot = reversePrecalcs.get(new Point(rotX, rotY));
        if (rot == null) {
            throw new NullPointerException(rotX +"@"+rotY);
        }
        var result = rot.multiply(m);
         screenX = ((int)result.get(0,0)) >> 14;
         screenY = ((int)result.get(1,0)) >> 14;



        if (screenX > screenWidth - 1 || screenX < 0 || screenY > screenHeight - 1 || screenY < 0) {
            return defaultColor;
        }



        return palette[(((screenY)/tileSize + screenX/tileSize))%palette.length];

    }

    public Board rotateY(int rotY) {
        this.rotY = rotY;
        //tiles = tiles.stream().map(tile -> tile.rotateY(rotY)).collect(Collectors.toList());
        return this;
    }

    public Board rotateX(int rotX) {
        this.rotX = rotX;
        //tiles = tiles.stream().map(tile -> tile.rotateX(rotX)).collect(Collectors.toList());
        return this;
    }

    public Board translateX(int i) {
        //tiles = tiles.stream().map(tile -> tile.translateX(i)).collect(Collectors.toList());
        return this;
    }

    public Board translateY(int i) {
        //tiles = tiles.stream().map(tile -> tile.translateY(i)).collect(Collectors.toList());
        return this;
    }
}

