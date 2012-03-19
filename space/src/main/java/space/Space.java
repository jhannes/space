package space;

import java.util.ArrayList;
import java.util.List;

public abstract class Space {

    protected static final double G = 6.67428e-11; // m3/kgs2
    protected List<PhysicalObject> objects = new ArrayList<PhysicalObject>();
    boolean showWake = false;
    private int step = 0;
    protected int nrOfObjects = 75;
    int frameRate = 25;

    protected int width;

    protected int height;

    double centrex = 0.0;

    double centrey = 0.0;

    double scale = 10;
    protected double seconds = 1;

    protected String getTitleString() {
        return "Objects:" + objects.size() + " scale:" + scale + " steps:" + step + " frame rate: " + frameRate;
    }

    protected abstract void doPaintObject(Display display, PhysicalObject po);

    protected abstract void createPhysicalObjects();

    protected abstract void collide();

    public void setStepSize(double seconds) {
        this.seconds = seconds;
    }

    public PhysicalObject add(double weightKilos, double x, double y,
                                     double vx, double vy, double radius) {
        PhysicalObject physicalObject = new PhysicalObject(weightKilos, x, y,
                vx, vy, radius);
        objects.add(physicalObject);
        return physicalObject;
    }

    protected void applyGravityForce() {
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
            aff.applyForce(fx, fy, seconds);
        }
    }


    public void paintSceneTo(Display display) {
        if (!showWake) {
            display.clear();
        }
        for (PhysicalObject po : objects) {
            doPaintObject(display, po);
        }
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void step() {
        collide();
        applyGravityForce();
        step++;
    }

}
