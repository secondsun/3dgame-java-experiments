import game.Renderer;
import game.ScanLineEngine;
import geometry.*;
import importer.ImportMain;
import util.BSPTree;
import util.BoundedCube;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        var frame = new JFrame("3D Engine");
        frame.setSize(800, 600);
        frame.add(new Main.Screen());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }


    public static class Screen extends Component {

        private final int screenWidth = 256;//256 * scale;
        private final int screenHeight = 160;//192 * scale;
        private int rotY = 0, rotX = 25, rotZ=200;
        private int theta = 1;

        public Screen() throws IOException {
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);

            Model board = null;
            List<Triangle> tris;

            try {
                (tris = ImportMain.calc("MAP056.48")).forEach(tri -> tri.scale(.5f));//.translateX(-117).rotateY(rotY).translateX(117)
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            board = new Model() {

                @Override
                public List<Triangle> getTriangles() {
                    return (tris);

                }

                @Override
                public BSPTree getBSPTree() {
                    return new BSPTree(new BSPTree.Node(new BoundedCube(tris)));
                }
            };

            Renderer engine = new ScanLineEngine(screenWidth, screenHeight, board);

            var camera = new Camera(new Vertex(rotX, 117, rotZ), new Vertex(35, 0, 0));
            board.lookAt(camera, new Vertex2D(1f, 1f), new Vertex2D(0, 0));
            System.out.println(" " + board.getBSPTree().getRoot().bounds);


            var tiles = board.getTriangles();
            var tree = board.getBSPTree();
            tiles = tree.order(tiles, camera);
            BufferedImage image = engine.draw(tiles);

            g.drawImage(image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_FAST), 0, 0, null);

            try {
                Thread.sleep(1000 / 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println(theta+ "@" + rotX + "," + rotZ);
            theta++;
            rotX = (int) (140 * Math.cos(Math.toRadians(theta)));
            rotZ = (int) (234 * Math.sin(Math.toRadians(theta)));
            rotY++;
            repaint();
        }
    }
}
