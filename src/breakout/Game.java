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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * The purpose of this class is to drive Game generation with associated specs, implementing JavaFX library
 * Refactored for Code Masterpiece, all functionality has been refactored and generalized
 * Naming conventions are all standard and complete, casting and instanceof validation is checked as well
 * Furthermore, redundancies in game flow have been refactored (createLevel call creates Bouncers, Bricks, etc.)
 * Main game engine to process game rules
 * Stores a group of all objects (bouncers, walls, bricks, splash displays)
 * Set metadata on top to configure game set up, then run
 * @author Max Smith
 */
public class Game extends Application {
    // Game metadata
    private final String TITLE = "Breakout Game JavaFX";
    private final int SCREEN_WIDTH = 400;
    private final int SCREEN_HEIGHT = 400;
    private final int DISPLAY_HEIGHT = 50;
    private int LIVES_LEFT = 3;
    private int LIVES_AT_LEVEL_START;
    private static int POINTS_SCORED = 0;
    private int LEVEL = 1;
    private int MAX_LEVEL = 20;

    // Game play metadata
    private final int FRAMES_PER_SECOND = 120;
    private final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    private final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    private final Paint BACKGROUND = Color.AZURE;

    // some things needed to remember during the game
    private Scene myScene;
    private Paddle myPaddle;
    private SplashMenu mySplash;

    private Group wallGroup = new Group();
    private Group bouncerGroup = new Group();
    private Group brickGroup = new Group();
    private Group displayGroup = new Group();

    /**
     * Initialize what will be displayed and how it will be updated.
     */
    @Override
    public void start (Stage stage) {
        myScene = setupGame();
        stage.setScene(myScene);
        stage.setTitle(TITLE);
        stage.show();
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step());
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    private Scene setupGame () {
        Group root = new Group();
        initializeRoot(root);
        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT + DISPLAY_HEIGHT, BACKGROUND);
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        return scene;
    }

    private void initializeRoot(Group root) {
        createSplash();
        createPaddle();
        createAllWalls();
        createBouncer();
        createDisplay();

        addObjectToRoot(root, mySplash);
        addObjectToRoot(root, myPaddle);
        addToRoot(root, wallGroup);
        addToRoot(root, bouncerGroup);
        addToRoot(root, brickGroup);
        addToRoot(root, displayGroup);

        ((Bouncer) bouncerGroup.getChildren().get(0)).stickBouncerOnPaddle(myPaddle);
        // scanRoot(root);
    }

    private void scanRoot(Group root) {
        for (Node n: root.getChildren()) {
            System.out.println("node: " + n.getClass());
            if (n instanceof Group) {
                scanRoot((Group)n);
            }
        }
    }

    // Play game while splash is not dispalyed
    private void step() {
        DisplayMenu.updateFullDisplay(displayGroup, LIVES_LEFT, LEVEL, POINTS_SCORED);
        if (!mySplash.isShowing()) {
            moveBouncer();
            Collision.collisionDetection(bouncerGroup, brickGroup,  wallGroup, myPaddle);
            checkOutOfBounds();
            checkBouncersLeft();
            checkLevelClear();
        }
    }

    /**
     * Scene is governed by groups of objects (e.g. bouncers and bricks)
     * In other methods, nodes to remove are accumulated then removed here
     * Helps avoid concurrent modification exception when iterating through collection
     * @param destroyMe collection of nodes to destroy
     * @param group group to destroy nodes from
     */
    public static void removeNodes(Collection<Node> destroyMe, Group group) {
        if (!destroyMe.isEmpty()) {
            for (Node n: destroyMe) {
                if (n instanceof Brick) {
                    Brick k = (Brick) n;
                    POINTS_SCORED += k.getOriginalPower();
                }
                group.getChildren().remove(n);
            }
        }
    }

    private void addToRoot(Group root, Group addMe) {
        root.getChildren().add(addMe);
    }

    private void addObjectToRoot(Group root, Node n) {
        root.getChildren().add(n);
    }

    private void createSplash() {
        mySplash = new SplashMenu(SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    // (int width, int height, int heightOffset, double widthOffset, int data, String label) {
    private void createDisplay() {
        displayGroup.getChildren().add(new DisplayMenu(SCREEN_WIDTH, SCREEN_HEIGHT, 10, 0.05, LIVES_LEFT, "LIVES"));
        displayGroup.getChildren().add(new DisplayMenu(SCREEN_WIDTH, SCREEN_HEIGHT, 10, 0.4, LEVEL, "LEVEL"));
        displayGroup.getChildren().add(new DisplayMenu(SCREEN_WIDTH, SCREEN_HEIGHT, 10, 0.75, POINTS_SCORED, "POINTS"));
    }

    private void createPaddle() {
        myPaddle = new Paddle(SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    private void createAllWalls() {
        wallGroup.getChildren().addAll(Wall.createAllWalls(SCREEN_WIDTH, SCREEN_HEIGHT));
    }

    private void createBouncer() {
        clearGroup(bouncerGroup);
        Bouncer b = new Bouncer();
        b.stickBouncerOnPaddle(myPaddle);
        bouncerGroup.getChildren().add(b);
    }

    private void clearGroup(Group group) {group.getChildren().clear();}

    private void createBricks() {
        clearGroup(brickGroup);
        double[] wallBounds = Wall.getWallBounds(wallGroup);
        double paddleBound = myPaddle.getPaddleBound();
        LIVES_AT_LEVEL_START = LIVES_LEFT;
        String fileName = "resources/level_" + LEVEL + ".txt";
        brickGroup.getChildren().addAll(Brick.createAllBricks(wallBounds, paddleBound, fileName));
    }

    private void checkOutOfBounds() {
        Collection<Node> removeBouncer = new ArrayList<Node>();
        for (Node n: bouncerGroup.getChildren()) {
            if (n instanceof Bouncer) {
                Bouncer b = (Bouncer) n;
                if (b.getCenterY() > (myPaddle.getY())) {
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

    private void checkLevelClear() {
        if (brickGroup.getChildren().isEmpty()) {
            LEVEL ++;
            if (LEVEL > MAX_LEVEL) {
                gameWin();
            } else {
                createLevel();
            }
        }
    }

    private void moveBouncer() {
        for (Node n: bouncerGroup.getChildren()) {
            if (n instanceof Bouncer) {
                Bouncer b = (Bouncer) n;
                b.move(SECOND_DELAY);
            }
        }
    }

    private void createLevel() {
        try {
            new LevelGenerator(LEVEL);
        } catch (IOException e) {
            e.printStackTrace();
        }
        clearGroup(bouncerGroup);
        createBricks();
        createBouncer();
    }

    private void jumpToLevel(int level) {
        if (level > MAX_LEVEL) {
            level = MAX_LEVEL;
        }
        LEVEL = level;
        createLevel();
    }

    private void restartLevel() {
        LIVES_LEFT = LIVES_AT_LEVEL_START;
        createLevel();
    }

    private void gameOver() {
        mySplash.toggleSplash();
        mySplash.setText("GAME OVER!!");
    }

    private void gameWin() {
        mySplash.toggleSplash();
        mySplash.setText("GAME WIN!!");
    }

    private void sideKeyPress(int direct) {
        myPaddle.sideKey(direct);
        for (Node n: bouncerGroup.getChildren()) {
            if (n instanceof Bouncer) {
                Bouncer b  = (Bouncer) n;
                if (b.getBouncerStuck()) {
                    b.setCenterX(b.getCenterX() + direct * myPaddle.getSpeed());
                }
            }
        }
    }

    private void spaceKeyPress() {
        for (Node n: bouncerGroup.getChildren()) {
            if (n instanceof Bouncer) {
                Bouncer b  = (Bouncer) n;
                if (b.getBouncerStuck()) {
                    b.releaseBall();
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
        } else if (code == KeyCode.R) {
            restartLevel();
        } else if (code == KeyCode.S) {
            myPaddle.toggleSticky();
        } else if (code == KeyCode.E) {
            myPaddle.extend(1);
        } else if (code == KeyCode.F) {
            myPaddle.extend(0.5);
        } else if (code == KeyCode.A) {
            myPaddle.speedPaddle();
        } else if (code == KeyCode.W){
            Bouncer.slowBouncers(bouncerGroup);
        } else if (code == KeyCode.X) {
            Bouncer.addBouncers(bouncerGroup);
        } else if (code == KeyCode.ENTER) {
            mySplash.toggleSplash();
            jumpToLevel(LEVEL);
        } else if (code == KeyCode.DIGIT2) {
            jumpToLevel(2);
        } else if (code == KeyCode.DIGIT3) {
            jumpToLevel(3);
        } else if (code == KeyCode.DIGIT4) {
            jumpToLevel(4);
        } else if (code == KeyCode.DIGIT5) {
            jumpToLevel(5);
        } else if (code == KeyCode.DIGIT6) {
            jumpToLevel(6);
        } else if (code == KeyCode.DIGIT7) {
            jumpToLevel(7);
        } else if (code == KeyCode.DIGIT8) {
            jumpToLevel(8);
        } else if (code == KeyCode.DIGIT9) {
            jumpToLevel(9);
        }
    }

    /**
     * Main method, start of program
     * @param args arguments into main metehod
     */
    public static void main (String[] args) {
        launch(args);
    }
}
