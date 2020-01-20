package breakout;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Paddle extends Rectangle {

    // Paddle metadata
    private static final int PADDLE_HEIGHT = 10;
    private static final int PADDLE_FLOAT = 50;              // paddle pixel float above bottom of screen
    private static final int START_WIDTH = 100;
    private static final double PADDLE_EDGE = Math.PI / 4;   // max paddle angular deflection off edge
    private static final Paint PADDLE_COLOR = Color.BLUEVIOLET;

    // Paddle data subject to change
    private double PADDLE_SPEED = 15;
    private boolean PADDLE_STICKY = false;
    private double EXTEND_FACTOR = 1.5;
    private double SPEED_FACTOR = 1.25;

    // Constructors
    public Paddle(int width, int height) {
        super(START_WIDTH, PADDLE_HEIGHT);
        this.setX(width / 2 - this.getBoundsInLocal().getWidth() / 2);
        this.setY(height - this.getBoundsInLocal().getHeight() / 2 - PADDLE_FLOAT);
        this.setFill(PADDLE_COLOR);
    }

    public void extend(double coef) {
        double oldCenter = (this.getX()) + (this.getWidth() / 2);
        this.setWidth(this.getWidth() * coef * EXTEND_FACTOR);
        this.realign(oldCenter);
    }

    private void realign(double center) {
        this.setX(center - this.getWidth() / 2);
    }

    public void speedPaddle() {
        this.setPaddleSpeed(this.getPaddleSpeed() * SPEED_FACTOR);
    }

    public double getPaddleBound() {
        return this.getBoundsInLocal().getMinY();
    }

    public double getPaddleSpeed() {
        return PADDLE_SPEED;
    }

    public void setPaddleSpeed(double speed) {
        PADDLE_SPEED = speed;
    }

    public double getPaddleEdge() {
        return PADDLE_EDGE;
    }

    public double getPaddleUpperBounds() {
        return 0;
    }

    public void toggleSticky() {
        this.PADDLE_STICKY = !(PADDLE_STICKY);
    }

    public boolean getSticky() {
        return PADDLE_STICKY;
    }
}
