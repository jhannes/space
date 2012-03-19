package space;

import java.util.ArrayList;
import java.util.List;

public abstract class Space {

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
        PhysicalObject.applyGravityForce(objects, seconds);
        step++;
    }

    public void drag(int deltaX, int deltaY) {
        centrex -= (deltaX * scale);
        centrey -= (deltaY * scale);
    }

}
