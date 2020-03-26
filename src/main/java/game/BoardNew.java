package game;

import geometry.EdgeEntry;
import geometry.Triangle;
import geometry.Vertex;

import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

public class BoardNew {

    private static final int scale = 3;

    private static int tileSize = 8 * scale;

    private final int screenWidth = 256 * scale;
    private final int screenHeight = 192 * scale;
    private final int[] palette;
    private final int boardWidth;
    private final int boardHeight;

    private List<Triangle> tiles;
    private List<Vertex> verticies;

    private int rotY;
    private int rotX;

    private int translateY;
    private int translateX;

    private List<EdgeEntry>[] edgeTable = createEdgeTupleTable(screenHeight);

    private List<EdgeEntry>[] createEdgeTupleTable(int i) {

        List<EdgeEntry>[] edgeTable = new List[i];
        for (int x = 0; x < i; x++) {
            edgeTable[x] = new ArrayList<>();
        }
        return edgeTable;
    }


    // Scanline Function
    void initEdgeTable() {
        int i;
        for (i = 0; i < screenHeight; i++) {
            edgeTable[i] = new ArrayList<>();
        }

    }


    public BoardNew(int columns, int rows, int[] palette) {
        var random = new Random(columns);
        this.boardWidth = columns * tileSize;
        this.boardHeight = rows * tileSize;

        this.palette = palette;
        tiles = new ArrayList<>(rows * columns);

        final Map<Vertex, Vertex> vertexMap = new HashMap<>();

        for (int x = 0; x < columns; x++) {
            for (int y = 0; y < rows; y++) {


                int paletteSize = palette.length;
                int paletteColorIndex = ((y * tileSize + x) % paletteSize);
                int zOff = random.nextInt(5);
                var v1 = vertexMap.computeIfAbsent(new Vertex((x * tileSize), (y * tileSize), zOff * tileSize),
                        point -> new Vertex(point.x, point.y, point.z));
                var v2 = vertexMap.computeIfAbsent(new Vertex((x * tileSize), (y * tileSize) + tileSize, zOff * tileSize),
                        point -> new Vertex(point.x, point.y, point.z));
                var v3 = vertexMap
                        .computeIfAbsent(new Vertex((x * tileSize) + tileSize, (y * tileSize) + tileSize, zOff * tileSize),
                                point -> new Vertex(point.x, point.y, point.z));
                var v4 = vertexMap.computeIfAbsent(new Vertex((x * tileSize) + tileSize, (y * tileSize), zOff * tileSize),
                        point -> new Vertex(point.x, point.y, point.z));

                var v5 = vertexMap.computeIfAbsent(new Vertex((x * tileSize), (y * tileSize), (zOff+1) * tileSize),
                        point -> new Vertex(point.x, point.y, point.z));
                var v6 = vertexMap.computeIfAbsent(new Vertex((x * tileSize), (y * tileSize) + tileSize, (zOff+1) * tileSize),
                        point -> new Vertex(point.x, point.y, point.z));
                var v7 = vertexMap
                        .computeIfAbsent(new Vertex((x * tileSize) + tileSize, (y * tileSize) + tileSize, (zOff+1) * tileSize),
                                point -> new Vertex(point.x, point.y, point.z));
                var v8 = vertexMap.computeIfAbsent(new Vertex((x * tileSize) + tileSize, (y * tileSize), (zOff+1) * tileSize),
                        point -> new Vertex(point.x, point.y, point.z));



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

                //tiles.add(new Quad(v1, v2, v3, v4, palette[paletteColorIndex]));

            }
        }
        verticies = new ArrayList<>(vertexMap.values());

    }

    //Calculate polygons for screen drawing
    public void generateEdgeList() {
        initEdgeTable();

        tiles.forEach(triangle -> {
            float yMin = Maths.min(triangle.v1().y, triangle.v2().y, triangle.v3().y);
            float yMax = Maths.max(triangle.v1().y, triangle.v2().y, triangle.v3().y);
            float xMin = Maths.min(triangle.v1().x, triangle.v2().x, triangle.v3().x);
            float xMax = Maths.max(triangle.v1().x, triangle.v2().x, triangle.v3().x);

            if (yMax < 0 || yMin > screenHeight) {
                //does not intersect with scan line
                //System.out.println("Ejecting" + triangle);
                return;
            }

            if (xMax < 0 || xMin > screenWidth) {
                //does not intersect with scan line
                //System.out.println("Ejecting" + triangle);
                return;
            }


            storeTriangleInTable(triangle);


        });

    }

    private void storeTriangleInTable(Triangle poly) {
        int color = poly.color();
        if (poly.normal().z > 0) {//skip polygons facing away
//      System.out.println("normal backwards" + poly);
            return;
        }

        var v1 = poly.v1();
        var v2 = poly.v2();
        var v3 = poly.v3();

        if (v1.equals(v2) || v2.equals(v3) || v3.equals(v1)) {
            return;
        }


        var sorted = new ArrayList<>(List.of(v1,v2,v3));

        sorted.sort((l,r) -> {var result = r.y-l.y;
            if (result < 0 ) {
                return -1;
            } else if (result > 0) {
                return 1;
            } else {
                return l.x-r.x<0?-1:1;
            }
        });

        var first = sorted.get(0);
        var second = sorted.get(1);
        var third = sorted.get(2);





        if (first.y != second.y) {
            drawTop(first, second, third, poly.center().z, color);
        }

        if (third.y != second.y) {
            drawBottom(first, second, third, poly.center().z, color);
        }


    }

    private void drawBottom(Vertex first, Vertex second, Vertex third,float zIndex, int color) {
        float secondThirdSlopeInv;
        float secondThirdYintercept;
        secondThirdSlopeInv = (float) (second.x - third.x) / (float) (second.y - third.y);
        secondThirdYintercept = second.y - (float) (second.x / secondThirdSlopeInv);


        float firstSecondSlopeInv;
        float firstSecondYintercept = 0;
        firstSecondSlopeInv = (float) (first.x - second.x) / (float) (first.y - second.y);
        firstSecondYintercept = first.y - (float) (first.x / firstSecondSlopeInv);

        float firstThirdSlopeInv;
        float firstThirdYintercept;
        firstThirdSlopeInv = (float) ((first.x - third.x)) / (float) ((first.y - third.y));
        firstThirdYintercept = first.y - (float) (first.x / firstThirdSlopeInv);



        for (int y = Math.round(Math.min(screenHeight - 1, second.y)); y >= Math.max(0, third.y); y--) {
            float startX;
            float endX;
            if (second.x > first.x) {
                startX =
                    firstThirdSlopeInv == 0 ? first.x : ((y - firstThirdYintercept) * firstThirdSlopeInv);
                endX =
                    secondThirdSlopeInv == 0 ? third.x : ((y - secondThirdYintercept) * secondThirdSlopeInv);
            } else {
                endX =
                    firstThirdSlopeInv == 0 ? first.x : ((y - firstThirdYintercept) * firstThirdSlopeInv);
                startX =
                    secondThirdSlopeInv == 0 ? third.x : ((y - secondThirdYintercept) * secondThirdSlopeInv);
            }

            if (Math.abs(startX - endX) < 0.001) {
                endX = startX;
            }

            try {
                EdgeEntry ee = new EdgeEntry(Math.round(startX), Math.round(endX), zIndex, 0,
                    0, 0, color);
                edgeTable[y].add(ee);
            } catch (RuntimeException ex) {
                System.out.println(startX + " " + endX);
                System.out.println(String.format("Poly: ,v1:%s\n v2:%s\n v3:%s", first.toString(), second.toString(), third.toString()));
                throw ex;
            }
        }
    }

    private void drawTop(Vertex first, Vertex second, Vertex third,float zIndex, int color) {

        float firstSecondSlopeInv;
        float firstSecondYintercept = 0;
        firstSecondSlopeInv = (float) (first.x - second.x) / (float) (first.y - second.y);
        firstSecondYintercept = first.y - (float) (first.x / firstSecondSlopeInv);


        float firstThirdSlopeInv;
        float firstThirdYintercept;
        firstThirdSlopeInv = (float) ((first.x - third.x)) / (float) ((first.y - third.y));
        firstThirdYintercept = first.y - (float) (first.x / firstThirdSlopeInv);

        for (int y = (int) Math.floor(Math.min(screenHeight - 1, first.y)); y >= Math.max(0, second.y); y--) {
            float startX, endX;
            if (second.x > first.x) {
                startX =
                    firstThirdSlopeInv == 0 ? first.x : ((y - firstThirdYintercept) * firstThirdSlopeInv);
                endX =
                    firstSecondSlopeInv == 0 ? second.x : ((y - firstSecondYintercept) * firstSecondSlopeInv);
            } else {
                endX =
                    firstThirdSlopeInv == 0 ? first.x : ((y - firstThirdYintercept) * firstThirdSlopeInv);
                startX =
                    firstSecondSlopeInv == 0 ? second.x : ((y - firstSecondYintercept) * firstSecondSlopeInv);
            }

            float diff = startX - endX;

            if (diff > 0) {//Sometimes it is possible for very extreme triangles to round annoyingly.
                var temp = startX;
                startX = endX;
                endX = temp;
            }

            if (Math.abs(diff) < 0.05) {
                endX = startX;
            }

            try {
                EdgeEntry ee = new EdgeEntry((int)Math.floor(startX), (int)Math.ceil(endX), zIndex, 0,
                    0, 0, color);
                edgeTable[y].add(ee);
            } catch (RuntimeException ex) {
                throw ex;
            }

        }
    }

    /**
     * This checks if it can handle the cases where there is a flat edge in the polygon
     *
     * @param v1    trangle point
     * @param v2    trangle point
     * @param v3    trangle point
     * @param color draw color
     * @return true if handled, false otherwise
     */
    private boolean handleEqual(Vertex v1, Vertex v2, Vertex v3, int color) {
        float invLeftSlope, leftYintercept, invRightSlope, rightYintercept;
        var center = new Vertex((v1.x + v2.x + v3.x) / 3, (v1.y + v2.y + v3.y) / 3, (v1.z + v2.z + v3.z) / 3);
        if (v1.y == v2.y) {

            if (v1.y > v3.y) {
                // handle \/

                invLeftSlope = (v1.x - v3.x) / (v1.y - v3.y);
                leftYintercept = invLeftSlope == 0 ? 0 : v1.y - v1.x / invLeftSlope;

                invRightSlope = (v2.x - v3.x) / (v2.y - v3.y);
                rightYintercept = invRightSlope == 0 ? 0 : v2.y - v2.x / invRightSlope;

                for (int y = Math.round(Math.min(screenHeight - 1, v1.y)); y >= Math.max(0, v3.y); y--) {
                    float startX = invLeftSlope == 0 ? v1.x : ((y - leftYintercept) * invLeftSlope);
                    float endX = invRightSlope == 0 ? v2.x : ((y - rightYintercept) * invRightSlope);

                    if (Math.abs(startX - endX) < 0.001) {
                      startX = endX;
                    }

                    EdgeEntry ee = new EdgeEntry(Math.round(startX), Math.round(endX), center.z, 0, 0, 0, color);
                    edgeTable[y].add(ee);
                }
            } else if (v3.y > v1.y) {
                // handle /\
                invLeftSlope = (v2.x - v3.x) / (v2.y - v3.y);
                leftYintercept = invLeftSlope == 0 ? 0 : v2.y - v2.x / invLeftSlope;

                invRightSlope = (v1.x - v3.x) / (v1.y - v3.y);
                rightYintercept = invRightSlope == 0 ? 0 : v1.y - v1.x / invRightSlope;


                for (int y = (int) Math.floor(Math.min(screenHeight - 1, v3.y)); y >= Math.max(0, v1.y); y--) {
                    float startX = invLeftSlope == 0 ? v3.x : ((y - leftYintercept) * invLeftSlope);
                    float endX = invRightSlope == 0 ? v1.x : ((y - rightYintercept) * invRightSlope);

                    if (Math.abs(startX - endX) < 0.001) {
                        startX = endX;
                    }
                    EdgeEntry ee = new EdgeEntry((int) Math.round(startX), (int) Math.round(endX), center.z, 0, 0, 0, color);
                    edgeTable[y].add(ee);
                }
            } else {
                return true; //a line
            }
            return true;
        } else if (v2.y == v3.y) {
            if (v3.y > v1.y) {
                //handle \/
                invLeftSlope = (v2.x - v1.x) / (v2.y - v1.y);
                leftYintercept = invLeftSlope == 0 ? 0 : v1.y - (v1.x / invLeftSlope);

                invRightSlope = (v1.x - v3.x) / (v1.y - v3.y);
                rightYintercept = invRightSlope == 0 ? 0 : v3.y - (v3.x / invRightSlope);

                for (int y = (int) Math.floor(Math.min(screenHeight - 1, v3.y)); y >= Math.ceil(Math.max(0, v1.y)); y--) {

                    float startX = invLeftSlope == 0 ? v2.x : ((y - leftYintercept) * invLeftSlope);
                    float endX = invRightSlope == 0 ? v3.x : ((y - rightYintercept) * invRightSlope);

                    if (Math.abs(startX - endX) < 0.001) {
                        endX = startX;
                    }

                    try {
                        EdgeEntry ee = new EdgeEntry((int) Math.round(startX), (int) Math.round(endX), center.z, 0, 0, 0, color);
                        edgeTable[y].add(ee);
                    } catch (RuntimeException ex) {
                        System.out.println(startX + " " + endX);
                        System.out.println(String.format("Poly: ,v1:%s\n v2:%s\n v3:%s", v1.toString(), v2.toString(), v3.toString()));
                        throw ex;
                    }
                }

            } else {
                // handle /\
                invLeftSlope = (v1.x - v3.x) / (v1.y - v3.y);
                leftYintercept = invLeftSlope == 0 ? 0 : v1.y - (v1.x / invLeftSlope);

                invRightSlope = (v2.x - v1.x) / (v2.y - v1.y);
                rightYintercept = invRightSlope == 0 ? 0 : v2.y - (v2.x / invRightSlope);

                for (int y = (int) Math.floor(Math.min(screenHeight - 1, v1.y)); y >= Math.max(0, v3.y); y--) {
                    float startX = invLeftSlope == 0 ? v1.x : ((y - leftYintercept) * invLeftSlope);
                    float endX = invRightSlope == 0 ? v2.x : ((y - rightYintercept) * invRightSlope);


                    if (Math.abs(startX - endX) < 0.001) {
                        endX = startX;
                    }


                    if (endX - startX > 1) {
                        EdgeEntry ee = new EdgeEntry((int) startX, (int) endX, center.z, 0, 0, 0, color);
                        edgeTable[y].add(ee);
                    }
                }
            }
            return true;//TODO Handle \/ and /\
        } else if (v1.y == v3.y) {
            if (v2.y > v1.y) {
                //handle /\
                invLeftSlope = (v2.x - v1.x) / (v2.y - v1.y);
                leftYintercept = invLeftSlope == 0 ? 0 : v1.y - (v1.x) / invLeftSlope;

                invRightSlope = (v2.x - v3.x) / (v2.y - v3.y);
                rightYintercept = invRightSlope == 0 ? 0 : v3.y - (v3.x) / invRightSlope;

                for (int y = (int) Math.floor(Math.min(screenHeight - 1, v2.y)); y >= Math.max(0, v3.y); y--) {

                    float startX = invLeftSlope == 0 ? v2.x : ((y - leftYintercept) * invLeftSlope);
                    float endX = invRightSlope == 0 ? v3.x : ((y - rightYintercept) * invRightSlope);


                    if (Math.abs(startX - endX) < 0.001) {
                        endX = startX;
                    }


                    try {
                        EdgeEntry ee = new EdgeEntry((int) Math.round(startX), (int) Math.round(endX), center.z, 0, 0, 0, color);
                        edgeTable[y].add(ee);
                    } catch (RuntimeException ex) {
                        System.out.println(startX + " " + endX);
                        System.out.println(String.format("Poly: ,v1:%s\n v2:%s\n v3:%s", v1.toString(), v2.toString(), v3.toString()));
                        throw ex;
                    }
                }

            } else {
                // handle /\
                invLeftSlope = (v2.x - v3.x) / (v2.y - v3.y);
                leftYintercept = invLeftSlope == 0 ? 0 : v2.y - v2.x / invLeftSlope;

                invRightSlope = (v2.x - v1.x) / (v2.y - v1.y);
                rightYintercept = invRightSlope == 0 ? 0 : v2.y - v2.x / invRightSlope;

                for (int y = Math.round(Math.min(screenHeight - 1, v3.y)); y >= Math.max(0, v2.y); y--) {
                    float startX = invLeftSlope == 0 ? v2.x : ((y - leftYintercept) * invLeftSlope);
                    float endX = invRightSlope == 0 ? v2.x : ((y - rightYintercept) * invRightSlope);
                    try {
                        if (endX > 0 && startX < screenWidth) {
                            EdgeEntry ee = new EdgeEntry((int) startX, Math.round(endX), center.z, 0, 0, 0, color);
                            edgeTable[y].add(ee);
                        }
                    } catch (RuntimeException ex) {
                        System.out.println(v2.equals(v3) + "");
                        System.out.println(String.format("Poly: ,v1:%s\n v2:%s\n v3:%s", v1.toString(), v2.toString(), v3.toString()));
                        throw ex;
                    }
                }
            }
            return true;
        } else {

            return false;

        }

    }


    public BufferedImage draw() {

        BufferedImage image = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
        int i, j, x1, ymax1, x2, ymax2, FillFlag = 0, coordCount;

        line:
        for (i = 0; i < screenHeight; i++) {
            var line = edgeTable[screenHeight - i - 1];//On the screen y is down, in the model y is up, so we have to reverse t
            line.sort((e1, e2) -> {
                return e1.startX - e2.startX;
            });

            var finalI = i;
//      line.forEach(edge -> {
//        var g = image.getGraphics();
//        g.setColor(new Color(edge.textureId));
//        g.drawLine(edge.startX, finalI, edge.endX, finalI);
//      });

            if (!line.isEmpty()) {

                for (int x = 0; x < screenWidth; x++) {
                    int lineIndex = 0;

                    if (line.size() == 0) {
                        continue;
                    }

                    EdgeEntry entry = line.get(0);
                    lineIndex++;
                    if (x < entry.startX) {
                        continue;
                    }
                    while (lineIndex < line.size() && x >= line.get(lineIndex).startX) {//finds "top" entry
                        var test = line.get(lineIndex);
                        if (test.z > entry.z) {
                            entry = test;
                        }
                        lineIndex++;
                    }


                    image.setRGB(x, i, entry.textureId);

                    lineIndex = 0;
                    while (lineIndex < line.size() && x >= line.get(lineIndex).startX) {//finds "top" entry
                        if (line.get(lineIndex).endX < x) {
                            line.remove(lineIndex);
                        } else {
                            lineIndex++;
                        }
                    }
                }
            }
        }
        return image;
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

    public int getScreenHeight() {
        return screenHeight;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public BoardNew translateZ(int i) {
        verticies.forEach(tile -> tile.translateZ(i));
        return this;
    }
}

