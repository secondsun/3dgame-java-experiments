import game.BoardNew;
import geometry.Pair;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.IntStream;

public class ScanLineEngine extends Component {

    boolean pause = false;
    private int rotY, rotX = 45;

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


        var board = new BoardNew(32, 24, new int[]{0xfc49ab, 0xff7300, 0xe7ff00, 0x5fe8ff, 0x64ff00});
        System.out.println(rotX + "," + rotY);
        board.translateX(-board.getScreenWidth() / 2).translateY(-board.getScreenHeight() / 2).rotateX(rotX).rotateY(rotY).translateX(board.getScreenWidth() / 2).translateY(board.getScreenHeight() / 2);
        rotY += 2;

        board.generateEdgeList();
        BufferedImage image = board.draw();

        g.drawImage(image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_FAST), 0, 0, null);

        if (rotY > 360) {
            rotY = 0;

        }
        rotX += 1;
        if (rotX > 360) {

            rotX = 0;
        }
//        try {
//            Thread.sleep(1000/20);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        repaint();
    }





}
