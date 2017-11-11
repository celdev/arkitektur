package arkanoid;

import javafx.scene.canvas.GraphicsContext;

public interface GameEngine {
    void registerObserver(Observer observer);

    void unregisterObserver(Observer observer);

    void initWorld(GraphicsContext graphicsContext);

    void start();

    void stop();

    void resetLevel();
}
