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

/**
 * Messing around with JFX
 * Code base attributed to Robert C. Duval
 * @author Robert C. Duvall
 * @author Max Smith
 */
public class Main extends Application {
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

    // Brick metadata
    public static final int BRICK_ROWS = 25;
    public static final int BRICKS_PER_ROW = 10;
    public static final double BRICK_STROKE_WIDTH = 0.05;
    public static final Paint BRICK_STROKE_COLOR = Color.BLACK;

    // TODO: implement brick padding
    public static final int BRICK_TOP_PAD = 25;
    public static final int BRICK_FLOOR_PAD = 40;
    public static final int BRICK_RIGHT_PAD = 15;
    public static final int BRICK_LEFT_PAD = 15;

    // Wall metadata
    public static final int WALL_FLOAT = 0;
    public static final int WALL_WIDTH = 10;
    public static final Paint WALL_FILL = Color.BLACK;

    // Bouncer metadata
    public static double BOUNCER_THETA = Math.PI / 2;
    public static int BOUNCER_NORMAL_SPEED = 240;
    public static int BOUNCER_SPEED = BOUNCER_NORMAL_SPEED;
    public static int BOUNCER_RADIUS = 8;
    public static boolean BOUNCER_STUCK = false;
    public static final Paint BOUNCER_COLOR = Color.GOLD;

    // dynamic variable sizes to configure at start of game
    public static double BRICK_HEIGHT;
    public static double BRICK_WIDTH;
    public static final Paint BRICK_COLOR = Color.RED;

    // some things needed to remember during game
    private Scene myScene;
    private Circle myBouncer;
    private Rectangle myPaddle;
    private Rectangle myLeftWall;
    private Rectangle myRightWall;
    private Rectangle myTopWall;
    private Rectangle myBrick;

    private Collection<Wall> allWalls = new ArrayList<Wall>();
    private Collection<Bouncer> allBouncers = new ArrayList<Bouncer>();
    private Collection<Brick> allBricks = new ArrayList<Brick>();

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
        createBouncer();
        createAllWalls();
        createBrick();

        // append all  shapes to root
        root.getChildren().add(myBouncer);
        root.getChildren().add(myPaddle);

        // root.getChildren().add(allWalls);
        // for (Wall w: allWalls) { root.getChildren().add(w); }

        root.getChildren().add(myLeftWall);
        root.getChildren().add(myRightWall);
        root.getChildren().add(myTopWall);
        root.getChildren().add(myBrick);

        // create a place to see the shapes
        Scene scene = new Scene(root);

        // respond to input
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        return scene;
    }

    private void createAllWalls() {
        myLeftWall = createRectangle(WALL_FLOAT, WALL_FLOAT, WALL_WIDTH, SCREEN_HEIGHT - WALL_FLOAT);
        myRightWall = createRectangle(SCREEN_WIDTH - 2 * WALL_FLOAT, WALL_FLOAT, WALL_WIDTH, SCREEN_HEIGHT - WALL_FLOAT);
        myTopWall = createRectangle(WALL_FLOAT, WALL_FLOAT, SCREEN_WIDTH - 3 * WALL_FLOAT, WALL_WIDTH);
        allWalls.add(new Wall(myLeftWall));
        allWalls.add(new Wall(myRightWall));
        allWalls.add(new Wall(myTopWall));
    }

    // TODO: Incorporate padding to decrease dimensions
    private void setBrickDimensions() {
        double leftX = myLeftWall.getX() + myLeftWall.getWidth();
        double rightX = myRightWall.getX();
        double bottomY = myPaddle.getY();
        double topY = myTopWall.getY() + myTopWall.getHeight();
        BRICK_WIDTH = (rightX - leftX) / BRICKS_PER_ROW;
        BRICK_HEIGHT = (bottomY - topY) / BRICK_ROWS;
    }

    private void createBrick() {
        setBrickDimensions();
        myBrick = new Rectangle();
        myBrick.setX(SCREEN_WIDTH / 2 - myBrick.getBoundsInLocal().getWidth() / 2);
        myBrick.setY(SCREEN_HEIGHT / 2 - myBrick.getBoundsInLocal().getHeight() / 2);
        myBrick.setWidth(BRICK_WIDTH);
        myBrick.setHeight(BRICK_HEIGHT);
        myBrick.setFill(BRICK_COLOR);
        myBrick.setStrokeWidth(myBrick.getWidth()*BRICK_STROKE_WIDTH);
        myBrick.setStroke(BRICK_STROKE_COLOR);
    }

    private void createPaddle() {
        myPaddle = new Rectangle();
        myPaddle.setX(SCREEN_WIDTH / 2 - myPaddle.getBoundsInLocal().getWidth() / 2);
        myPaddle.setY(SCREEN_HEIGHT - myPaddle.getBoundsInLocal().getHeight() / 2 - PADDLE_FLOAT);
        myPaddle.setWidth(PADDLE_WIDTH);
        myPaddle.setHeight(PADDLE_HEIGHT);
        myPaddle.setFill(PADDLE_COLOR);
    }

    private void createBouncer() {
        myBouncer = new Circle();
        myBouncer.setRadius(BOUNCER_RADIUS);
        myBouncer.setFill(BOUNCER_COLOR);
        setBallOnPaddle();
    }

    private Rectangle createRectangle(int x, int y, int width, int height) {
        return new Rectangle (x, y, width, height);
    }

    private void setBallOnPaddle() {
        myBouncer.setCenterX(myPaddle.getX() + myPaddle.getWidth() / 2);
        myBouncer.setCenterY(myPaddle.getY() - myBouncer.getRadius());
        BOUNCER_STUCK = true;
        BOUNCER_SPEED = 0;
    }

    private void releaseStuckBall() {
        BOUNCER_STUCK = false;
        BOUNCER_SPEED = BOUNCER_NORMAL_SPEED;
    }

    // Change properties of shapes in small ways to animate them over time
    // Note, there are more sophisticated ways to animate shapes, but these simple ways work fine to start
    private void step (double elapsedTime) {
        // update "actors" attributes
        myBouncer.setCenterY(myBouncer.getCenterY() + BOUNCER_SPEED * elapsedTime * Math.sin(BOUNCER_THETA));
        myBouncer.setCenterX(myBouncer.getCenterX() + BOUNCER_SPEED * elapsedTime * Math.cos(BOUNCER_THETA));
        checkCollision();
        checkInBounds();
    }

    private void checkInBounds() {
        if (myBouncer.getCenterY() > SCREEN_HEIGHT) {
            bouncerOffScreen();
        }
    }

    private void bouncerOffScreen() {
        LIVES_LEFT --;
        setBallOnPaddle();
        if (LIVES_LEFT < 1) {
            gameOver();
        }
    }

    // TODO: Populate end game
    private void gameOver() {

    }

    // TODO: Modify collision detection to SIDE of an object, deflect appropriately
    private void checkCollision() {
        double scale = 0; double shift = 1;
        if (shapeCollision(myBouncer, myPaddle)) {
            angleDeflect();
        } else if (shapeCollision(myBouncer, myTopWall)) {
            scale = -1; shift = 0;
        } else if (shapeCollision(myBouncer, myLeftWall)){
            scale = -1; shift = Math.PI;
        } else if (shapeCollision(myBouncer, myRightWall)) {
            scale = -1; shift = Math.PI;
        }
        if (scale != 0) {
            basicDeflect(scale, shift);
        }
    }

    private boolean shapeCollision(Shape s1, Shape s2) {
        return Shape.intersect(s1, s2).getBoundsInLocal().getWidth() != -1;
    }

    private void basicDeflect(double scale, double shift) {
        BOUNCER_THETA = BOUNCER_THETA * scale + shift;
    }

    private void angleDeflect() {
        double paddleX = myPaddle.getX();
        double bouncerX = myBouncer.getCenterX();
        double paddleWidth = myPaddle.getWidth();
        double paddleCenter = paddleX + paddleWidth / 2;
        BOUNCER_THETA = - Math.PI / 2 + 2 * (bouncerX - paddleCenter) * (PADDLE_EDGE / paddleWidth);
    }

    private void sideKeyPress(int direct, Rectangle wall) {
        if (!(shapeCollision(myPaddle, wall))) {
            myPaddle.setX(myPaddle.getX() + direct * PADDLE_SPEED);
            if (BOUNCER_STUCK) {
                myBouncer.setCenterX(myBouncer.getCenterX() + direct * PADDLE_SPEED);
            }
        }
    }

    private void handleKeyInput (KeyCode code) {
        if (code == KeyCode.RIGHT) {
            sideKeyPress(1, myRightWall);
        }
        else if (code == KeyCode.LEFT) {
            sideKeyPress(-1, myLeftWall);
        }
        if (code == KeyCode.SPACE && BOUNCER_STUCK) {
            releaseStuckBall();
        }
    }

    public static void main (String[] args) {
        launch(args);
    }
}
