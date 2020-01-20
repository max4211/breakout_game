package breakout;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.Collection;

public class Bouncer extends Circle {

    private double BOUNCER_NORMAL_THETA = - Math.PI / 2;
    private double BOUNCER_THETA = BOUNCER_NORMAL_THETA;
    private static double BOUNCER_NORMAL_SPEED = 240;
    private double BOUNCER_SPEED = BOUNCER_NORMAL_SPEED;
    private static int BOUNCER_RADIUS = 8;
    private boolean BOUNCER_STUCK = false;
    private static Paint BOUNCER_COLOR = Color.GOLD;
    private static int BOUNCER_DAMAGE = 1;
    private static final double SPEED_FACTOR = 0.9;

    public Bouncer() {
        super(BOUNCER_RADIUS, BOUNCER_COLOR);
    }

    public static Group addBouncers(Group bouncerGroup) {
        Collection<Bouncer> newBouncers = new ArrayList<Bouncer>();
        for (Node n: bouncerGroup.getChildren()) {
            if (n instanceof Bouncer) {
                Bouncer b1 = (Bouncer) n;
                newBouncers.add(duplicateBouncer(b1));
            }
        }
        for (Bouncer b: newBouncers) {
            bouncerGroup.getChildren().add(b);
        }
        return bouncerGroup;
    }

    private static Bouncer duplicateBouncer(Bouncer b) {
        Bouncer b2 = new Bouncer();
        b2.setBouncerTheta(randomTheta(b));
        b2.setCenterX(b.getCenterX());
        b2.setCenterY(b.getCenterY());
        return b2;
    }

    private static double randomTheta(Bouncer b) {
        double theta = b.getBouncerTheta();
        double scale = -1;
        if (Math.round(Math.random()) == 1) {
            scale = 1;
        }
        return theta + theta / 12 * scale;
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

    public void setBouncerSpeed(double speed) {
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

    public double getBouncerSpeed() {
        return BOUNCER_SPEED;
    }

    public double getBouncerNormalSpeed() {
        return BOUNCER_NORMAL_SPEED;
    }

    public double getBouncerNormalTheta() { return BOUNCER_NORMAL_THETA;}

    public int getBouncerDamage() {
        return BOUNCER_DAMAGE;
    }

    public static void clearBouncers(Group bouncerGroup) {
        bouncerGroup.getChildren().clear();
    }

    public void catchBall() {
        this.BOUNCER_SPEED = 0;
        this.BOUNCER_STUCK = true;
    }

    public void releaseBall() {
        this.BOUNCER_SPEED = BOUNCER_NORMAL_SPEED;
        this.BOUNCER_THETA = BOUNCER_NORMAL_THETA;
        this.BOUNCER_STUCK = false;
    }

    private void factorSpeed() {
        this.setBouncerSpeed(this.getBouncerSpeed() * SPEED_FACTOR);
    }

    public static void slowBouncers(Group bouncerGroup) {
        for (Node n: bouncerGroup.getChildren()) {
            if (n instanceof Bouncer) {
                Bouncer b = (Bouncer) n;
                b.factorSpeed();
            }
        }
    }


}
