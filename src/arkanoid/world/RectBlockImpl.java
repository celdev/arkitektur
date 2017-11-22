/*
 * This game is used in the course Arkitekture and Design patterns. During the course we'll discuss diffrent aspekt that makes 
 * this implmentation weak. It's then your job to improve it.
 */
package arkanoid.world;

import java.util.Random;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class RectBlockImpl extends Block {

    public final static int WIDTH = 20;
    public final static int HEIGHT = 10;


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

    public RectBlockImpl(int x, int y, int score) {
        super(score);
        PAINT = Paint.valueOf(COLORS[new Random().nextInt(COLORS.length)].toString());
        COLLIDER = new Rectangle(x - WIDTH / 2, y - HEIGHT / 2, WIDTH, HEIGHT);
    }

    public Rectangle getRect() {
        return COLLIDER;
    }

    public void draw(GraphicsContext canvas) {
        if (super.isVisible()) {
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

    @Override
    boolean collisionWithBall(Ball ball) {
        return super.isVisible() && getRect().getBoundsInParent().intersects(ball.getCollider().getBoundsInParent());
    }
}
