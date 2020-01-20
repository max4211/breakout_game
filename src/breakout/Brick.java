package breakout;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.Map;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;

import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Brick, extension of rectangle (constructor creates as such)
 * Bricks have power, each bouncer hit reduces power by bouncer damage
 * Brick field is configured from LevelGenerator file BRICKS_PER_ROW and ROWS_OF_BRICKS field
 * Along with adjustments for vertical and horizontal padding (see last few methods)
 * @author Max Smith
 */
public class Brick extends Rectangle {

    // Brick constant metadata
    private static final double BRICK_STROKE_WIDTH = 0.03;

    // dynamic variables to update based on brick collisions
    private int BRICK_POWER = 1;
    private int BRICK_ORIGINAL_POWER;
    private static Map<Integer, javafx.scene.paint.Paint> BRICK_COLORS;

    // Brick metadata read from file
    private static int BRICKS_PER_ROW;
    private static int ROWS_OF_BRICKS;

    // dynamic variable sizes to configure at start of level
    private static double BRICK_HEIGHT;
    private static double BRICK_WIDTH;

    // dynamic variable locations calculated from file config
    private static double BRICK_START_X;
    private static double BRICK_START_Y;

    // Brick padding (as percent values)
    private static final double BRICK_TOP_PAD = 0.20;
    private static final double BRICK_BOTTOM_PAD = 0.60;
    private static final double BRICK_RIGHT_PAD = 0.00;
    private static final double BRICK_LEFT_PAD = 0.00;

    private PowerUp myPower;

    /**
     * Brick constructor, create new brick
     * @param x rectangle x position
     * @param y rectangle y position
     * @param width rectangle width
     * @param height rectangle height
     * @param power power of brick (initial)
     */
    public Brick(double x, double y, double width, double height, int power) {
        super(x, y, width, height);
        BRICK_COLORS = BrickStyler.styleBricks();
        this.setStrokeWidth(this.getWidth() * BRICK_STROKE_WIDTH);
        this.setStroke(Color.WHITE);
        this.BRICK_POWER = power;
        this.BRICK_ORIGINAL_POWER = power;
        updateBrickColor();
    }

    /**
     * Called in collision detection, updates brick power
     * @param damage from bouncer applied to brick
     * @return value returned to determine if brick is destroyed
     */
    public boolean hitBrick(int damage) {
        this.BRICK_POWER -= damage;
        updateBrickColor();
        return (this.BRICK_POWER < 1);
    }

    /**
     * Calculates additional points to add to score
     * @return total points scored from destroying brick
     */
    public int getOriginalPower() {
        return BRICK_ORIGINAL_POWER;
    }

    private void assignPower(PowerUp p) {
        this.myPower = p;
    }

    private void updateBrickColor() {
        Paint fill;
        if (this.BRICK_POWER < 1) {
            this.setFill(Color.TRANSPARENT);
            this.setStroke(Color.TRANSPARENT);
        } else {
            if (BRICK_COLORS.containsKey(this.BRICK_POWER)) {
                fill = BRICK_COLORS.get(this.BRICK_POWER);
            } else {
                fill = BRICK_COLORS.get(Math.random() * 9 + 1);
                BRICK_COLORS.put(this.BRICK_POWER, fill);
                this.setStroke(Color.BLACK);
            }
            this.setFill(fill);
        }
    }

    /**
     * Used in collision detection, helps avoid cases where bricks have been destroyed but not removed
     * Brick collision must be of real power
     * @return power of brick
     */
    public int getBrickPower() {
        return this.BRICK_POWER;
    }

    /**
     * Initializes brick group from overall dimensions, and text document layout
     * @param wallBounds shows left, right, top boundary of creation
     * @param paddleBound lower (upper Y) bound is paddle
     * @param brickFieldText text document showing brick arrangement
     * @return Collection of bricks to be added to brickGroup
     */
    public static Collection<Brick> createAllBricks(double[] wallBounds, double paddleBound, String brickFieldText) {
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
        return powerUpBricks(allBricks);
    }

    private static Collection<Brick> powerUpBricks(Collection<Brick> allBricks) {
        for (Brick b: allBricks) {
            b.assignPower(new PowerUp('X'));
        }
        return allBricks;
    }

    private static Collection<Brick> createBrickList(String[] dataSplit, int brickRow) {
        Collection<Brick> brickList = new ArrayList<Brick>();
        for (int i = 0; i < dataSplit.length; i ++) {
            int brick = Integer.parseInt(dataSplit[i]);
            if (brick > 0) {
                Brick b = new Brick(BRICK_START_X + (BRICK_WIDTH * i), BRICK_START_Y + (BRICK_HEIGHT * brickRow), BRICK_WIDTH, BRICK_HEIGHT, brick);
                brickList.add(b);
            }
        }

        return brickList;
    }

    private static void calculateBrickDimensions(double[] wallBounds, double paddleBound) {
        double horizontalSpace = wallBounds[1] - wallBounds[0];
        double verticalSpace = paddleBound - wallBounds[2];
        BRICK_WIDTH = (horizontalSpace) * (1 - (BRICK_LEFT_PAD + BRICK_RIGHT_PAD)) / BRICKS_PER_ROW;
        BRICK_HEIGHT = (verticalSpace) * (1 - (BRICK_TOP_PAD + BRICK_BOTTOM_PAD)) / ROWS_OF_BRICKS;
        BRICK_START_X = horizontalSpace * BRICK_LEFT_PAD + wallBounds[0];
        BRICK_START_Y = verticalSpace * BRICK_TOP_PAD + wallBounds[2];
    }
}
