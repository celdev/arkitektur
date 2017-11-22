package arkanoid.world;

import arkanoid.Drawable;
import arkanoid.GameObject;

public abstract class Block extends GameObject implements Drawable {

    private boolean isVisible = true;
    private int score;

    public Block(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    abstract boolean collisionWithBall(Ball ball);

    public boolean isVisible() {
        return isVisible;
    }

    public void disable() {
        isVisible = false;
    }
}
