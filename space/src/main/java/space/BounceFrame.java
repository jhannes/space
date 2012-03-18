package space;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;

public class BounceFrame extends JFrame {

    @Override
    public void paint(Graphics original) {
        if (original != null) {
            BufferedImage buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = buffer.createGraphics();

            if (!space.showWake) {
                graphics.clearRect(0, 0, getWidth(), getHeight());
            }
            for (PhysicalObject po : space.objects) {
                space.doPaintObject(graphics, po);
                String string = "Objects:" + space.objects.size() + " scale:" + PhysicalObject.scale + " steps:" +
                    space.step + " frame rate: " + space.frameRate;
                setTitle(string);
            }
            original.drawImage(buffer, 0, 0, getWidth(), getHeight(), null);
        }
    }


    private final Bounce space;

    public BounceFrame(Bounce bounce) {
        this.space = bounce;
    }

    public void run() throws InvocationTargetException, InterruptedException {
        space.addMouseWheelListener(space);
        space.addMouseMotionListener(space);
        space.addKeyListener(space);
        space.setSize(800, 820);
        setSize(800, 820);

        space.createPhysicalObjects();
        setVisible(true);
        while (true) {
            final long start = System.currentTimeMillis();
            EventQueue.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    space.collide();
                    space.doStep();
                    Space.step++;
                    paint(getGraphics());
                }
            });
            try {
                long ahead = 1000 / Space.frameRate - (System.currentTimeMillis() - start);
                if (ahead > 50) {
                    Thread.sleep(ahead);
                    if(Space.frameRate<25) Space.frameRate++;
                } else {
                    Thread.sleep(50);
                    Space.frameRate--;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        new BounceFrame(new Bounce()).run();
    }

}
