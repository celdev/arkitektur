/*
 * This game is used in the course Arkitekture and Design patterns. During the course we'll discuss diffrent aspekt that makes 
 * this implmentation weak. It's then your job to improve it.
 */
package arkanoid.ui;

import arkanoid.Drawable;
import arkanoid.GameObject;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public abstract class HUD extends GameObject implements Drawable {

    private String text = "";

    private final String STATIC_TEXT;
    private final int X;
    private final int Y;

    public HUD(int inX, int inY, String inStaticText) {
        X = inX;
        Y = inY;
        STATIC_TEXT = inStaticText;
        start();
    }

    public void draw(GraphicsContext canvas) {
        Paint previousPaint = canvas.getFill();
        canvas.setFill(Color.WHITE);
        canvas.fillText(STATIC_TEXT + text, X, Y);
        canvas.setFill(previousPaint);
    }

    public void setValue(int value) {
        this.text = String.valueOf(value);
    }

    @Override
    public void start() {

    }

    @Override
    public abstract void update();
}
