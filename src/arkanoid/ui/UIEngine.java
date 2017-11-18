package arkanoid.ui;

import javafx.scene.canvas.GraphicsContext;

public interface UIEngine {

    void update();

    void stop(GraphicsContext graphicsContext);

    void draw(GraphicsContext graphicsContext);

    void finishGame(GraphicsContext gc, int score);
}
