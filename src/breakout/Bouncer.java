package breakout;

import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

public class Bouncer extends Circle {

    private static double BOUNCER_THETA = - Math.PI / 2;
    private static int BOUNCER_NORMAL_SPEED = 240;
    private static int BOUNCER_SPEED = BOUNCER_NORMAL_SPEED;
    private static int BOUNCER_RADIUS = 8;
    private static boolean BOUNCER_STUCK = false;
    private static Paint BOUNCER_COLOR = Color.GOLD;
    private static int BOUNCER_DAMAGE = 1;

    private Circle myBouncer;

    public Bouncer() {
        myBouncer = new Circle();
        myBouncer.setRadius(BOUNCER_RADIUS);
        myBouncer.setFill(BOUNCER_COLOR);
    }

    public Bouncer(Circle ball) {
        myBouncer = ball;
    }

    public Circle getMyBouncer() {
        return myBouncer;
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

}
