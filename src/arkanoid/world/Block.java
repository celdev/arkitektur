/*
 * This game is used in the course Arkitekture and Design patterns. During the course we'll discuss diffrent aspekt that makes 
 * this implmentation weak. It's then your job to improve it.
 */
package arkanoid.world;

import java.util.Random;

import arkanoid.Drawable;
import arkanoid.GameObject;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Block extends GameObject implements Drawable {

    public final static int WIDTH = 20;
    public final static int HEIGHT = 10;

    private boolean isVisible = true;

    private final Rectangle COLLIDER;
    private final Paint PAINT;

    private static final Color[] COLORS = {
            Color.RED,
            Color.GREEN,
            Color.BLUE,
            Color.CYAN,
            Color.YELLOW,
            Color.MAGENTA,
            Color.BROWN
    };

    public Block(int x, int y) {
        PAINT = Paint.valueOf(COLORS[new Random().nextInt(COLORS.length)].toString());
        COLLIDER = new Rectangle(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);
    }

    public Rectangle getRect() {
        return COLLIDER;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean status) {
        isVisible = status;
    }

    public void draw(GraphicsContext canvas) {
        if (isVisible) {
            Paint previousPaint = canvas.getFill();
            canvas.setFill(PAINT);
            canvas.fillRect(COLLIDER.getX(), COLLIDER.getY(), COLLIDER.getWidth(), COLLIDER.getHeight());
            canvas.setFill(previousPaint);
        }
    }

    @Override
    public void start() {

    }

    @Override
    public void update() {

    }
}
