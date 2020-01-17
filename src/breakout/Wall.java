package breakout;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Wall {

    private Rectangle myWall;

    public Wall(int x, int y, int width, int height) {
        myWall = new Rectangle (x, y, width, height);
    }

    public Wall(Rectangle rect) {
        myWall = rect;
    }

    private Rectangle getWall() {
        return myWall;
    }

}
