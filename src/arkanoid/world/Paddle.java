/*
 * This game is used in the course Arkitekture and Design patterns. During the course we'll discuss diffrent aspekt that makes 
 * this implmentation weak. It's then your job to improve it.
 */
package arkanoid.world;

import arkanoid.Arkanoid;
import arkanoid.Drawable;
import arkanoid.GameObject;
import arkanoid.MouseMovementListener;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

public class Paddle extends GameObject implements Drawable, MouseMovementListener {

    public static final double PADDLE_MOVEMENT_HIT_BALL_MODIFIER = 0.3;
    private int x = Arkanoid.WINDOW_WIDTH / 2;
    private int y = Arkanoid.WINDOW_HEIGHT / 10 * 9;
    private int width = 50;

    private final int HEIGHT = 15;
    private final int X_MIN = width / 2;
    private final int X_MAX = Arkanoid.WINDOW_WIDTH - width / 2;
    private double paddleMovement;

    private Rectangle collider = new Rectangle(x - width / 2, y - HEIGHT / 2, width, HEIGHT);
    private final Paint paint = Paint.valueOf(Color.CYAN.toString());;

    public Paddle() {
        start();
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

    @Override
    public void start() {
        x = Arkanoid.WINDOW_WIDTH / 2;
        y = Arkanoid.WINDOW_HEIGHT / 10 * 9;
        collider = new Rectangle(x - width / 2, y - HEIGHT / 2, width, HEIGHT);
    }

    public void update() {
        collider = new Rectangle(x - width / 2, y - HEIGHT / 2, width, HEIGHT);
    }

    public void updateMousePosition(int dx) {
        paddleMovement = dx;
        x += dx;

        if (x < X_MIN) {
            x = X_MIN;
        }
        if (x > X_MAX) {
            x = X_MAX;
        }
    }

    double getPaddleMovement() {
        return paddleMovement;
    }

    public void reset() {
        start();
    }

    public void reduceWidth(int reduction) {
        width -= reduction;
    }

    @Override
    public void draw(GraphicsContext canvas) {
        Paint previousPaint = canvas.getFill();
        canvas.setFill(paint);
        canvas.fillRect(collider.getX(), collider.getY(), collider.getWidth(), collider.getHeight());
        canvas.setFill(previousPaint);
    }

}
