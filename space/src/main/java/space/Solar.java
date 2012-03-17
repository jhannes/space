package space;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Solar extends Space implements MouseMotionListener {

    private static Point lastDrag = null;

    @Override
    public void mouseDragged(final MouseEvent e) {
        if (!IS_BOUNCING_BALLS) {
            if (lastDrag == null) {
                lastDrag = e.getPoint();
            }
            PhysicalObject.centrex = PhysicalObject.centrex - ((e.getX() - lastDrag.x) * PhysicalObject.scale);
            PhysicalObject.centrey = PhysicalObject.centrey - ((e.getY() - lastDrag.y) * PhysicalObject.scale);
            lastDrag = e.getPoint();
            getGraphics().clearRect(0, 0, getWidth(), getHeight());
        }
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
                if (!false) {
                    if (Math.sqrt(Math.pow(one.x - other.x, 2) + Math.pow(one.y - other.y, 2)) < 5e9) {
                        one.absorb(other);
                        remove.add(other);
                    }
                } else {
                    double distance = Math.sqrt(Math.pow(one.x - other.x, 2) + Math.pow(one.y - other.y, 2));
                    double collsionDistance = one.radius + other.radius;
                    if (distance < collsionDistance) {
                        one.hitBy(other);
                    }
                }
            }
            // Wall collision reverses speed in that direction
            if (false) {
                if (one.x - one.radius < 0) {
                    one.vx = -one.vx;
                }
                if (one.x + one.radius > 800) {
                    one.vx = -one.vx;
                }
                if (one.y - one.radius < 0) {
                    one.vy = -one.vy;
                }
                if (one.y + one.radius > 800 && !IS_BREAKOUT) {
                    one.vy = -one.vy;
                } else if (one.y - one.radius > 800) {
                    remove.add(one);
                }
            }
        }
        objects.removeAll(remove);
    }

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        new Solar().run(true);
    }

}
