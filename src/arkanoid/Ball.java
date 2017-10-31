/*
 * This game is used in the course Arkitekture and Design patterns. During the course we'll discuss diffrent aspekt that makes 
 * this implmentation weak. It's then your job to improve it.
 */
package arkanoid;

import java.util.Random;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Ball {

    private int x;
    private int y;

    private float speed;
    private boolean isMoving;

    private Vector2f velocityDir;
    private Rectangle collider;

    private final int X_MIN;
    private final int X_MAX;
    private final int Y_MIN;
    private final int Y_MAX;
    private final float RADIUS;
    private final int WIDTH = 32;
    private final int HEIGHT = 32;
    private final int SCREEN_WIDTH;
    private final int SCREEN_HEIGHT;
    private final Paint PAINT;

    public Ball(int inScreenWidth, int inScreenHeight) {
        SCREEN_WIDTH = inScreenWidth;
        SCREEN_HEIGHT = inScreenHeight;

        //set initial sprite and collider position, initialize velocity
        reset();

        X_MIN = WIDTH / 2;
        X_MAX = SCREEN_WIDTH - WIDTH / 2;
        Y_MIN = HEIGHT / 2;
        Y_MAX = SCREEN_HEIGHT / 4 * 5;

        RADIUS = HEIGHT / 2;

        speed = 10;

        PAINT = Paint.valueOf(Color.rgb(255, 170, 0).toString());
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

    public void update() {
        //if (!isMoving)
        //    return;

        x += speed * velocityDir.getX();
        y += speed * velocityDir.getY();

        if (x > X_MAX) {
            x = X_MAX;
            velocityDir.invertX();
        } else if (x < X_MIN) {
            x = X_MIN;
            velocityDir.invertX();
        }

        if (y < 0) {
            y = Y_MIN;
            velocityDir.invertY();
        }

        collider = new Rectangle(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);

    }

    public void setMoving(boolean status) {
        isMoving = status;
    }

    public void invertY(double newTilt) {
        velocityDir.invertY();
        velocityDir.setTitlt(newTilt);
    }

    public void reset() {
        x = SCREEN_WIDTH / 2;
        y = SCREEN_HEIGHT / 10 * 9 - HEIGHT;

        collider = new Rectangle(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);
        setMoving(false);

        Random R = new Random();
        //normalized internally
        velocityDir = new Vector2f(-1 + 2 * R.nextFloat(), 20, -1);
    }

    public boolean isOutsideScreen() {
        return (y > Y_MAX);
    }

    public void setSpeedScale(float scale) {
        speed *= scale;
    }

    public void draw(GraphicsContext canvas) {
        Paint previousPaint = canvas.getFill();
        canvas.setFill(Color.GREEN);
        canvas.fillOval(x, y, RADIUS, RADIUS);
        canvas.setFill(previousPaint);
    }

}
