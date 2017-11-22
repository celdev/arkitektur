package arkanoid.world;

import arkanoid.GameEngine;

public class HardCodedLevel extends Level {

    private final int spaceHor = 16;
    private final int spaceVert = 16;
    private final int noBlocksInLine = 10;
    private final int edgeOffsetHor = 300;
    private final int edgeOffsetVert = 32;
    private final int scorePerBlock = 50;

    public HardCodedLevel(GameEngine ge, int blocks) {
        super(ge, blocks);
    }

    @Override
    void createBlocks() {
        for (int i = 0; i < getActiveBlocksInLevel(); i++) {
            Block b = new RectBlockImpl(getBlockX(i), getBlockY(i), scorePerBlock);
            getBlocks().add(b);
        }
    }

    public int getBlockX(int blockNumber) {
        return edgeOffsetHor + RectBlockImpl.WIDTH / 2 + (blockNumber % noBlocksInLine) * (RectBlockImpl.WIDTH + spaceHor);
    }

    public int getBlockY(int blockNumber) {
        return edgeOffsetVert + RectBlockImpl.HEIGHT / 2 + (int) ((RectBlockImpl.HEIGHT + spaceVert) * Math.floor(blockNumber / noBlocksInLine));
    }
}
