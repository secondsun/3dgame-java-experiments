package dev.secondsun;

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
        private final int screenWidth = 128 ;
        private final int screenHeight = 160 ;
        private int rotY=64, rotX = 100;
        private int theta = 45;
        @Override
        public void paint(Graphics g) {
            super.paint(g);

            var board = new Stairwell(resources);
            Renderer engine = new ScanLineEngine(screenWidth, screenHeight, board, resources);

            var camera = new Camera(new Vertex(rotX,rotY,100), new Vertex(96,32,50));
            board.lookAt(camera, new Vertex2D(1.5f,1.5f), new Vertex2D(120,80));


            var tiles = board.getTriangles();
            var tree = board.getBSPTree();
            tiles = tree.order(tiles, camera);
            BufferedImage image = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
            var rgb = engine.draw(tiles);

            for (int x  =0; x < screenWidth; x++) {
                for (int y  =0; y < screenHeight; y++) {
                    image.setRGB(x,y, rgb[x + y*screenWidth]);
                }
            }

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
