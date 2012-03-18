package space;

import java.util.ArrayList;
import java.util.List;

public abstract class Space {
    protected static final double ASTRONOMICAL_UNIT = 149597870.7e3;

    protected static final double G = 6.67428e-11; // m3/kgs2
    protected List<PhysicalObject> objects = new ArrayList<PhysicalObject>();
    boolean showWake = false;
    int step = 0;
    protected int nrOfObjects = 75;
    int frameRate = 25;

    protected int width;

    protected int height;

    protected String getTitleString() {
        return "Objects:" + objects.size() + " scale:" + PhysicalObject.scale + " steps:" + step + " frame rate: " + frameRate;
    }

    protected abstract void doPaintObject(Display display, PhysicalObject po);

    protected abstract void createPhysicalObjects();

    protected abstract void collide();

    protected static double randSquare() {
        double random = Math.random();
        return random * random;
    }

    public void setStepSize(double seconds) {
        PhysicalObject.seconds = seconds;
    }

    public PhysicalObject add(double weightKilos, double x, double y,
                                     double vx, double vy, double radius) {
        PhysicalObject physicalObject = new PhysicalObject(weightKilos, x, y,
                vx, vy, radius);
        objects.add(physicalObject);
        return physicalObject;
    }

    protected abstract void step();

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

}
