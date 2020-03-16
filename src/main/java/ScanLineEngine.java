import game.Board;
import game.BoardNew;
import geometry.Quad;
import geometry.Vertex;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Comparator;

public class ScanLineEngine extends Component {

    private BoardNew board = new BoardNew(32, 24, new int[]{0xfc49ab, 0xff7300, 0xe7ff00, 0x5fe8ff, 0x64ff00});
    int rotY = 0;
    int rotX = 30;

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int i, j, x1, ymax1, x2, ymax2, FillFlag = 0, coordCount;
        board = new BoardNew(32, 24, new int[]{0xfc49ab, 0xff7300, 0xe7ff00, 0x5fe8ff, 0x64ff00});
        board.rotateY(rotY).rotateX(rotX).translateX(120).translateY(96);
        rotY += 7;
        //BufferedImage image = new BufferedImage(192, 256, BufferedImage.TYPE_INT_RGB);
        board.generateEdgeList();
        BufferedImage image = board.draw();

        g.drawImage(image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_FAST),0,0,null);

        try {
            Thread.sleep(1000/25);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (rotY > 360) {
            rotY = 0;
        }
        repaint();

    }
}
