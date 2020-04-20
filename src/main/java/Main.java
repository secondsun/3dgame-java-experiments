import game.Renderer;
import game.ScanLineEngine;
import geometry.Camera;
import geometry.MonasteryPlayfield;
import geometry.Vertex;
import geometry.Vertex2D;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Main {

    public static void main(String[] args) {
        var frame = new JFrame("3D Engine");
        frame.setSize(800,600);
        frame.add(new Main.Screen());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }



    public static class Screen extends Component {
        private final int scale = 3;
        private final int screenWidth = 256 * scale;
        private final int screenHeight = 192 * scale;
        private int rotY=0, rotX = 0;

        @Override
        public void paint(Graphics g) {
            super.paint(g);

            var board = new MonasteryPlayfield();
            Renderer engine = new ScanLineEngine(screenWidth, screenHeight, board);

            var camera = new Camera(new Vertex(rotX,-rotY,100), new Vertex(0,0,-1));
            board.lookAt(camera, new Vertex2D(2,2), new Vertex2D(0,0));


            var tiles = board.getTriangles();
            BufferedImage image = engine.draw(tiles);

            g.drawImage(image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_FAST), 0, 0, null);

            try {
                Thread.sleep(1000/100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            rotY++;
            rotX++;
            rotX++;
            if (rotX > 300) {
                rotX = 0;
            }
            if (rotY > 300) {
                rotY = 1;
            }

            repaint();
        }
    }
}
