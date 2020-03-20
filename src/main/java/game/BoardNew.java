package game;
//uses code from https://www.geeksforgeeks.org/scan-line-polygon-filling-using-opengl-c/

import geometry.*;
import org.la4j.Matrix;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

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
    EdgeTableTuple[] edgeTable = createEdgeTupleTable(screenHeight);

    private EdgeTableTuple[] createEdgeTupleTable(int i) {

        var edgeTable = new EdgeTableTuple[i];
        for (int x = 0; x < i; x++) {
            edgeTable[x] = new EdgeTableTuple(-1);
        }
        return edgeTable;
    }

    EdgeTableTuple activeEdgeTuple = new EdgeTableTuple(-1);

    // Scanline Function
    void initEdgeTable() {
        int i;
        for (i = 0; i < screenHeight; i++) {
            edgeTable[i].countEdgeBucket = 0;
        }

        activeEdgeTuple.countEdgeBucket = 0;
    }


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
        initEdgeTable();

        tiles.forEach(quad -> {
            int yMin = Maths.min(quad.v1().y, quad.v2().y, quad.v3().y, quad.v4().y);
            int yMax = Maths.max(quad.v1().y, quad.v2().y, quad.v3().y, quad.v4().y);
            int xMin = Maths.min(quad.v1().x, quad.v2().x, quad.v3().x, quad.v4().x);
            int xMax = Maths.max(quad.v1().x, quad.v2().x, quad.v3().x, quad.v4().x);


            if (yMax < -120 || yMin > 136) {
                //does not intersect with scan line
                return;
            }

            if (xMax < -96 || xMin > 1396) {
                //does not intersect with scan line
                return;
            }

            var pair = quad.edges();

            pair.first.forEach(this::storeEdgeInTable);
            pair.second.forEach(this::storeEdgeInTable);

        });
        //For Each Quad
        //For each Edge calculate yMin and yMax
        //if edge is horizontal, ignore it
        // else if yMax on scan-lie, ignore it
        // else if ymin<y<yMax add edge to scnalLine y's edge list
        //add "lower" tiles with high z values to edge list
    }

    private void storeEdgeInTable(Edge edge) {
        int x1 = edge.v1.x;
        int y1 = edge.v1.y;
        int x2 = edge.v2.x;
        int y2 = edge.v2.y;

        x1 = Maths.clamp(0, 255, x1);
        x2 = Maths.clamp(0, 255, x2);
        y1 = Maths.clamp(0, screenHeight, y1);
        y2 = Maths.clamp(0, screenHeight, y2);

        float m, minv;
        int ymaxTS, xwithyminTS, scanline; //ts stands for to store

        if (x2 == x1) {
            minv = 0;
        } else {
            m = ((float) (y2 - y1)) / ((float) (x2 - x1));

            // horizontal lines are not stored in edge table
            if (y2 == y1)
                return;

            minv = (float) 1.0 / m;

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
        storeEdgeInTuple(edgeTable[scanline], ymaxTS, xwithyminTS, minv,edge.polyId);

    }

    private void storeEdgeInTuple(EdgeTableTuple receiver, int ym, int xm, float slopInv, int polyId) {
        // both used for edgetable and active edge table..
        // The edge tuple sorted in increasing ymax and x of the lower end.
        (receiver.buckets[(receiver).countEdgeBucket]).ymax = ym;
        (receiver.buckets[(receiver).countEdgeBucket]).xofymin = (float) xm;
        (receiver.buckets[(receiver).countEdgeBucket]).slopeinverse = slopInv;
        (receiver.buckets[(receiver).countEdgeBucket]).polyId = polyId;

        // sort the buckets
        insertionSort(receiver);

        receiver.countEdgeBucket++;
    }

    /* Function to sort an array using insertion sort*/
    void insertionSort(EdgeTableTuple ett) {
        int i, j;
        EdgeBucket temp = new EdgeBucket(0, 0, 0, -1);

        for (i = 1; i < ett.countEdgeBucket; i++) {
            temp.ymax = ett.buckets[i].ymax;
            temp.xofymin = ett.buckets[i].xofymin;
            temp.slopeinverse = ett.buckets[i].slopeinverse;
            temp.polyId = ett.buckets[i].polyId;
            j = i - 1;

            while ((j >= 0) && (temp.xofymin < ett.buckets[j].xofymin)) {
                ett.buckets[j + 1].ymax = ett.buckets[j].ymax;
                ett.buckets[j + 1].xofymin = ett.buckets[j].xofymin;
                ett.buckets[j + 1].slopeinverse = ett.buckets[j].slopeinverse;
                ett.buckets[j + 1].polyId = ett.buckets[j].polyId;
                j = j - 1;
            }
            ett.buckets[j + 1].ymax = temp.ymax;
            ett.buckets[j + 1].xofymin = temp.xofymin;
            ett.buckets[j + 1].slopeinverse = temp.slopeinverse;
            ett.buckets[j + 1].polyId = temp.polyId;
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


    void removeEdgeByYmax(EdgeTableTuple Tup, int yy, int polyId) {
        int i, j;
        for (i = 0; i < Tup.countEdgeBucket; i++) {
            if (Tup.buckets[i].ymax == yy && Tup.buckets[i].polyId == polyId) {


                for (j = i; j < Tup.countEdgeBucket - 1; j++) {
                    Tup.buckets[j].ymax = Tup.buckets[j + 1].ymax;
                    Tup.buckets[j].xofymin = Tup.buckets[j + 1].xofymin;
                    Tup.buckets[j].slopeinverse = Tup.buckets[j + 1].slopeinverse;
                    Tup.buckets[j].polyId = Tup.buckets[j + 1].polyId;
                }
                Tup.countEdgeBucket--;
                i--;
            }
        }
    }

    public BufferedImage draw() {

        BufferedImage image = new BufferedImage(256, screenHeight, BufferedImage.TYPE_INT_RGB);
        int i, j, x1, ymax1, x2, ymax2, FillFlag = 0, coordCount;

        // we will start from scanline 0;
        // Repeat until last scanline:
        for (i = 0; i < screenHeight; i++)//4. Increment y by 1 (next scan line)
        {

            // 1. Move from ET bucket y to the
            // AET those edges whose ymin = y (entering edges)
            for (j = 0; j < edgeTable[i].countEdgeBucket; j++) {
                storeEdgeInTuple(activeEdgeTuple,
                        edgeTable[i].buckets[j].ymax,
                        (int) edgeTable[i].buckets[j].xofymin,
                        edgeTable[i].buckets[j].slopeinverse,
                        edgeTable[i].buckets[j].polyId);
            }


            // 2. Remove from AET those edges for
            // which y=ymax (not involved in next scan line)
            removeEdgeByYmax(activeEdgeTuple, i, activeEdgeTuple.polyId);

            //sort AET (remember: ET is presorted)
            insertionSort(activeEdgeTuple);


            //3. Fill lines on scan line y by using pairs of x-coords from AET
            j = 0;
            FillFlag = 0;
            coordCount = 0;
            x1 = 0;
            x2 = 0;
            ymax1 = 0;
            ymax2 = 0;
            while (j < activeEdgeTuple.countEdgeBucket) {
                if (coordCount % 2 == 0) {
                    x1 = (int) (activeEdgeTuple.buckets[j].xofymin);
                    ymax1 = activeEdgeTuple.buckets[j].ymax;
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
                    x2 = (int) activeEdgeTuple.buckets[j].xofymin;
                    ymax2 = activeEdgeTuple.buckets[j].ymax;

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
                        gfx.setColor(new Color(0.0f, 0.7f, 0.0f));
                        gfx.drawLine(x1, i, x2, i);

                        // printf("\nLine drawn from %d,%d to %d,%d",x1,i,x2,i);
                    }

                }

                j++;
            }


            // 5. For each nonvertical edge remaining in AET, update x for new y
            updatexbyslopeinv(activeEdgeTuple);
        }
        return image;
    }


    void updatexbyslopeinv(EdgeTableTuple Tup) {
        int i;

        for (i = 0; i < Tup.countEdgeBucket; i++) {
            (Tup.buckets[i]).xofymin = (Tup.buckets[i]).xofymin + (Tup.buckets[i]).slopeinverse;
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

