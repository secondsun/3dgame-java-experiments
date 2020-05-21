import game.Renderer;
import game.ScanLineEngine;
import geometry.Camera;
import geometry.playfield.DormRoom;
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
        private final int screenWidth = 256*4;//256 * scale;
        private final int screenHeight = 192*4;//192 * scale;
        private int rotY=32, rotX = 100;
        private int theta = 45;
        @Override
        public void paint(Graphics g) {
            super.paint(g);

            var board = new DormRoom();
            Renderer engine = new ScanLineEngine(screenWidth, screenHeight, board);

            var camera = new Camera(new Vertex(rotX,rotY,100), new Vertex(64,64,0));
            board.lookAt(camera, new Vertex2D(8f,8f), new Vertex2D(120*4,80*4));


            var tiles = board.getTriangles();
            var tree = board.getBSPTree();
            tiles = tree.order(tiles, camera);
            BufferedImage image = engine.draw(tiles);

            g.drawImage(image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_FAST), 0, 0, null);

            try {
                Thread.sleep(1000/30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            theta++;
            rotX= (int) (300*Math.cos(Math.toRadians(theta)) + 100);
            rotY= (int) (-300*Math.sin(Math.toRadians(theta)) + 32);
            repaint();
        }
    }
}
