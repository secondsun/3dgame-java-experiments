import game.BoardNew;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class ScanLineEngine extends Component  {

    private BoardNew board = new BoardNew(16, 12, new int[]{0xfc49ab, 0xff7300, 0xe7ff00, 0x5fe8ff, 0x64ff00});
    int rotY = 60;
    int rotX = 26;
    boolean pause = false;

    public ScanLineEngine() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                pause = !pause;
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        int i, j, x1, ymax1, x2, ymax2, FillFlag = 0, coordCount;
        board = new BoardNew(32, 24, new int[]{0xfc49ab, 0xff7300, 0xe7ff00});
        System.out.println(rotX + "," + rotY);
        board.translateX(-board.getScreenWidth()/2).translateY(-board.getScreenHeight()/2).rotateX(rotX).rotateY(rotY).translateX(board.getScreenWidth()/2).translateY(board.getScreenHeight()/2);
        rotY += 2;

        //BufferedImage image = new BufferedImage(192, 256, BufferedImage.TYPE_INT_RGB);
        board.generateEdgeList();
        BufferedImage image = board.draw();

        g.drawImage(image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_FAST),0,0,null);

        try {
            Thread.sleep(1000/100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        if (rotY > 90) {
            rotY = -90;
            rotX += 2;
            if (rotX > 90) {
                rotX = -90;
            }

        }

        repaint();

    }
}
