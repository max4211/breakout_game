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
import javafx.util.Duration;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Math.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
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
    private static int LIVES_AT_LEVEL_START;
    private static int POINTS_SCORED = 0;
    private int LEVEL = 1;
    private int MAX_LEVEL = 3;

    // Game play metadata
    private static final int FRAMES_PER_SECOND = 120;
    private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    private static final Paint BACKGROUND = Color.AZURE;

    // Testing  brick creation
    private static String BRICK_FIELD_TEXT;

    // Brick metadata read from file
    private int BRICKS_PER_ROW;
    private int ROWS_OF_BRICKS;

    // dynamic variable sizes to configure at start of level
    private double BRICK_HEIGHT;
    private double BRICK_WIDTH;

    // dynamic variable locations calculated from file config
    private double BRICK_START_X;
    private double BRICK_START_Y;

    // Brick padding (as percent values)
    private static final double BRICK_TOP_PAD = 0.20;
    private static final double BRICK_BOTTOM_PAD = 0.60;
    private static final double BRICK_RIGHT_PAD = 0.00;
    private static final double BRICK_LEFT_PAD = 0.00;

    // some things needed to remember during the game
    private Scene myScene;
    private Paddle myPaddle;

    // TODO: Updating elements to become groups
    private Group wallGroup = new Group();
    private Group bouncerGroup = new Group();
    private Group brickGroup = new Group();
    private Group textGroup = new Group();

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
        root = initializeRoot(root);

        // create a place to see the shapes and respond to input
        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT, BACKGROUND);
        // Scene scene = new Scene(root);
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        return scene;
    }

    private Group initializeRoot(Group root) {
        createPaddle();
        createAllWalls();
        createBouncer();
        createBricks();

        // root.getChildren().add(myPaddle);
        root = addPaddleToRoot(root);
        root = addToRoot(root, wallGroup);
        root = addToRoot(root, bouncerGroup);
        root = addToRoot(root, brickGroup);

        resetBouncer((Bouncer) bouncerGroup.getChildren().get(0));
        // scanRoot(root);
        return root;
    }

    private void scanRoot(Group root) {
        for (Node n: root.getChildren()) {
            System.out.println("node: " + n.getClass());
            if (n instanceof Group) {
                scanRoot((Group)n);
            }
        }
    }

    // Change properties of shapes in small ways to animate them over time
    private void step(double elapsedTime) {
        // update "actors" attributes
        moveBouncers(elapsedTime);
        Collision.collisionDetection(bouncerGroup, brickGroup,  wallGroup, myPaddle);
        outOfBoundsDetection();
        checkBouncersLeft();
        checkLevelClear();
    }

    public static void removeNodes(Collection<Node> destroyMe, Group group) {
        if (!destroyMe.isEmpty()) {
            for (Node n: destroyMe) {
                group.getChildren().remove(n);
            }
        }
    }

    private Group addToRoot(Group root, Group addMe) {
        root.getChildren().add(addMe);
        return root;
    }

    private Group addPaddleToRoot(Group root) {
        root.getChildren().add(myPaddle);
        return root;
    }

    private void createPaddle() {
        myPaddle = new Paddle(SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    private void createAllWalls() {
        for (Wall w: Wall.createAllWalls()) {wallGroup.getChildren().add(w);}
    }

    private void createBouncer() {
        Bouncer b = new Bouncer();
        resetBouncer(b);
        bouncerGroup.getChildren().add(b);
    }

    private void createBricks() {
        System.out.println("STARTING LEVEL: " + LEVEL);
        LIVES_AT_LEVEL_START = LIVES_LEFT;
        double[] wallBounds = Wall.getWallBounds(wallGroup);
        double paddleBound = getPaddleBound();
        BRICK_FIELD_TEXT = "resources/level_" + LEVEL + ".txt";
        Collection<Brick> myBricks = createAllBricks(wallBounds, paddleBound, BRICK_FIELD_TEXT);
        for (Brick k: myBricks) {brickGroup.getChildren().add(k);}
    }

    private double getPaddleBound() {
        return myPaddle.getBoundsInLocal().getMinY();
    }

    private void resetBouncer(Bouncer b) {
        b.setCenterX(myPaddle.getX() + myPaddle.getWidth() / 2);
        b.setCenterY(myPaddle.getY() - b.getRadius());
        b.setBouncerStuck(true);
        b.setBouncerSpeed(0);
    }

    private void displayLives() {
        System.out.println("LIVES: " + LIVES_LEFT);
    }

    private void outOfBoundsDetection() {
        Collection<Node> removeBouncer = new ArrayList<Node>();
        for (Node n: bouncerGroup.getChildren()) {
            if (n instanceof Bouncer) {
                Bouncer b = (Bouncer) n;
                if (b.getCenterY() >= SCREEN_HEIGHT) {
                    removeBouncer.add(n);
                }
            }
        }
        removeNodes(removeBouncer, bouncerGroup);
    }

    private void checkBouncersLeft() {
        if ((bouncerGroup.getChildren().isEmpty())) {
            LIVES_LEFT --;
            if (LIVES_LEFT < 1) {
                gameOver();
            } else {
                createBouncer();
            }
        }
    }

    private void gameOver() {
        System.out.println("GAME OVER!!");
        System.exit(0);
    }

    private void gameWin() {
        System.out.println("GAME WIN!!");
        System.exit(0);
    }

    private void checkLevelClear() {
        if (brickGroup.getChildren().isEmpty()) {
            System.out.println("LEVEL " + LEVEL + " CLEARED!!");
            LEVEL ++;
            if (LEVEL > MAX_LEVEL) {
                gameWin();
            }
            Bouncer.clearBouncers(bouncerGroup);
            createBricks();
            createBouncer();
        }
    }

    private void moveBouncers(double elapsedTime) {
        for (Node n: bouncerGroup.getChildren()) {
            if (n instanceof Bouncer) {
                Bouncer b = (Bouncer) n;
                b.setCenterY(b.getCenterY() + b.getBouncerSpeed() * elapsedTime * Math.sin(b.getBouncerTheta()));
                b.setCenterX(b.getCenterX() + b.getBouncerSpeed() * elapsedTime * Math.cos(b.getBouncerTheta()));
            }
        }
    }

    private void sideKeyPress(int direct) {
        myPaddle.setX(myPaddle.getX() + direct * myPaddle.getPaddleSpeed());
        for (Node n: bouncerGroup.getChildren()) {
            if (n instanceof Bouncer) {
                Bouncer b  = (Bouncer) n;
                if (b.getBouncerStuck()) {
                    b.setCenterX(b.getCenterX() + direct * myPaddle.getPaddleSpeed());
                }
            }
        }
    }

    private void spaceKeyPress() {
        for (Node n: bouncerGroup.getChildren()) {
            if (n instanceof Bouncer) {
                Bouncer b  = (Bouncer) n;
                if (b.getBouncerStuck()) {
                    b.setBouncerStuck(false);
                    b.setBouncerSpeed(b.getBouncerNormalSpeed());
                }
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
        } else if (code == KeyCode.L) {
            LIVES_LEFT ++;
            displayLives();
        } else if (code == KeyCode.R) {
            restartLevel();
        } else if (code == KeyCode.NUMPAD2) {
            jumpToLevel(2);
        } else if (code == KeyCode.NUMPAD3) {
            jumpToLevel(3);
        }
    }

    private void jumpToLevel(int level) {
        System.out.println("JUMPING TO LEVEL: " + level);
        LEVEL = level;
        restartLevel();
    }

    private void restartLevel() {
        LIVES_LEFT = LIVES_AT_LEVEL_START;
        System.out.println("RESTARTING LEVEL!!");
        createBricks();
        Bouncer.clearBouncers(bouncerGroup);
        createBouncer();
    }

    public Collection<Brick> createAllBricks(double[] wallBounds, double paddleBound, String brickFieldText) {
        Collection<Brick> allBricks = new CopyOnWriteArrayList<>();
        int brickRow = 0;
        try {
            Scanner scan = new Scanner(new File(brickFieldText));
            while (scan.hasNextLine()) {
                String[] dataSplit = scan.nextLine().split(" ");
                for (String s: dataSplit) {
                    if (s.equals("BRICKS_PER_ROW")) {
                        BRICKS_PER_ROW = Integer.parseInt(dataSplit[1]);
                    } else if (s.equals("ROWS_OF_BRICKS")) {
                        ROWS_OF_BRICKS = Integer.parseInt(dataSplit[1]);
                    } else {
                        if (brickRow == 0) {calculateBrickDimensions(wallBounds, paddleBound);}
                        allBricks.addAll(createBrickList(dataSplit, brickRow));
                        brickRow ++;
                    }
                    break;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("ERROR: " + e);
        }
        return allBricks;
    }

    private Collection<Brick> createBrickList(String[] dataSplit, int brickRow) {
        Collection<Brick> brickList = new ArrayList<Brick>();
        for (int i = 0; i < dataSplit.length; i ++) {
            int power = Integer.parseInt(dataSplit[i]);
            if (power > 0) {
                brickList.add(new Brick(BRICK_START_X + (BRICK_WIDTH * i), BRICK_START_Y + (BRICK_HEIGHT * brickRow), BRICK_WIDTH, BRICK_HEIGHT, power));
            }
        }
        return brickList;
    }

    private void calculateBrickDimensions(double[] wallBounds, double paddleBound) {
        double horizontalSpace = wallBounds[1] - wallBounds[0];
        double verticalSpace = paddleBound - wallBounds[2];
        BRICK_WIDTH = (horizontalSpace) * (1 - (BRICK_LEFT_PAD + BRICK_RIGHT_PAD)) / BRICKS_PER_ROW;
        BRICK_HEIGHT = (verticalSpace) * (1 - (BRICK_TOP_PAD + BRICK_BOTTOM_PAD)) / ROWS_OF_BRICKS;
        BRICK_START_X = horizontalSpace * BRICK_LEFT_PAD + wallBounds[0];
        BRICK_START_Y = verticalSpace * BRICK_TOP_PAD + wallBounds[2];
    }

    public static void main (String[] args) {
        launch(args);
    }
}
