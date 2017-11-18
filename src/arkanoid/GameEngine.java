package arkanoid;

import arkanoid.world.Level;
import javafx.scene.canvas.GraphicsContext;

import java.util.List;

public interface GameEngine {
    void registerObserver(Observer observer);

    void unregisterObserver(Observer observer);

    void initWorld(GraphicsContext graphicsContext, List<Level> levels);

    void start();

    void stop();

    void nextLevel();

    void levelFinished();

    void increaseScore(int toIncrease);

    int getScore();

    int getLives();
}
