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
import java.util.ArrayList;
import java.util.Collection;

/**
 * Main game engine to process game rules, calls additional classes
 * @author Max Smith
 */
public class Game extends Application {
    // Game metadata
    private static final String TITLE = "Breakout Game JavaFX";
    private static final int SCREEN_WIDTH = 400;
    private static final int SCREEN_HEIGHT = 400;
    private static final int DISPLAY_HEIGHT = 50;
    private int LIVES_LEFT = 3;
    private int LIVES_AT_LEVEL_START;
    private static int POINTS_SCORED = 0;
    private int LEVEL = 1;
    private int MAX_LEVEL = 2;

    // Game play metadata
    private static final int FRAMES_PER_SECOND = 120;
    private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    private static final double SECOND_DELAY = 1.0 / FRAMES_PER_SECOND;
    private static final Paint BACKGROUND = Color.AZURE;

    // Testing  brick creation
    private static String BRICK_FIELD_TEXT;

    // some things needed to remember during the game
    private Scene myScene;
    private Paddle myPaddle;
    private SplashMenu mySplash;

    // TODO: Updating elements to become groups
    private Group wallGroup = new Group();
    private Group bouncerGroup = new Group();
    private Group brickGroup = new Group();
    private Group displayGroup = new Group();
    private Group powerGroup = new Group();

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
        KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY), e -> step(SECOND_DELAY));
        Timeline animation = new Timeline();
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.getKeyFrames().add(frame);
        animation.play();
    }

    private Scene setupGame () {
        Group root = new Group();
        root = initializeRoot(root);
        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT + DISPLAY_HEIGHT, BACKGROUND);
        scene.setOnKeyPressed(e -> handleKeyInput(e.getCode()));
        return scene;
    }

    private Group initializeRoot(Group root) {
        createSplash();
        createPaddle();
        createAllWalls();
        createBouncer();
        createDisplay();

        root = addSplashToRoot(root);
        root = addPaddleToRoot(root);
        root = addToRoot(root, wallGroup);
        root = addToRoot(root, bouncerGroup);
        root = addToRoot(root, brickGroup);
        root = addToRoot(root, displayGroup);

        ((Bouncer) bouncerGroup.getChildren().get(0)).stickBouncerOnPaddle(myPaddle);
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

    // Play game while splash is not dispalyed
    private void step(double elapsedTime) {
        DisplayMenu.updateFullDisplay(displayGroup, LIVES_LEFT, LEVEL, POINTS_SCORED);
        if (!mySplash.isShowing()) {
            moveBouncer(elapsedTime);
            Collision.collisionDetection(bouncerGroup, brickGroup,  wallGroup, myPaddle);
            outOfBoundsDetection();
            checkBouncersLeft();
            checkLevelClear();
        }
    }

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

    private Group addToRoot(Group root, Group addMe) {
        root.getChildren().add(addMe);
        return root;
    }

    private Group addSplashToRoot(Group root) {
        root.getChildren().add(mySplash);
        return root;
    }

    private Group addPaddleToRoot(Group root) {
        root.getChildren().add(myPaddle);
        return root;
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
        for (Wall w: Wall.createAllWalls(SCREEN_WIDTH, SCREEN_HEIGHT)) {wallGroup.getChildren().add(w);}
    }

    private void createBouncer() {
        clearBouncer();
        Bouncer b = new Bouncer();
        b.stickBouncerOnPaddle(myPaddle);
        bouncerGroup.getChildren().add(b);
    }

    private void clearBouncer() {
        bouncerGroup.getChildren().clear();
    }

    private void clearBricks() {
        brickGroup.getChildren().clear();
    }

    private void createBricks() {
        clearBricks();
        double[] wallBounds = Wall.getWallBounds(wallGroup);
        double paddleBound = myPaddle.getPaddleBound();
        LIVES_AT_LEVEL_START = LIVES_LEFT;
        BRICK_FIELD_TEXT = "resources/level_" + LEVEL + ".txt";
        Collection<Brick> myBricks = Brick.createAllBricks(wallBounds, paddleBound, BRICK_FIELD_TEXT);
        for (Brick k: myBricks) {brickGroup.getChildren().add(k);}
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

    private void checkLevelClear() {
        if (brickGroup.getChildren().isEmpty()) {
            LEVEL ++;
            if (LEVEL > MAX_LEVEL) {
                gameWin();
            } else {
                Bouncer.clearBouncers(bouncerGroup);
                createBricks();
                createBouncer();
            }
        }
    }

    private void moveBouncer(double elapsedTime) {
        for (Node n: bouncerGroup.getChildren()) {
            if (n instanceof Bouncer) {
                Bouncer b = (Bouncer) n;
                b.move(elapsedTime);
            }
        }
    }

    private void createLevel() {
        createBricks();
        createBouncer();
    }

    private void jumpToLevel(int level) {
        LEVEL = level;
        createLevel();
    }

    private void restartLevel() {
        LIVES_LEFT = LIVES_AT_LEVEL_START;
        createBricks();
        Bouncer.clearBouncers(bouncerGroup);
        createBouncer();
    }

    private void gameOver() {
        mySplash.toggleSplash();
        mySplash.setText("GAME OVER!!");
        // System.exit(0);
    }

    private void gameWin() {
        mySplash.toggleSplash();
        mySplash.setText("GAME WIN!!");
        // System.exit(0);
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
            myPaddle.extend();
        } else if (code == KeyCode.A) {
            myPaddle.speedPaddle();
        } else if (code == KeyCode.Q) {
            // Debug code
        } else if (code == KeyCode.X) {
            Bouncer.addBouncers(bouncerGroup);
        } else if (code == KeyCode.DIGIT2) {
            jumpToLevel(2);
        } else if (code == KeyCode.DIGIT3) {
            jumpToLevel(3);
        } else if (code == KeyCode.DIGIT4) {
            jumpToLevel(4);
        } else if (code == KeyCode.DIGIT5) {
            jumpToLevel(5);
        } else if (code == KeyCode.ENTER) {
            mySplash.toggleSplash();
            jumpToLevel(LEVEL);
        }
    }

    public static void main (String[] args) {
        launch(args);
    }
}
