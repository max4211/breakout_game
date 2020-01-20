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

public class Brick extends Rectangle {

    // Brick constant metadata
    private static final double BRICK_STROKE_WIDTH = 0.05;

    // dynamic variables to update based on brick collisions
    private int BRICK_POWER = 1;
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

    public Brick(double x, double y, double width, double height, int power) {
        super(x, y, width, height);
        BRICK_COLORS = BrickStyler.styleBricks();
        this.setStrokeWidth(this.getWidth() * BRICK_STROKE_WIDTH);
        this.setStroke(Color.BLACK);
        this.BRICK_POWER = power;
        updateBrickColor();
    }

    public void assignPower(PowerUp p) {
        this.myPower = p;
    }

    public int getBrickPower() {
        return this.BRICK_POWER;
    }

    public boolean hitBrick(int damage) {
        this.BRICK_POWER -= damage;
        updateBrickColor();
        return (this.BRICK_POWER < 1);
    }

    public void updateBrickColor() {
        if (this.BRICK_POWER < 1) {
            this.setFill(Color.TRANSPARENT);
            this.setStroke(Color.TRANSPARENT);
        } else {
            this.setFill(BRICK_COLORS.get(this.BRICK_POWER));
        }
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

    public Paint getBrickColor() {
        return this.getFill();
    }
}
