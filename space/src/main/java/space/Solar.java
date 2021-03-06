package space;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class Solar extends Space  {

    private static final double ASTRONOMICAL_UNIT = 149597870.7e3;

    private static final double EARTH_WEIGHT = 5.9736e24;

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
                if (distance < 5e9) {
                    one.absorb(other);
                    remove.add(other);
                }
            }
        }
        objects.removeAll(remove);
    }

    @Override
    protected void doPaintObject(Display display, PhysicalObject po) {
        double radius = (po.mass >= Solar.EARTH_WEIGHT * 10000) ? 3.5 : 1;
        double centerX = (po.x - centrex) / scale + width / 2;
        double centerY = (po.y - centrey) / scale + height / 2;

        display.fillCircle(centerX, centerY, radius, weightToColor(po.mass));
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
    protected void createPhysicalObjects() {
        setStepSize(3600 * 24 * 7);

        double outerLimit = ASTRONOMICAL_UNIT * 20;

        for (int i = 0; i < nrOfObjects; i++) {
            double angle = randSquare() * 2 * Math.PI;
            double radius = (0.1 + 0.9 * Math.sqrt(randSquare())) * outerLimit;
            double weightKilos = 1e3 * Solar.EARTH_WEIGHT * (Math.pow(0.00001 + 0.99999 * randSquare(), 12));
            double x = radius * Math.sin(angle);
            double y = radius * Math.cos(angle);
            double speedRandom = Math.sqrt(1 / radius) * 2978000*1500 * (0.4 + 0.6 * randSquare());

            double vx = speedRandom * Math.sin(angle - Math.PI / 2);
            double vy = speedRandom * Math.cos(angle - Math.PI / 2);
            add(weightKilos, x, y, vx, vy, 1);
        }

        scale = outerLimit / width;
        add(Solar.EARTH_WEIGHT * 20000, 0, 0, 0, 0, 1);
    }

    protected static double randSquare() {
        double random = Math.random();
        return random * random;
    }

}
