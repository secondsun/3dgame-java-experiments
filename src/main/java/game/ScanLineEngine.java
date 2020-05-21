package game;

import geometry.*;
import util.Maths;
import util.Resources;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ScanLineEngine implements Renderer {


    private final int screenWidth;
    private final int screenHeight;
    private final Model board;
    private List<EdgeEntry>[] edgeTable;
    boolean pause = false;
    private Camera camera;


    public ScanLineEngine(int screenWidth, int screenHeight, Model board) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.board = board;
        this.edgeTable = createEdgeTupleTable(screenHeight);
    }

    //Calculate polygons for screen drawing
    private void generateEdgeList(List<Triangle> tiles) {
        initEdgeTable();

        tiles.forEach(triangle -> {
            float yMin = Maths.min(triangle.v1.y, triangle.v2.y, triangle.v3.y);
            float yMax = Maths.max(triangle.v1.y, triangle.v2.y, triangle.v3.y);
            float xMin = Maths.min(triangle.v1.x, triangle.v2.x, triangle.v3.x);
            float xMax = Maths.max(triangle.v1.x, triangle.v2.x, triangle.v3.x);

            if (yMax < 0 || yMin > screenHeight) {
                //does not intersect with scan line
                return;
            }

            if (xMax < 0 || xMin > screenWidth) {
                //does not intersect with scan line
                return;
            }


            storeTriangleInTable(triangle);


        });

    }


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

    private void storeTriangleInTable(Triangle poly) {
        int color = poly.textureId;
        if (poly.normal().z > 0) {//skip polygons facing away
//      System.out.println("normal backwards" + poly);
            return;
        }

        var v1 = poly.v1;
        var v2 = poly.v2;
        var v3 = poly.v3;

        if (v1.equals(v2) || v2.equals(v3) || v3.equals(v1)) {
            return;
        }


        var sorted = new ArrayList<>(List.of(v1, v2, v3));

        sorted.sort((l, r) -> {
            var result = r.y - l.y;
            if (result < 0) {
                return -1;
            } else if (result > 0) {
                return 1;
            } else {
                return l.x - r.x < 0 ? -1 : 1;
            }
        });

        var first = sorted.get(0);
        var second = sorted.get(1);
        var third = sorted.get(2);


        int lastY = (int) Math.floor(Math.min(screenHeight - 1, first.y));
        if (first.y != second.y) {
            lastY = drawTop(first, second, third, poly.center().z, color, poly);
        }

        if (third.y != second.y) {
            drawBottom(first, second, third, poly.center().z, color, lastY, poly);
        }


    }

    private void drawBottom(Vertex first, Vertex second, Vertex third, float zIndex, int color, int startY, Triangle poly) {
        float secondThirdSlopeInv;
        float secondThirdYintercept;
        secondThirdSlopeInv = (float) (second.x - third.x) / (float) (second.y - third.y);
        secondThirdYintercept = second.y - (float) (second.x / secondThirdSlopeInv);

        float firstThirdSlopeInv;
        float firstThirdYintercept;
        firstThirdSlopeInv = (float) ((first.x - third.x)) / (float) ((first.y - third.y));
        firstThirdYintercept = first.y - (float) (first.x / firstThirdSlopeInv);


//int y = Math.round(Math.min(screenHeight - 1, second.y))-1
        line:
        for (int y = startY; y >= Math.max(0, third.y); y--) {

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
                EdgeEntry ee = new EdgeEntry((int) Math.floor(startX), (int) Math.ceil(endX), zIndex, 0,
                        0, 0, color, poly);
                addEdge(ee, y);
            } catch (RuntimeException ex) {
                System.out.println(startX + " " + endX);
                System.out.println(String.format("Poly: ,A:%s\n B:%s\n C:%s", first.toString(), second.toString(), third.toString()));
                throw ex;
            }
        }
    }

    private void addEdge(EdgeEntry ee, int y) {


        for (EdgeEntry entry : edgeTable[y]) {
            if (entry.endX < ee.startX) {//entry ends before this one starts, do nothing

            } else if (entry.startX > ee.endX) {//new entry starts after existing one ends, do nothing

            } else {//danger zone, there may be overlap
                if (entry.startX > ee.startX && entry.endX < ee.endX) {//we need to split the new entry
                    EdgeEntry eeLeft = new EdgeEntry(ee.startX, entry.startX, ee.z, ee.textureVectorX, ee.textureVectorY, ee.textureVectorLength, ee.textureId, ee.triangle);
                    EdgeEntry eeRight = new EdgeEntry(entry.endX, ee.endX, ee.z, ee.textureVectorX, ee.textureVectorY, ee.textureVectorLength, ee.textureId, ee.triangle);
                    addEdge(eeLeft, y);
                    addEdge(eeRight, y);
                    return;
                } else if (entry.startX > ee.startX && entry.endX >= ee.endX) {//clip ee.end to entry.start
                    ee.endX = entry.startX;
                } else if (entry.startX <= ee.startX && entry.endX < ee.endX) {
                    ee.startX = entry.endX;
                } else if (entry.startX <= ee.startX && entry.endX >= ee.endX) {//this new entry is covered, skip it
                    return;
                } else {
                    System.out.println("WTF");
                }
            }
        }

        edgeTable[y].add(ee);
    }

    private int drawTop(Vertex first, Vertex second, Vertex third, float zIndex, int color, Triangle poly) {
        int toReturn = (int) Math.floor(Math.min(screenHeight - 1, first.y));
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

                //Because we are resolving to the scan line, we need to clip our geometry sometimes


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
                EdgeEntry ee = new EdgeEntry((int) Math.floor(startX), (int) Math.ceil(endX), zIndex, 0,
                        0, 0, color, poly);
                addEdge(ee, y);

                toReturn = y - 1;
            } catch (RuntimeException ex) {
                throw ex;
            }

        }
        return toReturn;
    }

    @Override
    public BufferedImage draw(List<Triangle> tiles) {
        generateEdgeList(tiles);
        BufferedImage image = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
        int i;

        line:
        for (i = 0; i < screenHeight; i++) {
            int y = screenHeight - i - 1;
            var line = edgeTable[screenHeight - i - 1];//On the screen y is down, in the model y is up, so we have to reverse t
            line.sort((e1, e2) -> {
                return e1.startX - e2.startX;
            });

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
                    Texture text;
                    if ((text = Resources.getTexture(entry.textureId)) != null) {
                        Vertex2D uv;
                        if (text.u() > 0) {//triangle.v2 is top left
                            var c = Maths.add(Maths.add(entry.triangle.v2, Maths.subtract(entry.triangle.v3,entry.triangle.v2)),Maths.subtract(entry.triangle.v1,entry.triangle.v2));
                            Quad quad = new Quad(
                                    new Vertex2D(entry.triangle.v2.x, entry.triangle.v2.y),
                                    new Vertex2D(entry.triangle.v3.x, entry.triangle.v3.y),
                                    new Vertex2D(c.x, c.y),
                                    new Vertex2D(entry.triangle.v1.x, entry.triangle.v1.y)
                                    );
                            uv = Maths.reverseBilinear(new Vertex2D(x, y), quad);
                        } else {//triangle.v2 is bottom right
                            var a = Maths.add(Maths.add(entry.triangle.v2, Maths.subtract(entry.triangle.v3,entry.triangle.v2)),Maths.subtract(entry.triangle.v1,entry.triangle.v2));
                            Quad quad = new Quad(
                                    new Vertex2D(a.x, a.y),
                                    new Vertex2D(entry.triangle.v1.x, entry.triangle.v1.y),
                                    new Vertex2D(entry.triangle.v2.x, entry.triangle.v2.y),
                                    new Vertex2D(entry.triangle.v3.x, entry.triangle.v3.y));
                            uv = Maths.reverseBilinear(new Vertex2D(x, y), quad);
                           // uv =new Vertex2D(.6f,.5f);
                        }
                        var texture = Resources.getImage(text.imageId());
                        try {
                            image.setRGB(x, i, texture.getRGB((int) Math.abs(uv.x * text.u())%text.u(), (int) Math.abs(uv.y *text.v())%text.v()));
                        } catch (ArrayIndexOutOfBoundsException ex) {
                            //System.out.println(uv);
                            image.setRGB(x, i, Color.BLACK.getRGB());
                        }
                    } else {
                        image.setRGB(x, i, entry.textureId);
                    }
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

}
