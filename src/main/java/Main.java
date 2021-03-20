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
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        var frame = new JFrame("3D Engine");
        frame.setSize(800, 600);
        frame.add(new Main.Screen());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }


    public static class Screen extends Component {
        private List<Triangle> tris = ImportMain.calc("MAP056.48").stream().map(tri -> tri.scale(.025f)).collect(Collectors.toList());


        private final int screenWidth = 256 * 4;//256 * scale;
        private final int screenHeight = 192 * 4;//192 * scale;
        private int rotY = 120, rotX = 120;
        private int theta = 45;

        public Screen() throws IOException {
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);

            Model board = null;
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

            var camera = new Camera(new Vertex(rotX, rotY, 200), new Vertex(0, 0, 0));
            board.lookAt(camera, new Vertex2D(100f, 100f), new Vertex2D(0,0));


            var tiles = board.getTriangles();
            var tree = board.getBSPTree();
            tiles = tree.order(tiles, camera);
            BufferedImage image = engine.draw(tiles);

            g.drawImage(image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_FAST), 0, 0, null);

            try {
                Thread.sleep(1000 / 30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //theta++;
            //rotX = (int) (300 * Math.cos(Math.toRadians(theta)) + 100);
            //rotY = (int) (-300 * Math.sin(Math.toRadians(theta)) + 32);
            repaint();
        }
    }
}
