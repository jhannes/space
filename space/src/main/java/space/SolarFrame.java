package space;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.lang.reflect.InvocationTargetException;

public class SolarFrame extends SpaceFrame {

    protected Point lastDrag;

    public SolarFrame(Space space) {
        super(space);
    }

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        new SolarFrame(new Solar()).run();
    }

    @Override
    protected void run() throws InvocationTargetException, InterruptedException {
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                space.scale += space.scale * (Math.min(9, e.getWheelRotation())) / 10 + 0.0001;
                getGraphics().clearRect(0, 0, getWidth(), getHeight());
            }
        });
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (lastDrag == null) {
                    lastDrag = e.getPoint();
                }
                int deltaX = e.getX() - lastDrag.x;
                int deltaY = e.getY() - lastDrag.y;
                space.drag(deltaX, deltaY);
                lastDrag = e.getPoint();
                getGraphics().clearRect(0, 0, getWidth(), getHeight());
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                lastDrag = null;
            }
        });
        super.run();
    }


}
