import game.Renderer;
import game.ScanLineEngine;
import geometry.BoardNew;
import geometry.Cube;
import geometry.Model;

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
        private int rotY=272, rotX = 317;

        @Override
        public void paint(Graphics g) {
            super.paint(g);

            var board = new BoardNew(32, 24, scale, new int[]{0xfc49ab, 0xff7300, 0xe7ff00, 0x5fe8ff, 0x64ff00});
            Model cube = new Cube();
            Renderer engine = new ScanLineEngine(screenWidth, screenHeight, cube);

            System.out.println(rotX + "," + rotY);
            board.rotateX(rotX).rotateY(rotY).translateX(screenWidth / 2).translateY(screenHeight / 2);
            rotY += 1;

            var tiles = board.getTriangles();
            var verticies = board.getVerticies();

            BufferedImage image = engine.draw(tiles, verticies);

            g.drawImage(image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_FAST), 0, 0, null);

            if (rotY > 360) {
                rotY = 0;

            }
            rotX += 1;
            if (rotX > 360) {

                rotX = 0;
            }

            try {
                Thread.sleep(1000/100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            repaint();
        }
    }
}
