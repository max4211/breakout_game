package breakout;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Paddle extends Rectangle {

    private Rectangle myPaddle;

    // Paddle metadata
    private static final int PADDLE_HEIGHT = 10;
    private static final int PADDLE_FLOAT = 50;              // paddle pixel float above bottom of screen
    private static final double PADDLE_EDGE = Math.PI / 4;   // max paddle angular deflection off edge
    private static final Paint PADDLE_COLOR = Color.BLUEVIOLET;

    // Paddle data subject to change
    private static int PADDLE_WIDTH = 100;
    private static int PADDLE_SPEED = 10;

    // Constructors
    public Paddle(int width, int height) {
        myPaddle = new Rectangle();
        myPaddle.setX(width / 2 - myPaddle.getBoundsInLocal().getWidth() / 2);
        myPaddle.setY(height - myPaddle.getBoundsInLocal().getHeight() / 2 - PADDLE_FLOAT);
        myPaddle.setWidth(PADDLE_WIDTH);
        myPaddle.setHeight(PADDLE_HEIGHT);
        myPaddle.setFill(PADDLE_COLOR);
    }

    public Rectangle getMyPaddle() {
        return myPaddle;
    }

    public int getPaddleSpeed() {
        return PADDLE_SPEED;
    }

    public void setPaddleSpeed(int speed) {
        PADDLE_SPEED = speed;
    }

    public double getPaddleEdge() {
        return PADDLE_EDGE;
    }

    public double getPaddleUpperBounds() {
        return 0;
    }

}
