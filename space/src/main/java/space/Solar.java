package space;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Solar extends Space implements MouseMotionListener {

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
    protected void doPaintObject(Graphics2D graphics, PhysicalObject po) {
        po.paintPhysicalObject(graphics, true);
    }

    @Override
    public void mouseWheelMoved(final MouseWheelEvent e) {
        PhysicalObject.scale = PhysicalObject.scale + PhysicalObject.scale * (Math.min(9, e.getWheelRotation())) / 10 + 0.0001;
        getGraphics().clearRect(0, 0, getWidth(), getHeight());
    }

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        new Solar().run(true);
    }

}
