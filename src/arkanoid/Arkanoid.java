/*
 * This game is used in the course Arkitekture and Design patterns. During the course we'll discuss diffrent aspekt that makes 
 * this implmentation weak. It's then your job to improve it.
 */
package arkanoid;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Arkanoid extends Application implements Observer {

    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;

    private ProgressBar progressBar;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Arkanoid");

        progressBar = new ProgressBar(1);
        progressBar.setMaxWidth(Double.MAX_VALUE);

        Canvas canvas = new Canvas(WINDOW_WIDTH, WINDOW_HEIGHT);

        GameEngineImpl.getInstance().initWorld(canvas.getGraphicsContext2D());
        GameEngineImpl.getInstance().registerObserver(this);
        primaryStage.setScene(new Scene(new Group(new VBox(progressBar, canvas))));
        primaryStage.setOnCloseRequest((WindowEvent we) -> {
            GameEngineImpl.getInstance().stop();
            Platform.exit();
        });

        primaryStage.show();
        GameEngineImpl.getInstance().start();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void update(double progress) {
        progressBar.setProgress(progress);
    }
}
