package arkanoid.world;

import arkanoid.GameEngine;

public class HardCodedLevel extends Level {

    private final int spaceHor = 16;
    private final int spaceVert = 16;
    private final int noBlocksInLine = 10;
    private final int edgeOffsetHor = 300;
    private final int edgeOffsetVert = 32;

    public HardCodedLevel(GameEngine ge, int blocks) {
        super(ge, blocks);
    }

    @Override
    void createBlocks() {
        System.out.println("Creating blocks");
        for (int i = 0; i < getActiveBlocksInLevel(); i++) {
            Block b = new Block(getBlockX(i), getBlockY(i));
            getBlocks().add(b);
        }
        System.out.println("Blocks in list = " + getBlocks().size());
    }

    public int getBlockX(int blockNumber) {
        return edgeOffsetHor + Block.WIDTH / 2 + (blockNumber % noBlocksInLine) * (Block.WIDTH + spaceHor);
    }

    public int getBlockY(int blockNumber) {
        return edgeOffsetVert + Block.HEIGHT / 2 + (int) ((Block.HEIGHT + spaceVert) * Math.floor(blockNumber / noBlocksInLine));
    }
}
