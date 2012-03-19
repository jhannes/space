package space;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Bounce extends Space {
    protected static final boolean IS_BREAKOUT = false; // Opens bottom, only active if IS_BOUNCING_BALLS is true

    @Override
    protected void collide() {
        List<PhysicalObject> remove = new ArrayList<PhysicalObject>();
        for (PhysicalObject one : objects) {
            if (remove.contains(one))
                continue;
            for (PhysicalObject other : objects) {
                if (one == other || remove.contains(other))
                    continue;
                double distance = Math.sqrt(Math.pow(one.x - other.x, 2) + Math.pow(one.y - other.y, 2));
                double collsionDistance = one.radius + other.radius;
                if (distance < collsionDistance) {
                    one.hitBy(other, -seconds / 10);
                }
            }
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
        objects.removeAll(remove);
    }

    @Override
    protected void doPaintObject(Display display, PhysicalObject po) {
        double centerX = (po.x - centrex) + width / 2;
        double centerY = (po.y - centrey) + height / 2;
        display.fillCircle(centerX, centerY, po.radius, Color.WHITE);
    }

    @Override
    protected void createPhysicalObjects() {
        nrOfObjects = 50;
        setStepSize(1); // One second per iteration
        for (int i = 0; i < nrOfObjects; i++) {
            // radius,weight in [1,20]
            double radiusAndWeight = 1 + 19 * Math.random();
            //x,y in [max radius, width or height - max radius]
            add(radiusAndWeight, 20 + 760 * Math.random(), 20 + 760 * Math.random(), 3 - 6 * Math.random(), 3 - 6 * Math.random(), radiusAndWeight);
        }
        scale = 1;
        centrex = 400;
        centrey = 390; //Must compensate for title bar
    }

}
