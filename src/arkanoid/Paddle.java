/*
 * This game is used in the course Arkitekture and Design patterns. During the course we'll discuss diffrent aspekt that makes 
 * this implmentation weak. It's then your job to improve it.
 */
package arkanoid;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class Paddle implements Drawable{

    private int x;
    private int y;
    private int width = 50;

    private final int HEIGHT = 15;
    private final int X_MIN;
    private final int X_MAX;
    private final int SCREEN_WIDTH;
    private final int SCREEN_HEIGHT;

    private Rectangle collider;
    private final Paint paint;

    public Paddle(int inScreenWidth, int inScreenHeight) {
        SCREEN_WIDTH = inScreenWidth;
        SCREEN_HEIGHT = inScreenHeight;

        X_MIN = width / 2;
        X_MAX = SCREEN_WIDTH - width / 2;

        x = SCREEN_WIDTH / 2;
        y = SCREEN_HEIGHT / 10 * 9;

        collider = new Rectangle(x - width / 2, y - HEIGHT / 2, width, HEIGHT);

        paint = Paint.valueOf(Color.CYAN.toString());
    }

    public Rectangle getRect() {
        return collider;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void update(float dx) {
        x += dx;

        if (x < X_MIN) {
            x = X_MIN;
        }
        if (x > X_MAX) {
            x = X_MAX;
        }

        collider = new Rectangle(x - width / 2, y - HEIGHT / 2, width, HEIGHT);
    }

    public void reset() {
        x = SCREEN_WIDTH / 2;
        y = SCREEN_HEIGHT / 10 * 9;

        collider = new Rectangle(x - width / 2, y - HEIGHT / 2, width, HEIGHT);
    }

    public void reduceWidth(int reduction) {
        width -= reduction;
    }

    public void draw(GraphicsContext canvas) {
        Paint previousPaint = canvas.getFill();
        canvas.setFill(paint);
        canvas.fillRect(collider.getX(), collider.getY(), collider.getWidth(), collider.getHeight());
        canvas.setFill(previousPaint);
    }

}
