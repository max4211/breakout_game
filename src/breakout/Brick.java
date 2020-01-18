package breakout;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Brick {

    private Rectangle myBrick;

    // Brick metadata
    private static final int BRICK_ROWS = 25;
    private static final int BRICKS_PER_ROW = 10;
    private static final double BRICK_STROKE_WIDTH = 0.05;
    private static final Paint BRICK_STROKE_COLOR = Color.BLACK;

    // TODO: implement brick padding (update to  double?)
    private static final int BRICK_TOP_PAD = 25;
    private static final int BRICK_FLOOR_PAD = 40;
    private static final int BRICK_RIGHT_PAD = 15;
    private static final int BRICK_LEFT_PAD = 15;

    // dynamic variable sizes to configure at start of game
    private static double BRICK_HEIGHT;
    private static double BRICK_WIDTH;

    // dynamic variable to update based on brick collisions
    public static final Paint BRICK_COLOR = Color.RED;

}
