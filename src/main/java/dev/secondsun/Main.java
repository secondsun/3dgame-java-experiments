package dev.secondsun;

import com.google.gson.Gson;
import dev.secondsun.game.Renderer;
import dev.secondsun.game.ScanLineEngine;
import dev.secondsun.geometry.Camera;
import dev.secondsun.geometry.Model;
import dev.secondsun.geometry.playfield.*;
import dev.secondsun.util.Resources;
import dev.secondsun.geometry.Vertex;
import dev.secondsun.geometry.Vertex2D;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

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
        private int camX=64, camY = 64, camZ = 64;
        private double theta = 0;

        @Override
        public void paint(Graphics g) {
            super.paint(g);

            CornerPyramids board = new CornerPyramids(16, 5);
            Renderer engine = new ScanLineEngine(screenWidth, screenHeight, board, resources);

            var lookAt = new Vertex(0,0,1);
            var position = new Vertex(0,0,32);
            var forward = position.subtract(lookAt).normalize();
            var yAxis = new Vertex(0f,1f,0f);
            var right = yAxis.cross(forward).normalize();
            var up = forward.cross(right).normalize();

            var camera = new Camera(position, lookAt, up);

            var position2 = new Vertex((float) 0, (float) 0, (float) (0));
            var lookAt2 = new Vertex((float) (16f*cos(theta)), (float) 16, (float) (16f*sin(theta)));
            var forward2 = position2.subtract(lookAt2).normalize();
            var yAxis2 = new Vertex(0f,1f,0f);
            var right2 = yAxis2.cross(forward2).normalize();
            var up2 = forward2.cross(right2).normalize();

            var camera2 = new Camera(position2, lookAt2, up2);

            var foV =new Vertex2D(.5f,.5f);
            var worldCenter = new Vertex2D(screenWidth/2,screenHeight/2);

            try {
                float[][] frustum = camera.frustum(Math.toRadians(45));
                //var tiles = board.tree.traverse(camera,frustum);
                var tiles = camera2.frustumModel(Math.toRadians(20));
                var lookAtM = camera.lookAt();
                float[][] fovMatrix = {{foV.x,0,0, worldCenter.x},
                        {0, foV.y,0, worldCenter.y},
                        {0,0,-128,-1},
                        {0,0,0,1}};
                tiles.forEach((tile -> tile.transform(lookAtM)));
                tiles.forEach(tile -> tile.transform(fovMatrix));

//                System.out.printf("Tiles : %d \n", tiles.size());
                System.out.printf("Theta : %.2f \n", theta);
                BufferedImage image = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
                var rgb = engine.draw(tiles);

                for (int x = 0; x < screenWidth; x++) {
                    for (int y = 0; y < screenHeight; y++) {
                        image.setRGB(x, y, rgb[x + y * screenWidth]);
                    }
                }

                g.drawImage(image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_FAST), 0, 0, null);

                try {
                    Thread.sleep(1000 / 45);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (Exception ignore) {
                throw new RuntimeException(ignore);
            }
           // theta+=.05;
//            camX= (int) (-(64)*Math.sin(Math.toRadians(theta)));
//            //camY= camY-theta;
//            camZ= (int) (-(64)*Math.cos(Math.toRadians(theta)));
            repaint();
        }
    }
}
