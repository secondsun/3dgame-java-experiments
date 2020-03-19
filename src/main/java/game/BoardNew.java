package game;
//uses code from https://www.geeksforgeeks.org/scan-line-polygon-filling-using-opengl-c/

import geometry.*;
import org.la4j.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class BoardNew {

    private static int tileSize = 8;

    private final int screenWidth = 256;
    private final int screenHeight = 192;
    private final int[] palette;
    private final int boardWidth;
    private final int boardHeight;

    private List<Quad> tiles;
    private List<Vertex> verticies;

    private int rotY;
    private int rotX;

    private Map<Point, Matrix> precalcs = Constants.getPrecalcs();
    private Map<Point, Matrix> reversePrecalcs = Constants.getReversePrecalcs();
    private int translateY;
    private int translateX;
    Map<Integer, EdgeTableTuple> edgeTable = new HashMap<>();


    EdgeTableTuple activeEdgeTuple = new EdgeTableTuple();


    public BoardNew(int columns, int rows, int[] palette) {
        this.boardWidth = columns * tileSize;
        this.boardHeight = rows * tileSize;

        this.palette = palette;
        tiles = new ArrayList<>(rows * columns);

        final Map<Point, Vertex> vertexMap = new HashMap<>();

        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {

                var v1 = vertexMap.computeIfAbsent(new Point((x * tileSize) - boardWidth / 2, boardHeight / 2 - (y * tileSize)), point -> new Vertex(point.x, point.y, 0));
                var v2 = vertexMap.computeIfAbsent(new Point((x * tileSize) - boardWidth / 2 + tileSize, boardHeight / 2 - (y * tileSize)), point -> new Vertex(point.x, point.y, 0));
                var v3 = vertexMap.computeIfAbsent(new Point((x * tileSize) - boardWidth / 2, boardHeight / 2 - (y * tileSize) + tileSize), point -> new Vertex(point.x, point.y, 0));
                var v4 = vertexMap.computeIfAbsent(new Point((x * tileSize) - boardWidth / 2 + tileSize, boardHeight / 2 - (y * tileSize) + tileSize), point -> new Vertex(point.x, point.y, 0));

                int paletteSize = palette.length;
                int paletteColorIndex = ((y * tileSize + x) % paletteSize);

                tiles.add(new Quad(v1, v2, v3, v4, palette[paletteColorIndex]));

            }
        }
        verticies = new ArrayList<>(vertexMap.values());

    }

    //Calculate polygons for screen drawing
    public void generateEdgeList() {

        tiles.forEach(quad -> {
            int yMin = Maths.min(quad.v1().y, quad.v2().y, quad.v3().y, quad.v4().y);
            int yMax = Maths.max(quad.v1().y, quad.v2().y, quad.v3().y, quad.v4().y);
            int xMin = Maths.min(quad.v1().x, quad.v2().x, quad.v3().x, quad.v4().x);
            int xMax = Maths.max(quad.v1().x, quad.v2().x, quad.v3().x, quad.v4().x);


            if (yMax < -120 || yMin > 136) {
                //does not intersect with scan line
                return;
            }

            if (xMax < -96 || xMin > 96) {
                //does not intersect with scan line
                return;
            }

            quad.edges().forEach(edge -> {
                storeEdgeInTable(edge, quad.color());
            });


        });
    }

    private void storeEdgeInTable(Edge edge, int color) {
        int x1 = edge.v1.x;
        int y1 = edge.v1.y;
        int x2 = edge.v2.x;
        int y2 = edge.v2.y;

        x1 = Maths.clamp(0, screenWidth, x1);
        x2 = Maths.clamp(0, screenWidth, x2);
        y1 = Maths.clamp(0, screenHeight, y1);
        y2 = Maths.clamp(0, screenHeight, y2);

        float slope, inverseSlope;
        int ymaxTS, xwithyminTS, scanline; //ts stands for to store

        if (x2 == x1) {
            inverseSlope = 0;
        } else {
            slope = ((float) (y2 - y1)) / ((float) (x2 - x1));

            // horizontal lines are not stored in edge table
            if (y2 == y1)
                return;

            inverseSlope = (float) 1.0 / slope;

        }

        if (y1 > y2) {
            scanline = y2;
            ymaxTS = y1;
            xwithyminTS = x2;
        } else {
            scanline = y1;
            ymaxTS = y2;
            xwithyminTS = x1;
        }
        // the assignment part is done..now storage..
        storeEdgeInTuple(edgeTable.computeIfAbsent(scanline, scanLine -> new EdgeTableTuple()), ymaxTS, xwithyminTS, inverseSlope, color);

    }

    private void storeEdgeInTuple(EdgeTableTuple receiver, int ym, int xm, float slopInv, int color) {
        // both used for edgetable and active edge table..
        // The edge tuple sorted in increasing ymax and x of the lower end.
        var edge = new EdgeBucket(ym, xm, slopInv);
        edge.color = color;
        receiver.buckets.add(edge);

        // sort the buckets
        insertionSort(receiver);
    }

    /* Function to sort an array using insertion sort*/
    void insertionSort(EdgeTableTuple ett) {
        int i, j;
        EdgeBucket temp = new EdgeBucket(0, 0, 0);

        for (i = 1; i < ett.buckets.size(); i++) {
            temp.ymax = ett.buckets.get(i).ymax;
            temp.xofymin = ett.buckets.get(i).xofymin;
            temp.slopeinverse = ett.buckets.get(i).slopeinverse;
            j = i - 1;

            while ((j >= 0) && (temp.xofymin < ett.buckets.get(j).xofymin)) {
                ett.buckets.get(j + 1).ymax = ett.buckets.get(j).ymax;
                ett.buckets.get(j + 1).xofymin = ett.buckets.get(j).xofymin;
                ett.buckets.get(j + 1).slopeinverse = ett.buckets.get(j).slopeinverse;
                j = j - 1;
            }
            ett.buckets.get(j + 1).ymax = temp.ymax;
            ett.buckets.get(j + 1).xofymin = temp.xofymin;
            ett.buckets.get(j + 1).slopeinverse = temp.slopeinverse;
        }
    }

    /**
     * Prepopulates the sets in the edge list
     *
     * @return
     */
    private List<Set<Quad>> newEdgeList() {
        var list = new ArrayList<Set<Quad>>(screenHeight);
        for (int i = 0; i < screenHeight; i++) {
            list.add(new HashSet<>());
        }
        return list;
    }


    void removeEdgeByYmax(EdgeTableTuple Tup, int yy) {
        int i, j;
        for (i = 0; i < Tup.buckets.size(); i++) {
            if (Tup.buckets.get(i).ymax == yy) {
                Tup.buckets.remove(i);
                i--;
            }
        }
    }

    public BufferedImage draw() {

        BufferedImage image = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);


        // we will start from scanline 0;
        // Repeat until last scanline:
        for (int sLine = 0; sLine < screenHeight; sLine++)//4. Increment y by 1 (next scan line)
        {
            System.out.println("sLine size is " + sLine);
            int scanLine = sLine;
            // 1. Move from ET bucket y to the
            // AET those edges whose ymin = y (entering edges)
            edgeTable.forEach((index, tuple) -> {
                for (int j = 0; j < tuple.buckets.size(); j++) {
                    storeEdgeInTuple(activeEdgeTuple,
                            tuple.buckets.get(j).ymax,
                            (int) tuple.buckets.get(j).xofymin,
                            tuple.buckets.get(j).slopeinverse,
                            tuple.buckets.get(j).color);
                }


                // 2. Remove from AET those edges for
                // which y=ymax (not involved in next scan line)
                removeEdgeByYmax(activeEdgeTuple, scanLine);

                //sort AET (remember: ET is presorted)
                insertionSort(activeEdgeTuple);


                //3. Fill lines on scan line y by using pairs of x-coords from AET
                int j = 0;
                int FillFlag = 0;
                int coordCount = 0;
                int x1 = 0;
                int x2 = 0;
                int ymax1 = 0;
                int ymax2 = 0;
                while (j < activeEdgeTuple.buckets.size()) {
                    if (coordCount % 2 == 0) {
                        x1 = (int) (activeEdgeTuple.buckets.get(j).xofymin);
                        ymax1 = activeEdgeTuple.buckets.get(j).ymax;
                        if (x1 == x2) {
                        /* three cases can arrive-
                            1. lines are towards top of the intersection
                            2. lines are towards bottom
                            3. one line is towards top and other is towards bottom
                        */
                            if (((x1 == ymax1) && (x2 != ymax2)) || ((x1 != ymax1) && (x2 == ymax2))) {
                                x2 = x1;
                                ymax2 = ymax1;
                            } else {
                                coordCount++;
                            }
                        } else {
                            coordCount++;
                        }
                    } else {
                        x2 = (int) activeEdgeTuple.buckets.get(j).xofymin;
                        ymax2 = activeEdgeTuple.buckets.get(j).ymax;

                        FillFlag = 0;

                        // checking for intersection...
                        if (x1 == x2) {
                    /*three cases can arive-
                        1. lines are towards top of the intersection
                        2. lines are towards bottom
                        3. one line is towards top and other is towards bottom
                    */
                            if (((x1 == ymax1) && (x2 != ymax2)) || ((x1 != ymax1) && (x2 == ymax2))) {
                                x1 = x2;
                                ymax1 = ymax2;
                            } else {
                                coordCount++;
                                FillFlag = 1;
                            }
                        } else {
                            coordCount++;
                            FillFlag = 1;
                        }


                        if (FillFlag != 0) {
                            //drawing actual lines...
                            var gfx = image.getGraphics();
                            gfx.setColor(new Color(activeEdgeTuple.buckets.get(j).color));
                            gfx.drawLine(x1, scanLine, x2, scanLine);

                            // printf("\nLine drawn from %d,%d to %d,%d",x1,i,x2,i);
                        }

                    }

                    j++;
                }


                // 5. For each nonvertical edge remaining in AET, update x for new y
                updatexbyslopeinv(activeEdgeTuple);
            });

        }
        return image;
    }


    void updatexbyslopeinv(EdgeTableTuple Tup) {
        int i;

        for (i = 0; i < Tup.buckets.size(); i++) {
            (Tup.buckets.get(i)).xofymin = (Tup.buckets.get(i)).xofymin + (Tup.buckets.get(i)).slopeinverse;
        }
    }

    public BoardNew rotateY(int rotY) {
        this.rotY = rotY;
        verticies.forEach(tile -> tile.rotateY(rotY));
        return this;
    }

    public BoardNew rotateX(int rotX) {
        this.rotX = rotX;
        verticies.forEach(tile -> tile.rotateX(rotX));
        return this;
    }

    public BoardNew translateX(int i) {
        this.translateX = i;
        verticies.forEach(tile -> tile.translateX(i));
        return this;
    }

    public BoardNew translateY(int i) {
        this.translateY = i;
        verticies.forEach(tile -> tile.translateY(i));
        return this;
    }

    public List<Vertex> getVerticies() {
        return verticies;
    }
}

