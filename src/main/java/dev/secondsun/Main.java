package dev.secondsun;

import com.google.gson.Gson;
import dev.secondsun.game.Renderer;
import dev.secondsun.game.ScanLineEngine;
import dev.secondsun.geometry.Camera;
import dev.secondsun.geometry.playfield.DormRoom;
import dev.secondsun.geometry.playfield.Stairwell;
import dev.secondsun.util.Resources;
import dev.secondsun.geometry.Vertex;
import dev.secondsun.geometry.Vertex2D;

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
        private final Resources resources = new Resources();
        private final int screenWidth = 256 ;
        private final int screenHeight = 320 ;
        private int rotY=64, rotX = 100;
        private int theta = 12;
        @Override
        public void paint(Graphics g) {
            super.paint(g);

            var board = new DormRoom(resources);
            Renderer engine = new ScanLineEngine(screenWidth, screenHeight, board, resources);

            var camera = new Camera(new Vertex(rotX+48,rotY+48,50), new Vertex(48,48,0), new Vertex(0,0,1));
            board.lookAt(camera, new Vertex2D(2f,2f), new Vertex2D(100,80));




            var tiles = board.getTriangles();
            var tree = board.getBSPTree();

            tiles = tree.order(camera);
            BufferedImage image = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
            var rgb = engine.draw(tiles);

            for (int x  =0; x < screenWidth; x++) {
                for (int y  =0; y < screenHeight; y++) {
                    image.setRGB(x,y, rgb[x + y*screenWidth]);
                }
            }

            g.drawImage(image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_FAST), 0, 0, null);

            try {
                Thread.sleep(1000/20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            theta+=3;
            rotX= (int) (300*Math.cos(Math.toRadians(theta)));
            rotY= (int) (-300*Math.sin(Math.toRadians(theta)));
            repaint();
        }
    }
}
