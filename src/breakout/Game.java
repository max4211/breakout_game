package breakout;

import javafx.application.Application;
import javafx.scene.Node;
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
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Main game engine to process game rules, calls additional classes
 * @author Max Smith
 */
public class Game extends Application {
    // Game metadata
    private static final String TITLE = "Breakout Game JavaFX";

    // TODO: Update to private
    public static final int SCREEN_WIDTH = 400;
    public static final int SCREEN_HEIGHT = 400;
    private static int LIVES_LEFT = 3;

    // Game play metadata
    private static final int FRAMES_PER_SECOND = 120;
    private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    private static final Paint BACKGROUND = Color.AZURE;

    // Testing  brick creation
    private static final String BRICK_FIELD_TEXT = "resources/level_test.txt";

    // some things needed to remember during the game
    private Scene myScene;
    private Paddle myPaddle;
    private Collection<Wall> allWalls = new ArrayList<Wall>();
    private Collection<Bouncer> allBouncers = new ArrayList<Bouncer>();
    private Collection<Brick> allBricks = new CopyOnWriteArrayList<>();

    // TODO: Updating elements to become groups
    private Group wallGroup = new Group();
    private Group bouncerGroup = new Group();
    private Group brickGroup = new Group();

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
        // createBricks();

        root = addPaddleToRoot(root);
        root = addToRoot(root, wallGroup);
        root = addToRoot(root, bouncerGroup);
        // root = addToRoot(root, brickGroup);

        resetBouncer();

        // create a place to see the shapes and respond to input
        Scene scene = new Scene(root);
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        return scene;
    }


    // Change properties of shapes in small ways to animate them over time
    private void step (double elapsedTime) {
        // update "actors" attributes
        // moveBouncers(elapsedTime);
        // collisionDetection();
        // outOfBoundsDetection();
    }

    private Group addToRoot(Group root, Group addMe) {
        root.getChildren().add(addMe);
        return root;
    }

    private Group addPaddleToRoot(Group root) {
        root.getChildren().add(myPaddle.getMyPaddle());
        return root;
    }

    private void createPaddle() {
        myPaddle = new Paddle(SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    private void createAllWalls() {
        allWalls = Wall.createAllWalls();
        for (Wall w: allWalls) {wallGroup.getChildren().add(w.getMyWall());}
    }

    private void createBouncers() {
        allBouncers.add(new Bouncer());
        bouncerGroup.getChildren().add(new Bouncer().getMyBouncer());
    }

    private void createBricks() {
        double[] wallBounds = getWallBounds();
        double paddleBound = getPaddleBound();
        allBricks.addAll(Brick.createAllBricks(wallBounds, paddleBound, BRICK_FIELD_TEXT));
        for (Brick k: allBricks) {brickGroup.getChildren().add(k);}
    }

    private double getPaddleBound() {
        return myPaddle.getMyPaddle().getBoundsInLocal().getMinY();
    }

    // int: {left_bound, right_bound, top_bound}
    private double[] getWallBounds() {
        double[] wallBounds = new double[3];
        for (Node n: wallGroup.getChildren()) {
            if (n instanceof Wall) {
                Wall w = (Wall) n;
                if (w.getMyName().equals("LEFT")) {
                    wallBounds[0] = w.getMyWall().getBoundsInLocal().getMaxX();
                } else if (w.getMyName().equals("RIGHT")) {
                    wallBounds[1] = w.getMyWall().getBoundsInLocal().getMinX();
                } else if (w.getMyName().equals("TOP")) {
                    wallBounds[2] = w.getMyWall().getBoundsInLocal().getMaxY();
                }
            }
        }
        return wallBounds;
    }

    private void resetBouncer() {
        for (Node n: bouncerGroup.getChildren()) {
            System.out.println("bouncerGroup node class: " + n.getClass());
            if (n instanceof Circle) {
                System.out.println("node instance of bouncer, setting on paddle");
                Circle c = (Circle) n;
                c.setCenterX(myPaddle.getMyPaddle().getX() + myPaddle.getMyPaddle().getWidth() / 2);
                c.setCenterY(myPaddle.getMyPaddle().getY() - c.getRadius());
                // b.setBouncerStuck(true);
                // b.setBouncerSpeed(0);
            }
        }
    }

    // TODO: Implement out of bounds detection
    private void outOfBoundsDetection() {

    }

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
            for (Brick k: allBricks) {
                redirect = checkRectangleBouncerCollision(k.getMyBrick(), b.getMyBouncer());
                if (redirect[0] != 0) {
                    basicDeflect(redirect, b);
                    k.hitBrick(b.getBouncerDamage());
                }
            }
        }
    }

    private void basicDeflect(double[] redirect, Bouncer b) {
        double scale = redirect[0]; double shift = redirect[1];
        b.setBouncerTheta(b.getBouncerTheta() * scale + shift);
    }

    private double[] checkRectangleBouncerCollision(Rectangle r, Circle c) {
        double[] redirect = new double[2];
        if (checkBottomCollision(r, c)) {
            redirect[0] = -1; redirect[1] = 0;
        } else if (checkRightCollision(r, c)) {
            redirect[0] = -1; redirect[1] = Math.PI;
        } else if (checkLeftCollision(r, c)) {
            redirect[0] = -1; redirect[1] = Math.PI;
        } else if (checkTopCollision(r, c)){
            redirect[0] = -1; redirect[1] = 0;
        }
        return redirect;
    }

    // TODO: Implement refactored method (elastic) below - may want to keep b/c sides add good specificity
    // IDEA: Could refactor based on top/bottom, left right (only things that matter for polar shift
    private boolean checkElasticCollision(Rectangle r, Circle c, double xShift, double yShift) {
        return r.getBoundsInLocal().contains(c.getBoundsInLocal().getCenterX() + xShift, c.getBoundsInLocal().getCenterY() + yShift);
    }

    private boolean checkRightCollision(Rectangle r, Circle c) {
        return r.getBoundsInLocal().contains(c.getBoundsInLocal().getCenterX() + c.getRadius(), c.getBoundsInLocal().getCenterY());
    }

    private boolean checkLeftCollision(Rectangle r, Circle c) {
        return r.getBoundsInLocal().contains(c.getBoundsInLocal().getCenterX() - c.getRadius(), c.getBoundsInLocal().getCenterY());
    }

    private boolean checkTopCollision(Rectangle r, Circle c) {
        return r.getBoundsInLocal().contains(c.getBoundsInLocal().getCenterX(), c.getBoundsInLocal().getCenterY() + c.getRadius());
    }

    private boolean checkBottomCollision(Rectangle r, Circle c) {
        return r.getBoundsInLocal().contains(c.getBoundsInLocal().getCenterX(), c.getBoundsInLocal().getCenterY() - c.getRadius());
    }

    private void angleDeflect(Bouncer b, Paddle p) {
        double paddleX = p.getMyPaddle().getX();
        double bouncerX = b.getMyBouncer().getCenterX();
        double paddleWidth = p.getMyPaddle().getWidth();
        double paddleCenter = paddleX + paddleWidth / 2;
        b.setBouncerTheta(- Math.PI / 2 + 2 * (bouncerX - paddleCenter) * (p.getPaddleEdge() / paddleWidth));
    }

    private boolean shapeCollision(Shape s1, Shape s2) {
        return Shape.intersect(s1, s2).getBoundsInLocal().getWidth() != -1;
    }

    private void moveBouncers(double elapsedTime) {
        for (Node n: bouncerGroup.getChildren()) {
            if (n instanceof Bouncer) {
                Bouncer b = (Bouncer) n;
                b.getMyBouncer().setCenterY(b.getMyBouncer().getCenterY() + b.getBouncerSpeed() * elapsedTime * Math.sin(b.getBouncerTheta()));
                b.getMyBouncer().setCenterX(b.getMyBouncer().getCenterX() + b.getBouncerSpeed() * elapsedTime * Math.cos(b.getBouncerTheta()));
            }
        }
    }

    private void sideKeyPress(int direct) {
        myPaddle.getMyPaddle().setX(myPaddle.getMyPaddle().getX() + direct * myPaddle.getPaddleSpeed());
        for (Node n: bouncerGroup.getChildren()) {
            if (n instanceof Circle) {
                Circle c = (Circle) n;
                if (true) {
                    c.setCenterX(c.getCenterX() + direct * myPaddle.getPaddleSpeed());
                }
            }
        }
    }

    private void spaceKeyPress() {
        for (Bouncer b: allBouncers ) {
            if (b.getBouncerStuck()) {
                b.setBouncerStuck(false);
                b.setBouncerSpeed(b.getBouncerNormalSpeed());
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
