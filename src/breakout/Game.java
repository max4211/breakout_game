package breakout;

import javafx.application.Application;
import javafx.stage.Stage;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import java.lang.Math.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * Messing around with JFX
 * Code base attributed to Robert C. Duval
 * @author Robert C. Duvall
 * @author Max Smith
 */
public class Game extends Application {
    // Game metadata
    public static final String TITLE = "Bounce Test JavaFX";
    public static final int SCREEN_WIDTH = 400;
    public static final int SCREEN_HEIGHT = 400;
    public static final int FRAMES_PER_SECOND = 120;
    public static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    public static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    public static final Paint BACKGROUND = Color.AZURE;
    public static int LIVES_LEFT = 3;

    // Paddle metadata
    public static final int PADDLE_HEIGHT = 10;
    public static final int PADDLE_SPEED = 10;
    public static final int PADDLE_FLOAT = 30;              // paddle pixel float above bottom of screen
    public static final double PADDLE_EDGE = Math.PI / 4;   // max paddle angular deflection off edge
    public static final Paint PADDLE_COLOR = Color.BLUEVIOLET;
    public static int PADDLE_WIDTH = 100;

    // Wall metadata
    public static final int WALL_FLOAT = 0;
    public static final int WALL_WIDTH = 10;
    public static final Paint WALL_FILL = Color.BLACK;

    // some things needed to remember during game
    private Scene myScene;
    private Paddle myPaddle;
    // private Collection<Paddle> allPaddles = new ArrayList<Paddle>();
    private Collection<Wall> allWalls = new ArrayList<Wall>();
    private Collection<Bouncer> allBouncers = new ArrayList<Bouncer>();

    /**
     * Initialize what will be displayed and how it will be updated.
     */
    @Override
    public void start (Stage stage) {
        // attach scene to the stage and display it
        myScene = setupGame();
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
    private Scene setupGame () {
        // create one top level collection to organize the things in the scene
        Group root = new Group();

        // make some shapes and set their properties
        createPaddle();
        createAllWalls();
        createBouncers();

        // add shapes of items to root
        root = addPaddleToRoot(root);
        root = addWallsToRoot(root);
        root = addBouncersToRoot(root);

        // create a place to see the shapes
        Scene scene = new Scene(root);
        // respond to input
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        return scene;
    }

    private void createPaddle() {
        // allPaddles.add(new Paddle(SCREEN_WIDTH, SCREEN_HEIGHT));
        myPaddle = new Paddle(SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    private void createAllWalls() {
        allWalls.add(new Wall(createRectangle(WALL_FLOAT, WALL_FLOAT, WALL_WIDTH, SCREEN_HEIGHT - WALL_FLOAT)));
        allWalls.add(new Wall(createRectangle(SCREEN_WIDTH - 2 * WALL_FLOAT, WALL_FLOAT, WALL_WIDTH, SCREEN_HEIGHT - WALL_FLOAT)));
        allWalls.add(new Wall(createRectangle(WALL_FLOAT, WALL_FLOAT, SCREEN_WIDTH - 3 * WALL_FLOAT, WALL_WIDTH)));
    }

    private void createBouncers() {
        allBouncers.add(new Bouncer());
    }

    private Rectangle createRectangle(int x, int y, int width, int height) {
        return new Rectangle (x, y, width, height);
    }

    // TODO: Refactor shape addition
    private Group addPaddleToRoot(Group root) {
        root.getChildren().add(myPaddle.getMyPaddle());
        return root;
    }

    private Group addWallsToRoot(Group root) {
        for (Wall w: allWalls) { root.getChildren().add(w.getMyWall()); }
        return root;
    }

    private Group addBouncersToRoot(Group root) {
        for (Bouncer b: allBouncers) {
            setBouncerOnPaddle(b, myPaddle);
            root.getChildren().add(b.getMyBouncer());
        }
        return root;
    }

    // Change properties of shapes in small ways to animate them over time
    private void step (double elapsedTime) {
        // update "actors" attributes
        moveBouncers(elapsedTime);
        collisionDetection();
    }

    // TODO: check all combinations of objects still in existence, verifying instance to determine reaction
    // TODO: Study getInBoundsLocal to figure out redirect
    private void collisionDetection() {
        double[] redirect = new double[2];
        for (Bouncer b: allBouncers) {
            if (shapeCollision(b.getMyBouncer(), myPaddle.getMyPaddle())) {
                angleDeflect(b, myPaddle);
            }
            for (Wall w: allWalls) {
                redirect = checkRectangleBouncerCollision(w.getMyWall(), b.getMyBouncer());
                if (redirect[0] != 0) {
                    basicDeflect(redirect, b);
                }
            }
        }
    }

    private void basicDeflect(double[] redirect, Bouncer b) {
        double scale = redirect[0]; double shift = redirect[1];
        b.BOUNCER_THETA = b.BOUNCER_THETA * scale + shift;
    }

    private double[] checkRectangleBouncerCollision(Rectangle r, Circle c) {
        double[] redirect = new double[2];
        if (checkBottomCollision(r, c)) {
            redirect[0] = -1; redirect[1] = 0;
            System.out.print("Bottom collision!!!");
        } else if (checkRightCollision(r, c)) {
            redirect[0] = -1; redirect[1] = Math.PI;
            System.out.print("Bottom collision!!!");
        } else if (checkLeftCollision(r, c)) {
            redirect[0] = -1; redirect[1] = Math.PI;
            System.out.print("Bottom collision!!!");
        } else {
            ;
        }
        return redirect;
    }

    // TODO: Implement refactored method (elastic) below
    private boolean checkElasticCollision(Rectangle r, Circle c, double xShift, double yShift) {
        return r.getBoundsInLocal().contains(c.getBoundsInLocal().getCenterX() + xShift, c.getBoundsInLocal().getCenterY() + yShift);
    }

    private boolean checkRightCollision(Rectangle r, Circle c) {
        return r.getBoundsInLocal().contains(c.getBoundsInLocal().getCenterX() + c.getRadius(), c.getBoundsInLocal().getCenterY());
    }

    private boolean checkLeftCollision(Rectangle r, Circle c) {
        return r.getBoundsInLocal().contains(c.getBoundsInLocal().getCenterX() - c.getRadius(), c.getBoundsInLocal().getCenterY());
    }

    private boolean checkBottomCollision(Rectangle r, Circle c) {
        return r.getBoundsInLocal().contains(c.getBoundsInLocal().getCenterX(), c.getBoundsInLocal().getCenterY() - c.getRadius());
    }

    private void angleDeflect(Bouncer b, Paddle p) {
        double paddleX = p.getMyPaddle().getX();
        double bouncerX = b.getMyBouncer().getCenterX();
        double paddleWidth = p.getMyPaddle().getWidth();
        double paddleCenter = paddleX + paddleWidth / 2;
        b.setBouncerTheta(- Math.PI / 2 + 2 * (bouncerX - paddleCenter) * (p.PADDLE_WIDTH / paddleWidth));
    }

    private boolean shapeCollision(Shape s1, Shape s2) {
        return Shape.intersect(s1, s2).getBoundsInLocal().getWidth() != -1;
    }

    private void setBouncerOnPaddle(Bouncer b, Paddle p) {
        b.getMyBouncer().setCenterX(p.getMyPaddle().getX() + p.getMyPaddle().getWidth() / 2);
        b.getMyBouncer().setCenterY(p.getMyPaddle().getY() - b.getMyBouncer().getRadius());
        b.setBouncerStuck(true);
        b.setBouncerSpeed(0);
    }

    private void moveBouncers(double elapsedTime) {
        for (Bouncer b: allBouncers) {
            b.getMyBouncer().setCenterY(b.getMyBouncer().getCenterY() + b.getBouncerSpeed() * elapsedTime * Math.sin(b.getBouncerTheta()));
            b.getMyBouncer().setCenterX(b.getMyBouncer().getCenterX() + b.getBouncerSpeed() * elapsedTime * Math.cos(b.getBouncerTheta()));
        }
    }

    private void sideKeyPress(int direct) {
        myPaddle.getMyPaddle().setX(myPaddle.getMyPaddle().getX() + direct * myPaddle.PADDLE_SPEED);
        for (Bouncer b: allBouncers ) {
            if (b.BOUNCER_STUCK) {
                b.getMyBouncer().setCenterX(b.getMyBouncer().getCenterX() + direct * myPaddle.PADDLE_SPEED);
            }
        }
    }

    private void spaceKeyPress() {
        for (Bouncer b: allBouncers ) {
            if (b.BOUNCER_STUCK) {
                b.setBouncerStuck(false);
                b.setBouncerSpeed(b.BOUNCER_NORMAL_SPEED);
            }
        }
    }

    private void handleKeyInput (KeyCode code) {
        if (code == KeyCode.RIGHT) {
            sideKeyPress(1);
        } else if (code == KeyCode.LEFT) {
            sideKeyPress(-1);
        } else if (code == KeyCode.SPACE) {
            spaceKeyPress();
        }
    }

    public static void main (String[] args) {
        launch(args);
    }
}
