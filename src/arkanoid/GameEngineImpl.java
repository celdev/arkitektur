/*
 * This game is used in the course Arkitekture and Design patterns. During the course we'll discuss diffrent aspekt that makes 
 * this implmentation weak. It's then your job to improve it.
 */
package arkanoid;

import java.util.ArrayList;
import java.util.List;

import arkanoid.ui.UIEngine;
import arkanoid.ui.UIEngineImpl;
import arkanoid.world.Ball;
import arkanoid.world.Block;
import arkanoid.world.Paddle;
import javafx.event.EventHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class GameEngineImpl implements Runnable, EventHandler<MouseEvent>, GameEngine {

    public static final int LOOP_MILLIS = 17;
    private Thread gameThread = null;

    private volatile boolean playing;
    private boolean isGameOver;
    private boolean collisionSuppressed;

    private int score = 0;
    private int lives = 3;
    private int activeBlocksInLevel = 20;
    private int blocksInLevel = 20;

    private double mouseX;
    private double paddleMovment;
    private float ballSpeedFactor = 1;
    
    private Timer levelWaitTimer;
    private GraphicsContext gc;

    //entity member variables
    private Ball ball;
    private Paddle paddle;
    private UIEngine uiEngine;
    private ArrayList<Block> blocks = new ArrayList<>();
    private List<Observer> observers = new ArrayList<>();
    private List<Drawable> gameObjects = new ArrayList<>();

    private final int spaceHor = 16;
    private final int spaceVert = 16;
    private final int noBlocksInLine = 5;
    private final int edgeOffsetHor = 300;
    private final int edgeOffsetVert = 32;

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

    public void updateObservers() {
        for (Observer observer : observers) {
            observer.update((double)activeBlocksInLevel/(double)blocksInLevel);
        }
    }

    @Override
    public void initWorld(GraphicsContext graphicsContext) {
        gc = graphicsContext;
        uiEngine = new UIEngineImpl(gc);
        paddle = new Paddle();
        ball = new Ball((int) gc.getCanvas().getWidth(), (int) gc.getCanvas().getHeight());

        levelWaitTimer = new Timer();

        for (int i = 0; i < activeBlocksInLevel; i++) {
            Block b = new Block(getBlockX(i), getBlockY(i));
            blocks.add(b);
            gameObjects.add(b);
        }

        //HUD elements


        playing = true;

        gameObjects.add(paddle);
        gameObjects.add(ball);

        gc.getCanvas().addEventFilter(MouseEvent.MOUSE_MOVED, this);
    }

    @Override
    public void start() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while (playing) {
            update();
            draw();
            control();//sleep
            if (levelWaitTimer.isExpired()) {
                resetLevel();
            }
        }
    }

    @Override
    public void stop() {
        playing = false;
    }

    @Override
    public void resetLevel() {
        paddle.reset();
        ball.reset();

        activeBlocksInLevel = 20;

        //increase ball speed, reduce paddle width
        ballSpeedFactor *= 1.1;// cumulative 10% increase per level
        ball.setSpeedScale(ballSpeedFactor);
        paddle.reduceWidth(10);// -10px per level
        updateObservers();
        for (Block b : blocks) {
            b.setVisible(true);
        }
    }

    public int getBlockX(int i) {
        //i is block number
        return edgeOffsetHor + Block.WIDTH / 2 + (i % noBlocksInLine) * (Block.WIDTH + spaceHor);
    }

    public int getBlockY(int i) {
        //i is block number
        return edgeOffsetVert + Block.HEIGHT / 2 + (int) ((Block.HEIGHT + spaceVert) * Math.floor(i / noBlocksInLine));
    }

    private void update() {
        ball.update();

        //is ball outside screen?
        if (ball.isOutsideScreen()) {
            ball.reset();
            paddle.reset();
            lives--;
            if (lives < 1) {
                isGameOver = true;
                playing = false;
            }
        }

        //ball vs paddle
        if (!collisionSuppressed && paddle.getRect().getBoundsInParent().intersects(ball.getRect().getBoundsInParent())) {
            ball.invertY((paddleMovment * 0.3));
            collisionSuppressed = true;
        } else if (collisionSuppressed) {
            collisionSuppressed = false;
        }

        //ball vs blocks
        for (Block b : blocks) {
            if (b.isVisible() && b.getRect().getBoundsInParent().intersects(ball.getRect().getBoundsInParent())) {
                //the ball collided with a block
                b.setVisible(false);
                activeBlocksInLevel--;

                updateObservers();
                
                ball.invertY(0.0);
                score += 50;
                //the last block was destroyed
                if (activeBlocksInLevel < 1) {
                    levelWaitTimer.startTimer(2);
                }
            }
        }

        //update HUDs
        uiEngine.update();
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
            gameObjects.forEach(drawable -> drawable.draw(gc));
            uiEngine.draw(gc);
            if (isGameOver) {
                uiEngine.stop(gc);
            }
        }
    }

    private void reset(GraphicsContext canvas, Color color) {
        canvas.setFill(color);
        canvas.fillRect(0, 0, canvas.getCanvas().getWidth(), canvas.getCanvas().getHeight());

    }

    private void control() {
        try {
            Thread.sleep(LOOP_MILLIS);
        } catch (InterruptedException e) {
        }
    }

    @Override
    public void handle(MouseEvent e) {
        paddle.updateMousePosition((int) (e.getX() - mouseX));
        paddle.update();
        paddleMovment = (int) (e.getX() - mouseX);
        mouseX = e.getX();
    }
}
