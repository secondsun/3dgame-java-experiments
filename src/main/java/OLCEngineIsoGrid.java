import game.Board;
import geometry.Constants;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;

import static geometry.Constants.rotX128Array;

public class OLCEngineIsoGrid extends Component {

    private BufferedImage screenImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
    private Board board = new Board(192, 256, new int[]{0xfc49ab, 0xff7300, 0xe7ff00, 0x5fe8ff, 0x64ff00});//.rotateY(0).rotateX(30);
    private int rotX = 0;
    private int rotY = 0;

    public OLCEngineIsoGrid() {
    }

    public void draw(int x, int y, int colorrgb) {
        if (x >= 0 && x < screenImage.getWidth() && y >= 0 && y < screenImage.getHeight()) {
            screenImage.setRGB(x, y, colorrgb);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        board = board.rotateY(0).rotateX(45);

        screenImage = new BufferedImage(256, 192, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < 192; y++) {
            for (int x = 0; x < 256; x++) {
                int color = board.draw(x, y, 0);
                draw(x, y, color);
            }
        }
        var displayImage = screenImage.getScaledInstance(getWidth(), getHeight(), BufferedImage.SCALE_FAST);
        g.drawImage(displayImage, 0, 0, null);
        rotX++;
        rotY += 7;
        if (rotY >= 360) {
            rotY = 0;
        }
//
        try {
            Thread.sleep(40);
        } catch (InterruptedException e) {
        }
        repaint();

    }

}
