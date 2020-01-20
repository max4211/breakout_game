package breakout;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

/**
 * Paddle to hit bouncers and redirect back out into field
 * See collision for specs on paddle collision angles
 * Sticky paddle, extension, and speed adjustments all in conjunction with cheat codes
 * @author Max Smith
 */
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

    /**
     * Paddle constructor, uses lots of static finals (original paddle is set size)
     * @param width width of main game screen
     * @param height height of main game screen
     */
    public Paddle(int width, int height) {
        super(START_WIDTH, PADDLE_HEIGHT);
        this.setX(width / 2 - this.getBoundsInLocal().getWidth() / 2);
        this.setY(height - this.getBoundsInLocal().getHeight() / 2 - PADDLE_FLOAT);
        this.setFill(PADDLE_COLOR);
    }

    /**
     * Called on paddles to extend (or contract) their size in response to cheat code
     * @param coef is factor to extend by (less than 1 shrink, greater than 1 expand
     */
    public void extend(double coef) {
        double oldCenter = (this.getX()) + (this.getWidth() / 2);
        this.setWidth(this.getWidth() * coef * EXTEND_FACTOR);
        this.realign(oldCenter);
    }

    private void realign(double center) {
        this.setX(center - this.getWidth() / 2);
    }

    /**
     * Accelerates paddle by global speed factor
     */
    public void speedPaddle() {
        this.setPaddleSpeed(this.getPaddleSpeed() * SPEED_FACTOR);
    }

    /**
     * Updates paddle when motion
     * @param direct is direction of motion (+ right, - left)
     */
    public void sideKey(int direct) {
        this.setX(this.getX() + direct * this.getPaddleSpeed());
    }

    /**
     * Helps calculate bouncer adjustment when stuck to paddle
     * @return paddle speed
     */
    public double getSpeed() {
        return this.PADDLE_SPEED;
    }

    /**
     * Helps determine brick dimensions according to spacing
     * @return paddle upper Y bound
     */
    public double getPaddleBound() {
        return this.getBoundsInLocal().getMinY();
    }

    /**
     * Updates paddle sticky in response to cheat code
     */
    public void toggleSticky() {
        this.PADDLE_STICKY = !(PADDLE_STICKY);
    }

    /**
     * Helps determine action on ball collision
     * @return whether paddle is sticky
     */
    public boolean getSticky() {
        return PADDLE_STICKY;
    }

    /**
     * Used in collision detection class to determine angle of deflection
     * @return largest angle to deflect bouncer
     */
    public double getPaddleEdge() {
        return PADDLE_EDGE;
    }

    private double getPaddleSpeed() {
        return PADDLE_SPEED;
    }

    private void setPaddleSpeed(double speed) {
        PADDLE_SPEED = speed;
    }
}
