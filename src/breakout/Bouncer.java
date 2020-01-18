package breakout;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class Bouncer {

    public static double BOUNCER_THETA = - Math.PI / 2;
    public static int BOUNCER_NORMAL_SPEED = 240;
    public static int BOUNCER_SPEED = BOUNCER_NORMAL_SPEED;
    public static int BOUNCER_RADIUS = 8;
    public static boolean BOUNCER_STUCK = false;
    public static Paint BOUNCER_COLOR = Color.GOLD;

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

}
