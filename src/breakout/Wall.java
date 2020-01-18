package breakout;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Collection;

public class Wall extends Node {

    private Rectangle myWall;
    private String myName;

    // Wall metadata
    private static final int WALL_FLOAT = 0;
    private static final int WALL_WIDTH = 10;
    private static final Paint WALL_FILL = Color.BLACK;

    public Wall(int x, int y, int width, int height, String name) {
        myWall = new Rectangle (x, y, width, height);
        myName = name;
    }

    public Wall(Rectangle rect, String name) {
        myWall = rect;
        myName = name;
    }

    public Wall(int x, int y, int width, int height) {
        myWall = new Rectangle (x, y, width, height);
        myName = "";
    }

    public Wall(Rectangle rect) {
        myWall = rect;
        myName = "";
    }

    public String getMyName() {
        return myName;
    }

    public Rectangle getMyWall() {
        return myWall;
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

}
