package game;

import geometry.*;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ScanLineEngine implements Renderer {


    private final int screenWidth = 256;
    private final int screenHeight = 192;
    private final Model board;

    boolean pause = false;


    public ScanLineEngine(Model board) {
        this.board = board;
    }

    //Calculate polygons for screen drawing
    private void generateEdgeList(List<Triangle> tiles, List<Vertex> verticies) {
        initEdgeTable();

        tiles.forEach(triangle -> {
            float yMin = Maths.min(triangle.v1().y, triangle.v2().y, triangle.v3().y);
            float yMax = Maths.max(triangle.v1().y, triangle.v2().y, triangle.v3().y);
            float xMin = Maths.min(triangle.v1().x, triangle.v2().x, triangle.v3().x);
            float xMax = Maths.max(triangle.v1().x, triangle.v2().x, triangle.v3().x);

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

    private void drawBottom(Vertex first, Vertex second, Vertex third, float zIndex, int color) {
        float secondThirdSlopeInv;
        float secondThirdYintercept;
        secondThirdSlopeInv = (float) (second.x - third.x) / (float) (second.y - third.y);
        secondThirdYintercept = second.y - (float) (second.x / secondThirdSlopeInv);

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
                EdgeEntry ee = new EdgeEntry((int)Math.floor(startX), (int)Math.ceil(endX), zIndex, 0,
                        0, 0, color);
                edgeTable[y].add(ee);
            } catch (RuntimeException ex) {
                throw ex;
            }

        }
    }

    @Override
    public BufferedImage draw(List<Triangle> tiles, List<Vertex> verticies) {
        generateEdgeList(tiles, verticies);
        BufferedImage image = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
        int i;

        line:
        for (i = 0; i < screenHeight; i++) {
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

}
