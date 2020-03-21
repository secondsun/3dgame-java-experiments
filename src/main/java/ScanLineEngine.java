import game.Board;
import game.BoardNew;
import geometry.Quad;
import geometry.Vertex;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Comparator;

public class ScanLineEngine extends Component {

    private BoardNew board = new BoardNew(32, 24, new int[]{0xfc49ab, 0xff7300, 0xe7ff00, 0x5fe8ff, 0x64ff00});
    int rotY = 27;
    int rotX = 26;

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int i, j, x1, ymax1, x2, ymax2, FillFlag = 0, coordCount;
        board = new BoardNew(32, 24, new int[]{0xfc49ab, 0xff7300, 0xe7ff00});
        System.out.println(rotX + "," + rotY);
        board.translateX(-board.getScreenWidth()/2).translateY(-board.getScreenHeight()/2).rotateY(rotY).rotateX(rotX).translateX(board.getScreenWidth()/2).translateY(board.getScreenHeight()/2);
        rotX += 3;

        //BufferedImage image = new BufferedImage(192, 256, BufferedImage.TYPE_INT_RGB);
        board.generateEdgeList();
        BufferedImage image = board.draw();

        g.drawImage(image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_FAST),0,0,null);

        try {
            Thread.sleep(1000/100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (rotX > 90) {
            rotX = -90;
            rotY += 2;
            if (rotY > 90) {
                rotY = -90;
            }
        }


        //repaint();

    }
}
