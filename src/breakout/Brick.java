package breakout;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class Brick extends Rectangle {

    private Rectangle myBrick;

    // Brick constant metadata
    private static final double BRICK_STROKE_WIDTH = 0.05;
    private static Paint BRICK_STROKE_COLOR = Color.BLACK;

    // Brick metadata read from file
    private static int BRICKS_PER_ROW;
    private static int ROWS_OF_BRICKS;

    // dynamic variable locations calculated from file config
    private static double BRICK_START_X;
    private static double BRICK_START_Y;

    // dynamic variable sizes to configure at start of level
    private static double BRICK_HEIGHT;
    private static double BRICK_WIDTH;

    // dynamic variables to update based on brick collisions
    private static int BRICK_POWER;
    private static Paint BRICK_COLOR = Color.RED;

    // Brick padding (as percent values)
    private static final double BRICK_TOP_PAD = 0.25;
    private static final double BRICK_BOTTOM_PAD = 0.40;
    private static final double BRICK_RIGHT_PAD = 0.15;
    private static final double BRICK_LEFT_PAD = 0.15;

    public Brick(double x, double y, int power) {
        myBrick = new Rectangle(x, y, BRICK_WIDTH, BRICK_HEIGHT);
        BRICK_POWER = power;
        myBrick.setFill(BRICK_COLOR);
        myBrick.setStrokeWidth(myBrick.getWidth() * BRICK_STROKE_WIDTH);
        myBrick.setStroke(BRICK_STROKE_COLOR);
    }

    public Rectangle getMyBrick() {
        return myBrick;
    }

    public int getBrickPower() {
        return BRICK_POWER;
    }

    public void hitBrick(int damage) {
        updateBrickPower(damage);
        updateBrickColor();
    }

    public void updateBrickPower(int damage) {
        BRICK_POWER -= damage;
        if (BRICK_POWER < 1) {
            destroyBrick();
        }
    }

    private void destroyBrick() {
        BRICK_COLOR = Color.TRANSPARENT;
        BRICK_STROKE_COLOR = Color.TRANSPARENT;
    }

    public void updateBrickColor() {
        BRICK_COLOR = Color.BLUE;
    }

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
        return allBricks;
    }

    private static void calculateBrickDimensions(double[] wallBounds, double paddleBound) {
        double horizontalSpace = wallBounds[1] - wallBounds[0];
        double verticalSpace = paddleBound - wallBounds[2];
        BRICK_WIDTH = (horizontalSpace) * (1 - (BRICK_LEFT_PAD + BRICK_RIGHT_PAD)) / BRICKS_PER_ROW;
        BRICK_HEIGHT = (verticalSpace) * (1 - (BRICK_TOP_PAD + BRICK_BOTTOM_PAD)) / ROWS_OF_BRICKS;
        BRICK_START_X = horizontalSpace * BRICK_LEFT_PAD;
        BRICK_START_Y = verticalSpace * BRICK_RIGHT_PAD;

        /*
        for (double d: wallBounds) {System.out.println("wallBound: " + d);}
        System.out.println("paddleBound: " + paddleBound);
        System.out.println("verticalSpace: " + verticalSpace);
        System.out.println("horizontalSpace: " + horizontalSpace);
        System.out.println("BRICK_WIDTH: " + BRICK_WIDTH);
        System.out.println("BRICK_HEIGHT: " + BRICK_HEIGHT);
        System.out.println("BRICK_START_X: " + BRICK_START_X);
        System.out.println("BRICK_START_Y: " + BRICK_START_Y);
         */
    }

    private static Collection<Brick> createBrickList(String[] dataSplit, int brickRow) {
        Collection<Brick> brickList = new ArrayList<Brick>();
        for (int i = 0; i < dataSplit.length; i ++) {
            int power = Integer.parseInt(dataSplit[i]);
            if (power > 0) {
                brickList.add(new Brick(BRICK_START_X + (BRICK_WIDTH * i), BRICK_START_Y + (BRICK_HEIGHT * brickRow), power));
            }
        }
        return brickList;
    }

}
