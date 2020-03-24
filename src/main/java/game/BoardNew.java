package game;

import geometry.EdgeEntry;
import geometry.Quad;
import geometry.Triangle;
import geometry.Vertex;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardNew {

  private static final int scale = 2;

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
    this.boardWidth = columns * tileSize;
    this.boardHeight = rows * tileSize;

    this.palette = palette;
    tiles = new ArrayList<>(rows * columns);

    final Map<Vertex, Vertex> vertexMap = new HashMap<>();

    for (int x = 0; x < columns; x++) {
      for (int y = 0; y < rows; y++) {

        var v1 = vertexMap.computeIfAbsent(new Vertex((x * tileSize), (y * tileSize),0),
            point -> new Vertex(point.x, point.y, 0));
        var v2 = vertexMap.computeIfAbsent(new Vertex((x * tileSize), (y * tileSize) + tileSize,0),
            point -> new Vertex(point.x, point.y, 0));
        var v3 = vertexMap
            .computeIfAbsent(new Vertex((x * tileSize) + tileSize, (y * tileSize) + tileSize,0),
                point -> new Vertex(point.x, point.y, 0));
        var v4 = vertexMap.computeIfAbsent(new Vertex((x * tileSize) + tileSize, (y * tileSize),0),
            point -> new Vertex(point.x, point.y, 0));

        var v5 = vertexMap.computeIfAbsent(new Vertex((x * tileSize), (y * tileSize),tileSize),
            point -> new Vertex(point.x, point.y, point.z));
        var v6 = vertexMap.computeIfAbsent(new Vertex((x * tileSize), (y * tileSize) + tileSize,tileSize),
            point -> new Vertex(point.x, point.y, point.z));
        var v7 = vertexMap
            .computeIfAbsent(new Vertex((x * tileSize) + tileSize, (y * tileSize) + tileSize,tileSize),
                point -> new Vertex(point.x, point.y, point.z));
        var v8 = vertexMap.computeIfAbsent(new Vertex((x * tileSize) + tileSize, (y * tileSize),tileSize),
            point -> new Vertex(point.x, point.y, point.z));


        int paletteSize = palette.length;
        int paletteColorIndex = ((y * tileSize + x) % paletteSize);



        var cube = new Triangle[] {
            new Triangle(v1, v2, v3, Color.PINK.getRGB()),//SOUTH
            new Triangle(v1, v3, v4, Color.PINK.getRGB()),//SOUTH
            new Triangle(v4, v3, v7, Color.DARK_GRAY.getRGB()),//EAST
            new Triangle(v4, v7, v6, Color.DARK_GRAY.getRGB()),//EAST
            new Triangle(v8, v7, v6, Color.YELLOW.getRGB()),//NORTH
            new Triangle(v8, v6, v5, Color.YELLOW.getRGB()),//NORTH
            new Triangle(v5, v6, v2, Color.DARK_GRAY.getRGB()),//WEST
            new Triangle(v5, v2, v1, Color.DARK_GRAY.getRGB()),//WEST
            new Triangle(v2, v3, v7, Color.BLUE.getRGB()),//TOP
            new Triangle(v2, v7, v3, Color.BLUE.getRGB()),//TOP
            new Triangle(v8, v5, v1, Color.WHITE.getRGB()),//BOTTOM
            new Triangle(v8, v1, v4, Color.WHITE.getRGB()),//BOTTOM

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
    if (poly.normal().x * poly.v1().x+ poly.normal().y * poly.v1().y+ poly.normal().z* poly.v1().z < 0) {//skip polygons facing away
      //System.out.println("normal backwards" + poly);
      return;
    }

    var v1 = poly.v1();
    var v2 = poly.v2();
    var v3 = poly.v3();

    Vertex first, second, third;

    if (handleEqual(v1, v2, v3, color)) {
      return;
    } else {
      if (v1.y > v2.y && v1.y > v3.y) {
        first = v1;
        if (v2.y > v3.y) {
          second = v2;
          third = v3;
        } else {
          second = v3;
          third = v2;
        }
      } else if (v2.y > v1.y && v2.y > v3.y) {
        first = v2;
        if (v1.y > v3.y) {
          second = v1;
          third = v3;
        } else {
          second = v3;
          third = v1;
        }
      } else if (v3.y > v1.y && v3.y > v2.y) {
        first = v3;
        if (v1.y > v2.y) {
          second = v1;
          third = v2;
        } else {
          second = v2;
          third = v1;
        }
      } else {
        throw new RuntimeException("WTF " + v1 + v2 + v3);
      }
    }

    float secondThirdSlopeInv;
    float secondThirdYintercept;
    if (second.x != third.x) {
      secondThirdSlopeInv = (float) (second.x - third.x) / (float) (second.y - third.y);
      secondThirdYintercept = second.y - (float) (second.x / secondThirdSlopeInv);
    } else {
      secondThirdSlopeInv = 0;
      secondThirdYintercept = 0;
    }

    float firstSecondSlopeInv;
    float firstSecondYintercept = 0;
    if (first.x != second.x) {
      firstSecondSlopeInv = (float) (first.x - second.x) / (float) (first.y - second.y);
      firstSecondYintercept = first.y - (float) (first.x / firstSecondSlopeInv);
    } else {
      firstSecondSlopeInv = 0;
    }

    float firstThirdSlopeInv;
    float firstThirdYintercept;
    if (third.x != first.x) {
      firstThirdSlopeInv = (float) ((first.x - third.x)) / (float) ((first.y - third.y));
      firstThirdYintercept = first.y - (float) (first.x / firstThirdSlopeInv);
    } else {
      firstThirdSlopeInv = 0;
      firstThirdYintercept = 0;
    }

    for (int y = Math.round(Math.min(screenHeight - 1, first.y)); y >= Math.max(0, second.y); y--) {

      float startX = Math.round(
          firstSecondSlopeInv == 0 ? first.x : ((y - firstSecondYintercept) * firstSecondSlopeInv));
      float endX = Math.round(
          firstThirdSlopeInv == 0 ? third.x : ((y - firstThirdYintercept) * firstThirdSlopeInv));
      if (startX > endX) {
        var temp = endX;
        endX = startX;
        startX = temp;
      }
      EdgeEntry ee = new EdgeEntry((int) Math.max(startX, 0), (int) Math.min(screenWidth, endX), poly.center().z,0,
          0, 0, color);
      edgeTable[y].add(ee);

    }

    for (int y = Math.round(Math.min(screenHeight - 1, second.y)); y >= Math.max(0, third.y); y--) {

      float endX = Math.round(
          firstThirdSlopeInv == 0 ? first.x : ((y - firstThirdYintercept) * firstThirdSlopeInv));
      float startX = Math.round(
          secondThirdSlopeInv == 0 ? third.x : ((y - secondThirdYintercept) * secondThirdSlopeInv));

      if (startX > endX) {
        var temp = endX;
        endX = startX;
        startX = temp;
      }

      EdgeEntry ee = new EdgeEntry((int) Math.max(startX, 0), (int) Math.min(screenWidth, endX), poly.center().z, 0,
          0, 0, color);
      edgeTable[y].add(ee);

    }


  }

  /**
   * This checks if it can handle the cases where there is a flat edge in the polygon
   *
   * @param v1 trangle point
   * @param v2 trangle point
   * @param v3 trangle point
   * @param color draw color
   * @return true if handled, false otherwise
   */
  private boolean handleEqual(Vertex v1, Vertex v2, Vertex v3, int color) {
    float invLeftSlope, leftYintercept, invRightSlope, rightYintercept;
    var center = new Vertex((v1.x+v2.x+v3.x)/3,(v1.y+v2.y+v3.y)/3,(v1.z+v2.z+v3.z)/3);
    if (v1.y == v2.y) {

      if (v1.y > v3.y) {
        // handle \/

        invLeftSlope = (v1.x - v3.x) / (v1.y - v3.y);
        leftYintercept = invLeftSlope== 0 ?0:v1.y - v1.x / invLeftSlope;

        invRightSlope = (v2.x - v3.x) / (v2.y - v3.y);
        rightYintercept = invRightSlope==0?0:v2.y - v2.x / invRightSlope;

        for (int y = Math.round(Math.min(screenHeight - 1, v1.y)); y >= Math.max(0, v3.y); y--) {
          float startX = invLeftSlope == 0 ? v1.x : ((y - leftYintercept) * invLeftSlope);
          float endX = invRightSlope == 0 ? v2.x : ((y - rightYintercept) * invRightSlope);
          EdgeEntry ee = new EdgeEntry((int) startX, (int) endX,center.z, 0, 0, 0, color);
          edgeTable[y].add(ee);
        }
      } else if (v3.y > v1.y) {
        // handle /\
        invRightSlope = (v1.x - v3.x) / (v1.y - v3.y);
        rightYintercept = (v1.y - v1.x) / invRightSlope;

        invLeftSlope = (v2.x - v3.x) / (v2.y - v3.y);
        leftYintercept = v2.y - v2.x / invLeftSlope;

        for (int y = Math.round(Math.min(screenHeight - 1, v3.y)); y >= Math.max(0, v1.y); y--) {
          float startX = invLeftSlope == 0 ? v3.x : ((y - leftYintercept) * invLeftSlope);
          float endX = invRightSlope == 0 ? v1.x : ((y - rightYintercept) * invRightSlope);
          EdgeEntry ee = new EdgeEntry((int) startX, (int) endX,center.z, 0, 0, 0, color);
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
        leftYintercept = v1.y - (v1.x / invLeftSlope);

        invRightSlope = (v1.x - v3.x) / (v1.y - v3.y);
        rightYintercept = v3.y - (v3.x / invRightSlope);

        for (int y = Math.round(Math.min(screenHeight - 1, v3.y)); y >= Math.max(0, v1.y); y--) {
          float startX = invLeftSlope == 0 ? v1.x : ((y - leftYintercept) * invLeftSlope);
          float endX = invRightSlope == 0 ? v3.x : ((y - rightYintercept) * invRightSlope);
          EdgeEntry ee = new EdgeEntry((int) startX, (int) endX, center.z,0, 0, 0, color);
          edgeTable[y].add(ee);
        }

      } else {
        // handle /\
        invLeftSlope = (v1.x - v3.x) / (v1.y - v3.y);
        leftYintercept = invLeftSlope == 0 ? 0: v1.y - (v1.x / invLeftSlope);

        invRightSlope = (v2.x - v1.x) / (v2.y - v1.y);
        rightYintercept = invRightSlope == 0 ? 0 : v2.y - (v2.x / invRightSlope);

        for (int y = Math.round(Math.min(screenHeight - 1, v1.y)); y >= Math.max(0, v3.y); y--) {
          float startX = invLeftSlope == 0 ? v1.x : ((y - leftYintercept) * invLeftSlope);
          float endX = invRightSlope == 0 ? v2.x : ((y - rightYintercept) * invRightSlope);
          EdgeEntry ee = new EdgeEntry((int) startX, (int) endX, center.z,0, 0, 0, color);
          edgeTable[y].add(ee);
        }
      }
      return true;//TODO Handle \/ and /\
    } else if (v1.y == v3.y) {
      if (v2.y > v1.y) {
        //handle \/
        invLeftSlope = (v2.x - v1.x) / (v2.y - v1.y);
        leftYintercept = invLeftSlope == 0 ? 0: v1.y - (v1.x) / invLeftSlope;

        invRightSlope = (v2.x - v3.x) / (v2.y - v3.y);
        rightYintercept = invRightSlope == 0 ? 0 : v3.y - (v3.x) / invRightSlope;

        for (int y = Math.round(Math.min(screenHeight - 1, v2.y)); y >= Math.max(0, v3.y); y--) {
          float startX = invLeftSlope == 0 ? v2.x : ((y - leftYintercept) * invLeftSlope);
          float endX = invRightSlope == 0 ? v3.x : ((y - rightYintercept) * invRightSlope);
          EdgeEntry ee = new EdgeEntry((int) startX, (int) endX, center.z,0, 0, 0, color);
          edgeTable[y].add(ee);
        }

      } else {
        // handle /\
        invLeftSlope = (v2.x - v3.x) / (v2.y - v3.y);
        leftYintercept =invLeftSlope == 0 ? 0:  v2.y - v2.x / invLeftSlope;

        invRightSlope = (v2.x - v1.x) / (v2.y - v1.y);
        rightYintercept = invRightSlope == 0 ? 0 : v2.y - v2.x / invRightSlope;

        for (int y = Math.round(Math.min(screenHeight - 1, v1.y)); y >= Math.max(0, v3.y); y--) {
          float startX = invLeftSlope == 0 ? v2.x : ((y - leftYintercept) * invLeftSlope);
          float endX = invRightSlope == 0 ? v2.x : ((y - rightYintercept) * invRightSlope);
          EdgeEntry ee = new EdgeEntry((int) startX, (int) endX, center.z,0, 0, 0, color);
          edgeTable[y].add(ee);
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
            continue ;
          }
          while (lineIndex < line.size() && x >= line.get(lineIndex).startX) {//finds "top" entry
            var test = line.get(lineIndex);
            if (test.z > entry.z) {
              entry = test;
            }
            lineIndex++;
          }


          image.setRGB(x,i,entry.textureId);

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

