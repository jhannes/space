package space;

import java.lang.reflect.InvocationTargetException;

public class Bounce extends Space {

    public static void main(String[] args) throws InvocationTargetException, InterruptedException {
        new Bounce().run(false);
    }

}
