package space;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class SwingDisplay implements Display {

    private final Graphics2D graphics;
    private int width;
    private int height;
    private BufferedImage buffer;

    public SwingDisplay(int width, int height) {
        this.buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.graphics = buffer.createGraphics();
        this.width = width;
        this.height = height;
    }

    @Override
    public void fillCircle(double centerX, double centerY, double radius, Color color) {
        graphics.setColor(color);
        graphics.fillOval((int)(centerX- radius), (int)(centerY-radius), (int)radius * 2, (int)radius * 2);
    }

    public void clear() {
        graphics.clearRect(0, 0, width, height);
    }

    public void drawBufferTo(Graphics original) {
        original.drawImage(buffer, 0, 0, width, height, null);
    }

}
