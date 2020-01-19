package breakout;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public class Bouncer extends Circle {

    private double BOUNCER_THETA = - Math.PI / 2;
    private static int BOUNCER_NORMAL_SPEED = 180;
    private int BOUNCER_SPEED = BOUNCER_NORMAL_SPEED;
    private static int BOUNCER_RADIUS = 8;
    private boolean BOUNCER_STUCK = false;
    private static Paint BOUNCER_COLOR = Color.GOLD;
    private static int BOUNCER_DAMAGE = 1;

    public Bouncer() {
        super(BOUNCER_RADIUS, BOUNCER_COLOR);
    }

    public double getBouncerTheta() {
        return BOUNCER_THETA;
    }

    public void setBouncerTheta(double theta) {
        BOUNCER_THETA = theta;
    }

    public boolean getBouncerStuck() {
        return BOUNCER_STUCK;
    }

    public void setBouncerStuck(boolean bool) {
        BOUNCER_STUCK = bool;
    }

    public void setBouncerSpeed(int speed) {
        BOUNCER_SPEED = speed;
    }

    public void move(double elapsedTime) {
        this.setCenterY(this.getCenterY() + this.getBouncerSpeed() * elapsedTime * Math.sin(this.getBouncerTheta()));
        this.setCenterX(this.getCenterX() + this.getBouncerSpeed() * elapsedTime * Math.cos(this.getBouncerTheta()));
    }

    public void stickBouncerOnPaddle(Paddle p) {
        this.setCenterX(p.getX() + p.getWidth() / 2);
        this.setCenterY(p.getY() - this.getRadius());
        this.setBouncerStuck(true);
        this.setBouncerSpeed(0);
    }

    public void toggleBouncerStuck() {
        BOUNCER_STUCK = !(BOUNCER_STUCK);
    }

    public int getBouncerSpeed() {
        return BOUNCER_SPEED;
    }

    public int getBouncerNormalSpeed() {
        return BOUNCER_NORMAL_SPEED;
    }

    public int getBouncerDamage() {
        return BOUNCER_DAMAGE;
    }

    public static void clearBouncers(Group bouncerGroup) {
        bouncerGroup.getChildren().clear();
    }

}
