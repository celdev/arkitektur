package arkanoid.ui;

import arkanoid.Drawable;
import arkanoid.GameEngineImpl;
import arkanoid.GameObject;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UIEngineImpl implements UIEngine {

    private List<GameObject> uiElements = new ArrayList<>();
    private List<Drawable> drawableUiElements = new ArrayList<>();

    public UIEngineImpl(GraphicsContext gc) {
        uiElements.add(new HUD(20, 20, "Score: "){
            @Override
            public void update() {
                this.setValue(GameEngineImpl.getInstance().getScore());
            }
        });
        uiElements.add(new HUD(20, 40, "Lives: "){
            @Override
            public void update() {
                this.setValue(GameEngineImpl.getInstance().getLives());
            }
        });
        drawableUiElements.addAll(getDrawables(uiElements));
    }

    private List<Drawable> getDrawables(List<GameObject> uiElements) {
        return uiElements.stream().filter(gameObject -> gameObject instanceof Drawable).map(gameObject -> (Drawable)gameObject).collect(Collectors.toList());
    }

    @Override
    public void update() {
        uiElements.forEach(GameObject::update);
    }

    @Override
    public void draw(GraphicsContext gc) {
        drawableUiElements.forEach(d -> d.draw(gc));
    }

    @Override
    public void stop(GraphicsContext gc) {
        HUD gameOverHUD = new HUD((int) gc.getCanvas().getWidth() / 2, (int) gc.getCanvas().getHeight() / 2, "Game Over"){
            @Override
            public void update() {}
        };
        gameOverHUD.draw(gc);
    }
}
