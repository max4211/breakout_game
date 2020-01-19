package breakout;

import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

public class Collision {

    public static boolean checkTopBottomCollision(Rectangle r, Circle c) {
        return r.getBoundsInLocal().contains(c.getBoundsInLocal().getCenterX(), c.getBoundsInLocal().getCenterY() + c.getRadius()) ||
                r.getBoundsInLocal().contains(c.getBoundsInLocal().getCenterX(), c.getBoundsInLocal().getCenterY() - c.getRadius());
    }

    public static boolean checkLeftRightCollision(Rectangle r, Circle c) {
        return r.getBoundsInLocal().contains(c.getBoundsInLocal().getCenterX() + c.getRadius(), c.getBoundsInLocal().getCenterY()) ||
                r.getBoundsInLocal().contains(c.getBoundsInLocal().getCenterX() - c.getRadius(), c.getBoundsInLocal().getCenterY());
    }

    public static boolean shapeCollision(Shape s1, Shape s2) {
        return Shape.intersect(s1, s2).getBoundsInLocal().getWidth() != -1;
    }

    public static void basicDeflect(double[] redirect, Bouncer b) {
        double scale = redirect[0]; double shift = redirect[1];
        b.setBouncerTheta(b.getBouncerTheta() * scale + shift);
    }

    public static double[] checkRectangleBouncerCollision(Rectangle r, Circle c) {
        double[] redirect = new double[2];
        if (Collision.checkTopBottomCollision(r, c)) {
            redirect[0] = -1; redirect[1] = 0;
        } else if (Collision.checkLeftRightCollision(r, c)) {
            redirect[0] = -1; redirect[1] = Math.PI;
        }
        return redirect;
    }

    public static boolean testBrickCollision(Bouncer b, Brick k) {
        double[] redirect = checkRectangleBouncerCollision(k, b);
        if (redirect[0] != 0) {
            if (k.getBrickPower() > 0) {
                Collision.basicDeflect(redirect, b);
                if (k.hitBrick(b.getBouncerDamage())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void testWallCollision(Bouncer b, Wall w) {
        double[] redirect = checkRectangleBouncerCollision(w, b);
        if (redirect[0] != 0) {
            System.out.println("FOUND WALL COLLISION");
            Collision.basicDeflect(redirect, b);
        }
    }

    public static void testPaddleCollision(Bouncer b, Paddle p) {
        if (Collision.shapeCollision(b, p)) {
            angleDeflect(b, p);
        }
    }

    private static void angleDeflect(Bouncer b, Paddle p) {
        double paddleX = p.getX();
        double bouncerX = b.getCenterX();
        double paddleWidth = p.getWidth();
        double paddleCenter = paddleX + paddleWidth / 2;
        b.setBouncerTheta(- Math.PI / 2 + 2 * (bouncerX - paddleCenter) * (p.getPaddleEdge() / paddleWidth));
    }
}
