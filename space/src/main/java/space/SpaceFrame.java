package space;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;

public class SpaceFrame extends JFrame {

    private final Space space;

    public SpaceFrame(Space space) {
        this.space = space;
    }

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        new SpaceFrame(new Bounce()).run();
    }

    private void run() throws InvocationTargetException, InterruptedException {
        space.run();
    }

}
