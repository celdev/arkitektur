/*
 * This game is used in the course Arkitekture and Design patterns. During the course we'll discuss diffrent aspekt that makes 
 * this implmentation weak. It's then your job to improve it.
 */
package arkanoid;

import java.util.ArrayList;
import java.util.List;

import arkanoid.ui.UIEngine;
import arkanoid.ui.UIEngineImpl;
import arkanoid.world.*;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class GameEngineImpl implements Runnable, EventHandler<MouseEvent>, GameEngine {

    private static final int LOOP_MILLIS = 17;

    private volatile boolean playing;
    private boolean isGameOver;
    private boolean gameCompleted;

    private int score = 0;
    private int lives = 3;
    private int currentLevel = 0;

    private double mouseX;


    private Timer levelWaitTimer = new Timer();
    private GraphicsContext gc;

    private Ball ball;
    private Paddle paddle;
    private UIEngine uiEngine;
    private List<Observer> observers = new ArrayList<>();
    private List<Drawable> drawables = new ArrayList<>();
    private List<Level> levels;
    private Level level;



    private static GameEngine instance;

    private GameEngineImpl() {}

    public static GameEngine getInstance() {
        if (instance == null) {
            synchronized (GameEngineImpl.class){
                if (instance == null) {
                    instance = new GameEngineImpl();
                }
            }
        }
        return instance;
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void unregisterObserver(Observer observer) {
        observers.remove(observer);
    }

    private void updateObservers() {
        observers.forEach(observer -> observer.update((double)level.getActiveBlocksInLevel()/(double)level.getBlocksInLevel()));
    }

    @Override
    public void initWorld(GraphicsContext graphicsContext, List<Level> levels) {
        gc = graphicsContext;
        this.levels = levels;
        uiEngine = new UIEngineImpl(gc);
        paddle = new Paddle();
        ball = new Ball((int) gc.getCanvas().getWidth(), (int) gc.getCanvas().getHeight());
        level = levels.get(0);
        playing = true;

        drawables.add(paddle);
        drawables.add(ball);

        gc.getCanvas().addEventFilter(MouseEvent.MOUSE_MOVED, this);
    }

    @Override
    public void start() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();//sleep
            if (levelWaitTimer.isExpired()) {
                nextLevel();
            }
        }
        //playing is false so either the game over or the game completed text should be shown
        if (isGameOver) {
            uiEngine.stop(gc);
        }
        if (gameCompleted) {
            uiEngine.finishGame(gc, score);
        }
    }

    @Override
    public void stop() {
        playing = false;
    }

    @Override
    public void nextLevel() {
        paddle.reset();
        ball.reset();

        ball.increaseSpeed();
        paddle.reduceWidth(10);// -10px per level
        updateObservers();
        if (++currentLevel < levels.size()) {
            level = levels.get(currentLevel);
        } else {
            playing = false;
            gameCompleted = true;
        }
    }

    private void ballLost() {
        ball.reset();
        paddle.reset();
        lives--;
        if (lives < 1) {
            isGameOver = true;
            playing = false;
        }
    }

    private void update() {
        ball.update();

        //is ball outside screen?
        if (ball.isOutsideScreen()) {
            ballLost();
            return;
        }
        ball.checkPaddleCollision(paddle);
        level.checkCollision(ball);

        //update HUDs
        uiEngine.update();
        updateObservers();
    }

    @Override
    public void levelFinished() {
        levelWaitTimer.startTimer(2);
    }

    @Override
    public void increaseScore(int toIncrease) {
        score += toIncrease;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public int getLives() {
        return lives;
    }

    private void draw() {
        if (gc != null) {
            reset(gc, Color.DARKGRAY);
            drawables.forEach(drawable -> drawable.draw(gc));
            uiEngine.draw(gc);
            level.draw(gc);
        }
    }

    private void reset(GraphicsContext canvas, Color color) {
        canvas.setFill(color);
        canvas.fillRect(0, 0, canvas.getCanvas().getWidth(), canvas.getCanvas().getHeight());
    }

    private void control() {
        try {
            Thread.sleep(LOOP_MILLIS);
        } catch (InterruptedException e) {//
        }
    }

    @Override
    public void handle(MouseEvent e) {
        paddle.updateMousePosition((int) (e.getX() - mouseX));
        paddle.update();
        mouseX = e.getX();
    }
}
