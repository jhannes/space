package space;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public abstract class Space extends JFrame implements MouseWheelListener,
        MouseMotionListener, KeyListener {
    protected static final double ASTRONOMICAL_UNIT = 149597870.7e3;
    protected static boolean IS_BREAKOUT = false; // Opens bottom, only active if IS_BOUNCING_BALLS is true

    private static final long serialVersionUID = 1532817796535372081L;

    protected static final double G = 6.67428e-11; // m3/kgs2
    protected static List<PhysicalObject> objects = new ArrayList<PhysicalObject>();
    static boolean showWake = false;
    static int step = 0;
    protected static int nrOfObjects = 75;
    static int frameRate = 25;

    public Space() {
        setBackground(Color.BLACK);
    }

    @Override
    public void paint(Graphics original) {
        if (original != null) {
            BufferedImage buffer = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = buffer.createGraphics();

            if (!showWake) {
                graphics.clearRect(0, 0, getWidth(), getHeight());
            }
            for (PhysicalObject po : objects) {
                doPaintObject(graphics, po);
                String string = "Objects:" + objects.size() + " scale:" + PhysicalObject.scale + " steps:" + step + " frame rate: " + frameRate;
                setTitle(string);
            }
            original.drawImage(buffer, 0, 0, getWidth(), getHeight(), null);
        }
    }

    protected abstract void doPaintObject(Graphics2D graphics, PhysicalObject po);

    protected void run() throws InterruptedException, InvocationTargetException {
        addMouseWheelListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        setSize(800, 820);

        createPhysicalObjects();
        setVisible(true);
        animate();
    }

    protected void animate() throws InterruptedException, InvocationTargetException {
        while (true) {
            final long start = System.currentTimeMillis();
            EventQueue.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    collide();
                    step();
                }
            });
            try {
                long ahead = 1000 / frameRate - (System.currentTimeMillis() - start);
                if (ahead > 50) {
                    Thread.sleep(ahead);
                    if(frameRate<25) frameRate++;
                } else {
                    Thread.sleep(50);
                    frameRate--;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    protected abstract void createPhysicalObjects();

    protected abstract void collide();

    protected static double randSquare() {
        double random = Math.random();
        return random * random;
    }

    public void setStepSize(double seconds) {
        PhysicalObject.seconds = seconds;
    }

    public static PhysicalObject add(double weightKilos, double x, double y,
                                     double vx, double vy, double radius) {
        PhysicalObject physicalObject = new PhysicalObject(weightKilos, x, y,
                vx, vy, radius);
        objects.add(physicalObject);
        return physicalObject;
    }

    public void step() {
        doStep();
        step++;
        paint(getGraphics());
    }

    protected abstract void doStep();

    @Override
    public abstract void mouseWheelMoved(final MouseWheelEvent e);

    @Override
    public void mouseDragged(final MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }


    @Override
    public void keyPressed(KeyEvent e) {
    }


    @Override
    public void keyReleased(KeyEvent e) {
    }


    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 'w')
            showWake = !showWake;
    }

}
