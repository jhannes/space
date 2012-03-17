package space;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.InvocationTargetException;

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


    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        new Solar().run(true);
    }

}
