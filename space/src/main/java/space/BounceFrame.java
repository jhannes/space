package space;

import java.lang.reflect.InvocationTargetException;

public class BounceFrame {

    private final Bounce space;

    public BounceFrame(Bounce bounce) {
        this.space = bounce;
    }

    public void run() throws InvocationTargetException, InterruptedException {
        space.run();
    }

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        new BounceFrame(new Bounce()).run();
    }

}
