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
    private static final double ASTRONOMICAL_UNIT = 149597870.7e3;
    protected static boolean IS_BREAKOUT = false; // Opens bottom, only active if IS_BOUNCING_BALLS is true

    protected static boolean IS_BOUNCING_BALLS = false;

    private static final long serialVersionUID = 1532817796535372081L;

    protected static final double G = 6.67428e-11; // m3/kgs2
    protected static List<PhysicalObject> objects = new ArrayList<PhysicalObject>();
    private static boolean showWake = false;
    private static int step = 0;
    private static int nrOfObjects = 75;
    private static int frameRate = 25;

    public Space() {
        setBackground(Color.BLACK);
        PhysicalObject.frame = this;
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

    protected void run(boolean isSolarSystem) throws InterruptedException, InvocationTargetException {
        final Space space = this;
        space.addMouseWheelListener(space);
        space.addMouseMotionListener(space);
        space.addKeyListener(space);
        space.setSize(800, 820);

        if (isSolarSystem) {
            space.setStepSize(3600 * 24 * 7);

            double outerLimit = ASTRONOMICAL_UNIT * 20;

            for (int i = 0; i < nrOfObjects; i++) {
                double angle = randSquare() * 2 * Math.PI;
                double radius = (0.1 + 0.9 * Math.sqrt(randSquare())) * outerLimit;
                double weightKilos = 1e3 * PhysicalObject.EARTH_WEIGHT * (Math.pow(0.00001 + 0.99999 * randSquare(), 12));
                double x = radius * Math.sin(angle);
                double y = radius * Math.cos(angle);
                double speedRandom = Math.sqrt(1 / radius) * 2978000*1500 * (0.4 + 0.6 * randSquare());

                double vx = speedRandom * Math.sin(angle - Math.PI / 2);
                double vy = speedRandom * Math.cos(angle - Math.PI / 2);
                add(weightKilos, x, y, vx, vy, 1);
            }

            PhysicalObject.scale = outerLimit / space.getWidth();

            add(PhysicalObject.EARTH_WEIGHT * 20000, 0, 0, 0, 0, 1);
        } else {
            nrOfObjects = 50;
            space.setStepSize(1); // One second per iteration
            for (int i = 0; i < nrOfObjects; i++) {
                // radius,weight in [1,20]
                double radiusAndWeight = 1 + 19 * Math.random();
                //x,y in [max radius, width or height - max radius]
                Space.add(radiusAndWeight, 20 + 760 * Math.random(), 20 + 760 * Math.random(), 3 - 6 * Math.random(), 3 - 6 * Math.random(), radiusAndWeight);
            }
            PhysicalObject.scale = 1;
            PhysicalObject.centrex = 400;
            PhysicalObject.centrey = 390; //Must compensate for title bar
        }
        space.setVisible(true);
        while (true) {
            final long start = System.currentTimeMillis();
            EventQueue.invokeAndWait(new Runnable() {
                @Override
                public void run() {
                    space.collide();
                    space.step();
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

    protected abstract void collide();

    private static double randSquare() {
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
    public void mouseWheelMoved(final MouseWheelEvent e) {
        if (!IS_BOUNCING_BALLS) {
            PhysicalObject.scale = PhysicalObject.scale + PhysicalObject.scale * (Math.min(9, e.getWheelRotation())) / 10 + 0.0001;
            getGraphics().clearRect(0, 0, getWidth(), getHeight());
        }
    }

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
