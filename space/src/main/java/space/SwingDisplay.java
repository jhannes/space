package space;

import java.awt.Color;
import java.awt.Graphics2D;

public class SwingDisplay implements Display {

    private final Graphics2D graphics;

    public SwingDisplay(Graphics2D graphics) {
        this.graphics = graphics;
    }

    @Override
    public void fillCircle(double centerX, double centerY, double radius, Color color) {
        graphics.setColor(color);
        graphics.fillOval((int)(centerX- radius), (int)(centerY-radius), (int)radius * 2, (int)radius * 2);
    }

}
