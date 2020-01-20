package breakout;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import java.util.HashMap;
import java.util.Map;

public class BrickStyler {

    public static Map<Integer, Paint> styleBricks() {
        Map<Integer, Paint> brickColors = new HashMap<Integer, Paint>();
        brickColors.put(1, Color.RED);
        brickColors.put(2, Color.ORANGE);
        brickColors.put(3, Color.YELLOW);
        brickColors.put(4, Color.GREEN);
        brickColors.put(5, Color.BLUE);
        brickColors.put(6, Color.DARKMAGENTA);
        brickColors.put(7, Color.VIOLET);
        brickColors.put(8, Color.DARKSLATEGRAY);
        brickColors.put(9, Color.CHOCOLATE);
        return brickColors;
    }

}
