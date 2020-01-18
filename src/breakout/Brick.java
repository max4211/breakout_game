package breakout;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Brick {

    private Rectangle myBrick;

    // Brick constant metadata
    private static final double BRICK_STROKE_WIDTH = 0.05;
    private static final Paint BRICK_STROKE_COLOR = Color.BLACK;

    // Brick metadata read from file
    private static int BRICK_ROWS;
    private static int BRICKS_PER_ROW;

    // dynamic variable sizes to configure at start of level
    private static double BRICK_HEIGHT;
    private static double BRICK_WIDTH;

    // dynamic variables to update based on brick collisions
    private static int BRICK_POWER;
    public static Paint BRICK_COLOR = Color.RED;

    // TODO: implement brick padding (percent values)
    private static final double BRICK_TOP_PAD = 25;
    private static final double BRICK_FLOOR_PAD = 40;
    private static final double BRICK_RIGHT_PAD = 15;
    private static final double BRICK_LEFT_PAD = 15;

}
