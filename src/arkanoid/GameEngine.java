/*
 * This game is used in the course Arkitekture and Design patterns. During the course we'll discuss diffrent aspekt that makes 
 * this implmentation weak. It's then your job to improve it.
 */
package arkanoid;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ProgressBar;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class GameEngine implements Runnable {

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
    private GraphicsContext canvas;
    private ProgressBar pb;

    //entity member variables
    private Ball ball;
    private HUD scoreHUD;
    private HUD livesHUD;
    private Paddle paddle;
    private ArrayList<Block> blocks = new ArrayList<>();
    private List<Observer> observers = new ArrayList<>();
    private List<Drawable> gameObjects = new ArrayList<>();

    private final int spaceHor = 16;
    private final int spaceVert = 16;
    private final int noBlocksInLine = 5;
    private final int edgeOffsetHor = 300;
    private final int edgeOffsetVert = 32;

    private static GameEngine instance;

    private GameEngine() {}

    public static GameEngine getInstance() {
        if (instance == null) {
            synchronized (GameEngine.class){
                if (instance == null) {
                    instance = new GameEngine();
                }
            }
        }
        return instance;
    }

    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    public void unregisterObserver(Observer observer) {
        observers.remove(observer);
    }

    public void updateObservers() {
        for (Observer observer : observers) {
            observer.update((double)activeBlocksInLevel/(double)blocksInLevel);
        }
    }

    public void initWorld(GraphicsContext graphicsContext) {
        canvas = graphicsContext;

        
        paddle = new Paddle((int)canvas.getCanvas().getWidth(), (int)canvas.getCanvas().getHeight());
        ball = new Ball((int)canvas.getCanvas().getWidth(), (int)canvas.getCanvas().getHeight());

        levelWaitTimer = new Timer();

        for (int i = 0; i < activeBlocksInLevel; i++) {
            Block b = new Block(getBlockX(i), getBlockY(i));
            blocks.add(b);
            gameObjects.add(b);
        }

        //HUD elements
        scoreHUD = new HUD(20, 20, "Score: ", 36);
        livesHUD = new HUD(20, 40, "Lives: ", 36);

        playing = true;

        gameObjects.add(paddle);
        gameObjects.add(ball);
        gameObjects.add(scoreHUD);
        gameObjects.add(livesHUD);

        canvas.getCanvas().addEventHandler(MouseEvent.MOUSE_MOVED, (MouseEvent e) -> {
            paddle.update((int) (e.getX() - mouseX));
            paddleMovment = (int) (e.getX() - mouseX);
            mouseX = e.getX();
        });
    }

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

    public void stop() {
        playing = false;
    }

    public void resetLevel() {
        paddle.reset();
        ball.reset();

        activeBlocksInLevel = 20;

        //increase ball speed, reduce paddle width
        ballSpeedFactor *= 1.1;// cumulative 10% increase per level
        ball.setSpeedScale(ballSpeedFactor);
        paddle.reduceWidth(10);// -10px per level

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
        scoreHUD.update(score);
        livesHUD.update(lives);
    }

    private void draw() {
        if (canvas != null) {
            reset(canvas, Color.DARKGRAY);
            gameObjects.forEach(drawable -> drawable.draw(canvas));
            if (isGameOver) {
                HUD gameOverHUD = new HUD((int) canvas.getCanvas().getWidth() / 2, (int) canvas.getCanvas().getHeight() / 2, "Game Over", 75);
                gameOverHUD.draw(canvas);
            }
        }
    }

    private void reset(GraphicsContext canvas, Color color) {
        canvas.setFill(color);
        canvas.fillRect(0, 0, canvas.getCanvas().getWidth(), canvas.getCanvas().getHeight());
    }

    private void control() {
        try {
            Thread.sleep(17);
        } catch (InterruptedException e) {
        }
    }

}
