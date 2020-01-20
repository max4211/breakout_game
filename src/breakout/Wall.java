package breakout;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Wall class, permanent object in scene, added to wallGroup
 * Perfectly elastic collisions with bouncers, could be used for permanent bricks as well
 * @author Max Smith
 */
public class Wall extends Rectangle {

    private String myName;

    // Wall metadata
    private static final int WALL_FLOAT = 0;
    private static final int WALL_WIDTH = 10;
    private static final Paint WALL_FILL = Color.BLACK;

    /**
     * Wall constructor
     * @param x rectangular dimension
     * @param y rectangular dimension
     * @param width rectangular dimension
     * @param height rectangular dimension
     * @param name wall name (helps with bounds detection)
     */
    public Wall(int x, int y, int width, int height, String name) {
        super(x, y, width, height);
        myName = name;
    }

    private String getMyName() {
        return myName;
    }

    // ensure no overlap, use wall positional data to create bounds for brick construction
    public static Collection<Wall> createAllWalls(int width, int height) {
        Collection<Wall> allWalls = new ArrayList<Wall>();
        allWalls.add(new Wall(WALL_FLOAT, WALL_FLOAT + WALL_WIDTH, WALL_WIDTH, height - (WALL_FLOAT + WALL_WIDTH), "LEFT"));
        allWalls.add(new Wall(width - WALL_FLOAT - WALL_WIDTH, WALL_FLOAT + WALL_WIDTH, WALL_WIDTH, height - (WALL_FLOAT + WALL_WIDTH), "RIGHT"));
        allWalls.add(new Wall(WALL_FLOAT, WALL_FLOAT, width - 2 * WALL_FLOAT, WALL_WIDTH, "TOP"));
        return allWalls;
    }

    /**
     * Calculate boundaries for brick field generation
     * @param wallGroup contains all wall objects
     * @return double: {left_bound, right_bound, top_bound} (from players perspective)
     */
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
