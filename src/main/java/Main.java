import game.Renderer;
import game.ScanLineEngine;
import geometry.MonasteryPlayfield;

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
        private int rotY=272, rotX = 0;

        @Override
        public void paint(Graphics g) {
            super.paint(g);

            var board = new MonasteryPlayfield();
            Renderer engine = new ScanLineEngine(screenWidth, screenHeight, board);

            System.out.println(rotX + "," + rotY);
            board.scale(scale).translateX(-screenWidth/2).translateY(-screenHeight/2).rotateZ(rotX).rotateX(-45).translateX(screenWidth/2).translateY(screenHeight/2);
            rotY += 1;

            var tiles = board.getTriangles();

            BufferedImage image = engine.draw(tiles);

            g.drawImage(image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_FAST), 0, 0, null);

            if (rotY > 360) {
                rotY = 0;

            }
            rotX += 1;
            if (rotX > 360) {

                rotX = 0;
            }

            try {
                Thread.sleep(1000/200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            repaint();
        }
    }
}
