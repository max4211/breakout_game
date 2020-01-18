package breakout;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Collection;

public class Wall extends Node {

    private Rectangle myWall;

    // Wall metadata
    public static final int WALL_FLOAT = 0;
    public static final int WALL_WIDTH = 10;
    public static final Paint WALL_FILL = Color.BLACK;

    public Wall(int x, int y, int width, int height) {
        myWall = new Rectangle (x, y, width, height);
    }

    public Wall(Rectangle rect) {
        myWall = rect;
    }

    public Rectangle getMyWall() {
        return myWall;
    }

    public static Collection<Wall> createAllWalls() {
        Collection<Wall> allWalls = new ArrayList<Wall>();
        allWalls.add(new Wall(WALL_FLOAT, WALL_FLOAT, WALL_WIDTH, Game.SCREEN_HEIGHT - WALL_FLOAT));
        allWalls.add(new Wall(Game.SCREEN_WIDTH - 2 * WALL_FLOAT, WALL_FLOAT, WALL_WIDTH, Game.SCREEN_HEIGHT - WALL_FLOAT));
        allWalls.add(new Wall(WALL_FLOAT, WALL_FLOAT, Game.SCREEN_WIDTH - 3 * WALL_FLOAT, WALL_WIDTH));
        return allWalls;
    }

}
