package dev.secondsun;

import com.google.gson.Gson;
import dev.secondsun.game.Renderer;
import dev.secondsun.game.ScanLineEngine;
import dev.secondsun.geometry.Camera;
import dev.secondsun.geometry.playfield.*;
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
        private final int screenWidth = 512 ;
        private final int screenHeight = 384 ;
        private int camX=128, camY = 128, camZ = 128;
        private int theta = 1;

        @Override
        public void paint(Graphics g) {
            super.paint(g);

            RandomPyramids  board = new RandomPyramids(15, 0);
            Renderer engine = new ScanLineEngine(screenWidth, screenHeight, board, resources);

            var position = new Vertex(camX,camY,camZ);
            var lookAt = new Vertex(0,0,0);
            var forward = position.subtract(lookAt).normalize();
            var yAxis = new Vertex(0f,1f,0f);
            var right = yAxis.cross(forward).normalize();
            var up = forward.cross(right).normalize();

            var camera = new Camera(position, lookAt, up);

            board.lookAt(camera, new Vertex2D(3f,3f), new Vertex2D(screenWidth/2,screenHeight/2));
            var tiles = board.tree.traverse(camera.getFrom());

            BufferedImage image = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
            var rgb = engine.draw(tiles);

            for (int x  =0; x < screenWidth; x++) {
                for (int y  =0; y < screenHeight; y++) {
                    image.setRGB(x,y, rgb[x + y*screenWidth]);
                }
            }

            g.drawImage(image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_FAST), 0, 0, null);

            try {
                Thread.sleep(1000/120);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            theta+=1;
            camX= (int) (-256*Math.sin(Math.toRadians(theta)));
            //camY= (int) (256*Math.cos(Math.toRadians(theta)));
            camZ= (int) (-256*Math.cos(Math.toRadians(theta)));
            repaint();
        }
    }
}
