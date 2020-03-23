import geometry.Triangle;
import geometry.Vertex;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

public class OLCEngine extends Component {

    private BufferedImage screenImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);

    private int rotX = 30;
    private int rotY = 0;

    public OLCEngine() {
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                screenImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
                repaint();
            }
        });
    }

    public void draw(int x, int y, int colorrgb) {
        if (x >= 0 && x < screenImage.getWidth() && y >= 0 && y < screenImage.getHeight()) {
            screenImage.setRGB(x,getHeight()- y, colorrgb);
        }
    }

    public void drawLine(int x1, int y1, int x2, int y2, int color) {
        int x, y, dx, dy, dx1, dy1, px, py, xe, ye, i;
        dx = x2 - x1;
        dy = y2 - y1;
        dx1 = Math.abs(dx);
        dy1 = Math.abs(dy);
        px = 2 * dy1 - dx1;
        py = 2 * dx1 - dy1;
        if (dy1 <= dx1) {
            if (dx >= 0) {
                x = x1;
                y = y1;
                xe = x2;
            } else {
                x = x2;
                y = y2;
                xe = x1;
            }

            draw(x, y, color);

            for (i = 0; x < xe; i++) {
                x = x + 1;
                if (px < 0)
                    px = px + 2 * dy1;
                else {
                    if ((dx < 0 && dy < 0) || (dx > 0 && dy > 0)) y = y + 1;
                    else y = y - 1;
                    px = px + 2 * (dy1 - dx1);
                }
                draw(x, y, color);
            }
        } else {
            if (dy >= 0) {
                x = x1;
                y = y1;
                ye = y2;
            } else {
                x = x2;
                y = y2;
                ye = y1;
            }

            draw(x, y, color);

            for (i = 0; y < ye; i++) {
                y = y + 1;
                if (py <= 0)
                    py = py + 2 * dx1;
                else {
                    if ((dx < 0 && dy < 0) || (dx > 0 && dy > 0)) x = x + 1;
                    else x = x - 1;
                    py = py + 2 * (dx1 - dy1);
                }
                draw(x, y, color);
            }
        }
    }

    void drawTriangle(float x1, float y1, float x2, float y2, float x3, float y3, int color) {
        drawTriangle(Math.round(x1), Math.round( y1), Math.round( x2), Math.round( y2), Math.round( x3), Math.round( y3),  color);
    }

    void drawTriangle(int x1, int y1, int x2, int y2, int x3, int y3, int color) {
        drawLine(x1, y1, x2, y2, color);
        drawLine(x2, y2, x3, y3, color);
        drawLine(x3, y3, x1, y1, color);
    }

    @Override
    public void paint(Graphics g) {

        super.paint(g);

        var v1 = new Vertex(-1, -1, -1);
        var v2 = new Vertex(-1, 1, -1);
        var v3 = new Vertex(1, 1, -1);
        var v4 = new Vertex(1, -1, -1);
        var v5 = new Vertex(-1, -1, 1);
        var v6 = new Vertex(-1, 1, 1);
        var v7 = new Vertex(1, 1, 1);
        var v8 = new Vertex(1, -1, 1);


        //Right
        var t5 = new Triangle(v4, v3, v7, Color.PINK.getRGB());
        var t6 = new Triangle(v4, v7, v8, Color.PINK.getRGB());

        //left
        var t9 = new Triangle(v5, v2, v1, Color.RED.getRGB());
        var t10 = new Triangle(v5, v6, v2, Color.RED.getRGB());
        //bottom
        var t7 = new Triangle(v8, v5, v1, Color.GREEN.getRGB());
        var t8 = new Triangle(v8, v1, v4, Color.GREEN.getRGB());
        //back
        var t11 = new Triangle(v8, v7, v6, Color.BLUE.getRGB());
        var t12 = new Triangle(v8, v6, v5, Color.BLUE.getRGB());

        //Front
        var t1 = new Triangle(v1, v2, v3, Color.WHITE.getRGB());
        var t2 = new Triangle(v1, v3, v4, Color.WHITE.getRGB());

        //Top
        var t3 = new Triangle(v2, v6, v7, Color.YELLOW.getRGB());
        var t4 = new Triangle(v2, v7, v3, Color.YELLOW.getRGB());




        var cube = new Triangle[]{t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12};

        for (Triangle face : cube) {
            face = face.scale(50).rotateY(rotY).rotateX(rotX).translateX(getWidth()/2).translateY(getHeight()/2);
            if (direction(face) > 0)
                drawTriangle(face);
        }


        g.drawImage(screenImage, 0, 0, null);
        screenImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        rotY += 7;
        try {
            Thread.sleep(40);
        } catch (InterruptedException e) {
        }
        repaint();

    }

    private int direction(Triangle face) {
        var normal = face.normal();
        var v1 = face.v1();
        return Math.round(v1.x * normal.x + v1.y*normal.y + v1.z * normal.z);
    }

    private int randomColor() {
        int r = (int)(Math.random() * 255);
        int g = (int)(Math.random() * 255);
        int b = (int)(Math.random() * 255);
        return r<<16|g<<8|b;
    }

    private void drawTriangle(Triangle scale) {
        drawTriangle(scale.v1().x, scale.v1().y,
                scale.v2().x, scale.v2().y,
                scale.v3().x, scale.v3().y, scale.color());
    }
}
