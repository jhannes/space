package space;

import java.awt.Color;
import java.awt.Graphics2D;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Bounce extends Space {
    protected static final boolean IS_BREAKOUT = false; // Opens bottom, only active if IS_BOUNCING_BALLS is true

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        new Bounce().run();
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
                double distance = Math.sqrt(Math.pow(one.x - other.x, 2) + Math.pow(one.y - other.y, 2));
                double collsionDistance = one.radius + other.radius;
                if (distance < collsionDistance) {
                    one.hitBy(other);
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
        double centerX = (po.x - PhysicalObject.centrex) + getSize().width / 2;
        double centerY = (po.y - PhysicalObject.centrey) + getSize().height / 2;
        new SwingDisplay(graphics).fillCircle(centerX, centerY, po.radius, Color.WHITE);
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
        PhysicalObject.scale = 1;
        PhysicalObject.centrex = 400;
        PhysicalObject.centrey = 390; //Must compensate for title bar
    }

}
