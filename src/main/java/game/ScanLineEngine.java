package game;

import geometry.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ScanLineEngine implements Renderer {


    private final int screenWidth;
    private final int screenHeight;
    private final Model board;
    private List<EdgeEntry>[] edgeTable;
    boolean pause = false;


    public ScanLineEngine(int screenWidth, int screenHeight, Model board) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.board = board;
        this.edgeTable = createEdgeTupleTable(screenHeight);
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

        var first = poly.v1();
        var second = poly.v2();
        var third = poly.v3();
        var tex = Resources.getTexture(poly.color());

        var texSecond = tex.origin();
        var texFirst = new Vertex(0, texSecond.y + tex.v(), 0);
        var texThird = new Vertex(texSecond.x + tex.u(), 0, 0);

        if (first.equals(second) || second.equals(third) || third.equals(first)) {
            return;
        }


        if (second.y > first.y) {
            var temp = second;
            second = first;
            first = temp;
            temp = texSecond;
            texSecond = texFirst;
            texFirst = temp;
        }
        if (third.y > first.y) {
            var temp = third;
            third = first;
            first = temp;
            temp = texThird;
            texThird = texFirst;
            texFirst = temp;
        }

        if (third.y > second.y) {
            var temp = third;
            third = second;
            second = temp;
            temp = texThird;
            texThird = texSecond;
            texSecond = temp;
        }

        int lastY = (int) Math.floor(Math.min(screenHeight - 1, first.y));
        if (first.y != second.y) {
            lastY = drawTop(first, second, third, texFirst, texSecond, texThird, poly.center().z, color);
        }

        if (third.y != second.y) {
            drawBottom(first, second, third, texFirst, texSecond, texThird, poly.center().z, color, lastY);
        }


    }

    private void drawBottom(Vertex first, Vertex second, Vertex third, Vertex texFirst, Vertex texSecond, Vertex texThird, float zIndex, int color, int startY) {
        float secondThirdSlopeInv;
        float secondThirdYintercept;
        secondThirdSlopeInv = (float) (second.x - third.x) / (float) (second.y - third.y);
        secondThirdYintercept = second.y - (float) (second.x / secondThirdSlopeInv);

        float firstThirdSlopeInv;
        float firstThirdYintercept;
        firstThirdSlopeInv = (float) ((first.x - third.x)) / (float) ((first.y - third.y));
        firstThirdYintercept = first.y - (float) (first.x / firstThirdSlopeInv);


//int y = Math.round(Math.min(screenHeight - 1, second.y))-1
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
                        0, 0, color);
                edgeTable[y].add(ee);
            } catch (RuntimeException ex) {
                System.out.println(startX + " " + endX);
                System.out.println(String.format("Poly: ,v1:%s\n v2:%s\n v3:%s", first.toString(), second.toString(), third.toString()));
                throw ex;
            }
        }
    }

    private int drawTop(Vertex first, Vertex second, Vertex third, Vertex texFirst, Vertex texSecond, Vertex texThird, float zIndex, int color) {
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
            float texX,texY;
            float dax_step,dbx_step;
            float dxStart,dyStart,dxEnd,dyEnd;
            if (second.x > first.x) {
                startX =
                        firstThirdSlopeInv == 0 ? first.x : ((y - firstThirdYintercept) * firstThirdSlopeInv);
                endX =
                        firstSecondSlopeInv == 0 ? second.x : ((y - firstSecondYintercept) * firstSecondSlopeInv);
                dax_step = firstThirdSlopeInv;
                dbx_step = firstSecondSlopeInv;
                dxStart = first.x - third.x;
                dyStart = first.y - third.y;
                dxEnd = first.x - second.x;
                dyEnd = first.y - second.y;
                //Because we are resolving to the scan line, we need to clip our geometry sometimes

            } else {
                endX =
                        firstThirdSlopeInv == 0 ? first.x : ((y - firstThirdYintercept) * firstThirdSlopeInv);
                startX =
                        firstSecondSlopeInv == 0 ? second.x : ((y - firstSecondYintercept) * firstSecondSlopeInv);
                dbx_step = firstThirdSlopeInv;
                dax_step = firstSecondSlopeInv;
                dxStart = first.x - second.x;
                dyStart = first.y - second.y;
                dxEnd = first.x - third.x;
                dyEnd = first.y - third.y;
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

                var tex = Resources.getTexture(color);
                int start = (int) Math.floor(startX);
                int end = (int) Math.ceil(endX);
                if (tex == null) { //color is a color
                    EdgeEntry ee = new EdgeEntry(start, end, zIndex, 0,
                            0, 0, color);
                    edgeTable[y].add(ee);
                } else {
                    //interpolate are add E's
                    Vertex texStart, texEnd;
                    float du1Slope, dv1Slope,du2Slope, dv2Slope;

                    du1Slope = tex.u()/dxStart;
                    dv1Slope = tex.v()/dyStart;
                    du2Slope = tex.u()/dxEnd;
                    dv2Slope = tex.v()/dyEnd;

                    texStart = new Vertex(tex.origin().x + (y - first.y) * du1Slope, tex.origin().y + (y - first.y) * dv1Slope,0);
                    texEnd = new Vertex(tex.origin().x + (y - first.y) * du2Slope, tex.origin().y + (y - first.y) * dv2Slope,0);

                    float tStepx = 1/(texEnd.x - texStart.x+1);
                    float tStepy =1/(texEnd.y - texStart.y+1);
                    float tU = texStart.x;
                    float tV = texStart.y;

                    for (int i = start; i <= end; i++) {
                        BufferedImage image = Resources.getImage(tex.imageId());
                        var texColor = image.getRGB((int)(tex.origin().x + tex.u() +tU),(int)(tex.origin().y + tex.v() + tV));
                        EdgeEntry ee = new EdgeEntry(i, i, zIndex, 0,
                                0, 0, texColor);
                        edgeTable[y].add(ee);
                        tU += tStepx;
                        tV += tStepy;
                    }
                }
                toReturn = y - 1;
            } catch (RuntimeException ex) {
            //    throw ex;
            }

        }
        return toReturn;
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
