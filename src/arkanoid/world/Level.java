package arkanoid.world;

import arkanoid.GameEngineImpl;
import arkanoid.GameObject;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;

public class Level extends GameObject {

    private final int blocksInLevel = 30;
    private int activeBlocksInLevel = blocksInLevel;

    private final int spaceHor = 16;
    private final int spaceVert = 16;
    private final int noBlocksInLine = 10;
    private final int edgeOffsetHor = 300;
    private final int edgeOffsetVert = 32;

    private List<Block> blocks = new ArrayList<>();

    public Level() {
        start();
    }

    @Override
    public void start() {
        for (int i = 0; i < activeBlocksInLevel; i++) {
            Block b = new Block(getBlockX(i), getBlockY(i));
            blocks.add(b);
        }
    }

    public int getBlockX(int blockNumber) {
        return edgeOffsetHor + Block.WIDTH / 2 + (blockNumber % noBlocksInLine) * (Block.WIDTH + spaceHor);
    }

    public int getBlockY(int blockNumber) {
        return edgeOffsetVert + Block.HEIGHT / 2 + (int) ((Block.HEIGHT + spaceVert) * Math.floor(blockNumber / noBlocksInLine));
    }

    @Override
    public void update() {

    }

    public void resetLevel() {
        for (Block b : blocks) {
            b.setVisible(true);
        }
        activeBlocksInLevel = blocksInLevel;
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
                GameEngineImpl.getInstance().increaseScore(50);
                //the last block was destroyed
                if (activeBlocksInLevel < 1) {
                    GameEngineImpl.getInstance().levelFinished();
                }
            }
        }
    }

    public void draw(GraphicsContext gc) {
        blocks.forEach(block -> block.draw(gc));
    }
}
