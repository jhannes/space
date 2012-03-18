package space;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Solar extends Space implements MouseWheelListener,
    MouseMotionListener, KeyListener {

    private static Point lastDrag = null;

    @Override
    public void mouseDragged(final MouseEvent e) {
        if (lastDrag == null) {
            lastDrag = e.getPoint();
        }
        PhysicalObject.centrex = PhysicalObject.centrex - ((e.getX() - lastDrag.x) * PhysicalObject.scale);
        PhysicalObject.centrey = PhysicalObject.centrey - ((e.getY() - lastDrag.y) * PhysicalObject.scale);
        lastDrag = e.getPoint();
        getGraphics().clearRect(0, 0, getWidth(), getHeight());
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        lastDrag = null;
    }


    @Override
    protected void collide() {
        List<PhysicalObject> remove = new ArrayList<PhysicalObject>();
        for (PhysicalObject one : objects) {
            if (remove.contains(one))
                continue;
            for (PhysicalObject other : objects) {
                if (one == other || remove.contains(other))
                    continue;
                if (Math.sqrt(Math.pow(one.x - other.x, 2) + Math.pow(one.y - other.y, 2)) < 5e9) {
                    one.absorb(other);
                    remove.add(other);
                }
            }
        }
        objects.removeAll(remove);
    }

    @Override
    protected void doStep() {
        for (PhysicalObject aff : objects) {
            double fx = 0;
            double fy = 0;
            for (PhysicalObject oth : objects) {
                if (aff == oth)
                    continue;
                double[] d = new double[]{aff.x - oth.x, aff.y - oth.y};
                double r2 = Math.pow(d[0], 2) + Math.pow(d[1], 2);
                double f = G * aff.mass * oth.mass / r2;
                double sqrtOfR2 = Math.sqrt(r2);
                fx += f * d[0] / sqrtOfR2;
                fy += f * d[1] / sqrtOfR2;
            }
            double ax = fx / aff.mass;
            double ay = fy / aff.mass;
            aff.x = aff.x - ax * Math.pow(PhysicalObject.seconds, 2) / 2 + aff.vx * PhysicalObject.seconds;
            aff.y = aff.y - ay * Math.pow(PhysicalObject.seconds, 2) / 2 + aff.vy * PhysicalObject.seconds;
            aff.vx = aff.vx - ax * PhysicalObject.seconds;
            aff.vy = aff.vy - ay * PhysicalObject.seconds;
        }
    }

    @Override
    protected void doPaintObject(Graphics2D graphics, PhysicalObject po, Display display) {
        int diameter = po.mass >= PhysicalObject.EARTH_WEIGHT * 10000 ? 7 : 2;
        double scale = PhysicalObject.scale;
        graphics.setColor(weightToColor(po.mass));
        double x = ((po.x - PhysicalObject.centrex) / scale + getSize().width / 2)-diameter/2;
        double y = ((po.y - PhysicalObject.centrey) / scale + getSize().height / 2)-diameter/2;
        graphics.fillOval((int)x, (int)y, diameter, diameter);
    }

    public static Color weightToColor(double weight) {
        if (weight < 1e10) return Color.GREEN;
        if (weight < 1e12) return Color.CYAN;
        if (weight < 1e14) return Color.MAGENTA;
        if (weight < 1e16) return Color.BLUE;
        if (weight < 1e18) return Color.GRAY;
        if (weight < 1e20) return Color.RED;
        if (weight < 1e22) return Color.ORANGE;
        if (weight < 1e25) return Color.PINK;
        if (weight < 1e28) return Color.YELLOW;
        return Color.WHITE;
    }

    @Override
    public void mouseWheelMoved(final MouseWheelEvent e) {
        PhysicalObject.scale = PhysicalObject.scale + PhysicalObject.scale * (Math.min(9, e.getWheelRotation())) / 10 + 0.0001;
        getGraphics().clearRect(0, 0, getWidth(), getHeight());
    }

    @Override
    protected void createPhysicalObjects() {
        setStepSize(3600 * 24 * 7);

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

        PhysicalObject.scale = outerLimit / getWidth();

        add(PhysicalObject.EARTH_WEIGHT * 20000, 0, 0, 0, 0, 1);
    }

    @Override
    protected void run() throws InterruptedException, InvocationTargetException {
        addMouseWheelListener(this);
        addMouseMotionListener(this);
        addKeyListener(this);
        super.run();
    }

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        new Solar().run();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyChar() == 'w')
            showWake = !showWake;
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}
