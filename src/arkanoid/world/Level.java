package arkanoid.world;

import arkanoid.Drawable;
import arkanoid.GameEngine;
import arkanoid.GameEngineImpl;
import arkanoid.GameObject;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public abstract class Level extends GameObject implements Drawable {

    private int blocksInLevel;
    private int activeBlocksInLevel;

    private final int spaceHor = 16;
    private final int spaceVert = 16;
    private final int noBlocksInLine = 10;
    private final int edgeOffsetHor = 300;
    private final int edgeOffsetVert = 32;

    private List<Block> blocks = new ArrayList<>();
    private GameEngine ge;

    public Level(GameEngine ge, int blockInLevel) {
        this.ge = ge;
        this.blocksInLevel = blockInLevel;
        this.activeBlocksInLevel = blocksInLevel;
        start();
    }

    @Override
    public void start() {
        createBlocks();
    }

    abstract void createBlocks();



    @Override
    public void update() {

    }

    public int getActiveBlocksInLevel() {
        return activeBlocksInLevel;
    }

    public int getBlocksInLevel() {
        return blocksInLevel;
    }

    public void checkCollision(Ball ball) {
        for (Block b : blocks) {
            if (b.isVisible() && b.getRect().getBoundsInParent().intersects(ball.getCollider().getBoundsInParent())) {
                //the ball collided with a block
                b.setVisible(false);
                activeBlocksInLevel--;
                ball.invertY(0.0);
                ge.increaseScore(50);
                //the last block was destroyed
                if (activeBlocksInLevel < 1) {
                    ge.levelFinished();
                }
            }
        }
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    @Override
    public void draw(GraphicsContext gc) {
        blocks.forEach(block -> block.draw(gc));
    }
}
