package breakout;

import javafx.application.Application;
import javafx.stage.Stage;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import java.lang.Math.*;


/**
 * Messing around with JFX
 * Code base attributed to Robert C. Duval
 * @author Robert C. Duvall
 * @author Max Smith
 */
public class Main extends Application {
    public static final String TITLE = "Bounce Test JavaFX";
    public static final int SIZE = 400;
    public static final int FRAMES_PER_SECOND = 120;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final Paint BACKGROUND = Color.AZURE;
    public static final String BOUNCER_IMAGE = "ball.gif";
    public static final String PADDLE_IMAGE = "paddle.gif";
    public static final int PADDLE_SPEED = 10;
    public static final int PADDLE_FLOAT = 30;  // paddle pixel float above bottom of screen
    public static final double PADDLE_EDGE = Math.PI / 4;       // max paddle angular deflection off edge

    // idea to handle ball movement, set angle of motion (theta) and speed
    // these should be sufficient to handle the ball vector
    public static double BOUNCER_THETA = Math.PI / 2;
    public static int BOUNCER_SPEED = 50;

    // some things needed to remember during game
    private Scene myScene;
    private ImageView myBouncer;
    private ImageView myPaddle;

    /**
     * Initialize what will be displayed and how it will be updated.
     */
    @Override
    public void start (Stage stage) {
        // attach scene to the stage and display it
        myScene = setupGame(SIZE, SIZE, BACKGROUND);
        stage.setScene(myScene);
        stage.setTitle(TITLE);
        stage.show();
        // attach "game loop" to timeline to play it (basically just calling step() method repeatedly forever)
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    // Create the game's "scene": what shapes will be in the game and their starting properties
    private Scene setupGame (int width, int height, Paint background) {
        // create one top level collection to organize the things in the scene
        Group root = new Group();
        // make some shapes and set their properties
        Image imageBounce = new Image(this.getClass().getClassLoader().getResourceAsStream(BOUNCER_IMAGE));
        Image imagePaddle = new Image(this.getClass().getClassLoader().getResourceAsStream(PADDLE_IMAGE));
        myBouncer = new ImageView(imageBounce);
        myPaddle = new ImageView(imagePaddle);
        // x and y represent the top left corner, so center it in window
        myBouncer.setX(width / 2 - myBouncer.getBoundsInLocal().getWidth() / 2);
        myBouncer.setY(height / 2 - myBouncer.getBoundsInLocal().getHeight() / 2);
        myPaddle.setX(width / 2 - myPaddle.getBoundsInLocal().getWidth() / 2);
        myPaddle.setY(height - myPaddle.getBoundsInLocal().getHeight() / 2 - PADDLE_FLOAT);

        root.getChildren().add(myBouncer);
        root.getChildren().add(myPaddle);
        // create a place to see the shapes
        Scene scene = new Scene(root, width, height, background);
        // respond to input
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        return scene;
    }

    // Change properties of shapes in small ways to animate them over time
    // Note, there are more sophisticated ways to animate shapes, but these simple ways work fine to start
    private void step (double elapsedTime) {
        // update "actors" attributes
        myBouncer.setY(myBouncer.getY() + BOUNCER_SPEED * elapsedTime * Math.sin(BOUNCER_THETA));
        myBouncer.setX(myBouncer.getX() + BOUNCER_SPEED * elapsedTime * Math.cos(BOUNCER_THETA));

        // check for collisions
        // with shapes, can check precisely
        // NEW Java 10 syntax that simplifies things (but watch out it can make code harder to understand)
        // var intersection = Shape.intersect(myMover, myGrower);

        /*
        Shape intersection = Shape.intersect(myMover, myBouncer);
        if (intersection.getBoundsInLocal().getWidth() != -1) {
            myMover.setFill(HIGHLIGHT);
        }
        else {
            myMover.setFill(MOVER_COLOR);
        }
        */
        // with images can only check bounding box
        if (myPaddle.getBoundsInParent().intersects(myBouncer.getBoundsInParent())) {
            paddleDeflect();
        }

    }

    // Handle ball deflect off of paddle
    // Maintain polar coordinates to determine new direction init og ball
    // TODO: figure out paddle deflection angular system
    private void paddleDeflect() {
        double paddleX = myPaddle.getX();
        double bouncerX = myBouncer.getX();
        double paddleSize = myPaddle.getFitWidth();
        double paddleCenter = paddleX - paddleSize;
        // BOUNCER_THETA = (paddleCenter - bouncerX)/(paddleCenter - paddleX)*PADDLE_EDGE;
        BOUNCER_THETA = BOUNCER_THETA * -1;
    }

    // What to do each time a key is pressed
    private void handleKeyInput (KeyCode code) {
        if (code == KeyCode.RIGHT) {
            myPaddle.setX(myPaddle.getX() + PADDLE_SPEED);
        }
        else if (code == KeyCode.LEFT) {
            myPaddle.setX(myPaddle.getX() - PADDLE_SPEED);
        }
        // NEW Java 12 syntax that some prefer (but watch out for the many special cases!)
        //   https://blog.jetbrains.com/idea/2019/02/java-12-and-intellij-idea/
        // Note, must set Project Language Level to "13 Preview" under File -> Project Structure
        // switch (code) {
        //     case RIGHT -> myMover.setX(myMover.getX() + MOVER_SPEED);
        //     case LEFT -> myMover.setX(myMover.getX() - MOVER_SPEED);
        //     case UP -> myMover.setY(myMover.getY() - MOVER_SPEED);
        //     case DOWN -> myMover.setY(myMover.getY() + MOVER_SPEED);
        // }
    }

    /**
     * Start the program.
     */
    public static void main (String[] args) {
        launch(args);
    }
}
