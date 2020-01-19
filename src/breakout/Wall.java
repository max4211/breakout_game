package breakout;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.Collection;

public class Wall extends Rectangle {

    private String myName;

    // Wall metadata
    private static final int WALL_FLOAT = 0;
    private static final int WALL_WIDTH = 10;
    private static final Paint WALL_FILL = Color.BLACK;

    public Wall(int x, int y, int width, int height, String name) {
        super(x, y, width, height);
        myName = name;
    }

    public String getMyName() {
        return myName;
    }

    // ensure no overlap, use wall positional data to create bounds for brick construction
    // TODO: segment wall creation into specific sides (ok to do here b/c segmented)
    public static Collection<Wall> createAllWalls() {
        Collection<Wall> allWalls = new ArrayList<Wall>();
        allWalls.add(new Wall(WALL_FLOAT, WALL_FLOAT + WALL_WIDTH, WALL_WIDTH, Game.SCREEN_HEIGHT - (WALL_FLOAT + WALL_WIDTH), "LEFT"));
        allWalls.add(new Wall(Game.SCREEN_WIDTH - WALL_FLOAT - WALL_WIDTH, WALL_FLOAT + WALL_WIDTH, WALL_WIDTH, Game.SCREEN_HEIGHT - (WALL_FLOAT + WALL_WIDTH), "RIGHT"));
        allWalls.add(new Wall(WALL_FLOAT, WALL_FLOAT, Game.SCREEN_WIDTH - 2 * WALL_FLOAT, WALL_WIDTH, "TOP"));
        return allWalls;
    }

    // int: {left_bound, right_bound, top_bound}
    public static double[] getWallBounds(Group wallGroup) {
        double[] wallBounds = new double[3];
        for (Node n: wallGroup.getChildren()) {
            if (n instanceof Wall) {
                Wall w = (Wall) n;
                if (w.getMyName().equals("LEFT")) {
                    wallBounds[0] = w.getBoundsInLocal().getMaxX();
                } else if (w.getMyName().equals("RIGHT")) {
                    wallBounds[1] = w.getBoundsInLocal().getMinX();
                } else if (w.getMyName().equals("TOP")) {
                    wallBounds[2] = w.getBoundsInLocal().getMaxY();
                }
            }
        }
        return wallBounds;
    }

}
