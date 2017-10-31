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
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Arkanoid extends Application {

    private Group root;
    private Canvas canvas;
    private ProgressBar progressBar;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Arkanoidf");

        root = new Group();

        VBox rows = new VBox();

        progressBar = new ProgressBar(1);
        progressBar.setMaxWidth(Double.MAX_VALUE);
        rows.getChildren().add(progressBar);

        canvas = new Canvas(800, 600);
        rows.getChildren().add(canvas);

        GraphicsContext gc = canvas.getGraphicsContext2D();
        GameEngine.getInstance().initWorld(gc, progressBar);
        root.getChildren().add(rows);
        primaryStage.setScene(new Scene(root));
        primaryStage.setOnCloseRequest((WindowEvent we) -> {
            GameEngine.getInstance().stop();
            Platform.exit();
        });

        primaryStage.show();
        GameEngine.getInstance().start();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
