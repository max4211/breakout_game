package breakout;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Paddle extends Node {

    private Rectangle myPaddle;

    // Paddle metadata
    public static final int PADDLE_HEIGHT = 10;
    public static final int PADDLE_SPEED = 10;
    public static final int PADDLE_FLOAT = 30;              // paddle pixel float above bottom of screen
    public static final double PADDLE_EDGE = Math.PI / 4;   // max paddle angular deflection off edge
    public static final Paint PADDLE_COLOR = Color.BLUEVIOLET;
    public static int PADDLE_WIDTH = 100;

    public Paddle(int width, int height) {
        myPaddle = new Rectangle();
        myPaddle.setX(width / 2 - myPaddle.getBoundsInLocal().getWidth() / 2);
        myPaddle.setY(height - myPaddle.getBoundsInLocal().getHeight() / 2 - PADDLE_FLOAT);
        myPaddle.setWidth(PADDLE_WIDTH);
        myPaddle.setHeight(PADDLE_HEIGHT);
        myPaddle.setFill(PADDLE_COLOR);
    }

    @Override
    public Node getStyleableNode() {
        return null;
    }
}
