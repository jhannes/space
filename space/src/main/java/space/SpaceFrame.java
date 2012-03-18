package space;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;

public class SpaceFrame extends JFrame {

    private final Space space;

    public SpaceFrame(Space space) {
        setBackground(Color.BLACK);
        this.space = space;
    }

    @Override
    public void paint(Graphics original) {
        if (original != null) {
            SwingDisplay display = new SwingDisplay(getWidth(), getHeight());

            if (!space.showWake) {
                display.clear();
            }
            for (PhysicalObject po : space.objects) {
                space.doPaintObject(display, po);
            }
            setTitle(space.getTitleString());
            display.drawBufferTo(original);
        }
    }


    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        new SpaceFrame(new Bounce()).run();
    }

    protected void run() throws InvocationTargetException, InterruptedException {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == 'w')
                    space.showWake = !space.showWake;
            }
        });
        setSize(800, 820);

        space.setSize(800, 820);
        space.createPhysicalObjects();
        setVisible(true);
        while (true) {
            final long start = System.currentTimeMillis();
            EventQueue.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    space.collide();
                    space.doStep();
                    space.step++;
                    paint(getGraphics());
                    setTitle(space.getTitleString());
                }
            });
            try {
                long ahead = 1000 / space.frameRate - (System.currentTimeMillis() - start);
                if (ahead > 50) {
                    Thread.sleep(ahead);
                    if(space.frameRate<25) space.frameRate++;
                } else {
                    Thread.sleep(50);
                    space.frameRate--;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
