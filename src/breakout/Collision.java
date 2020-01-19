package breakout;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.util.ArrayList;
import java.util.Collection;

public class Collision {

    private static final boolean printCollisionData = true;

    public static void collisionDetection(Group bouncerGroup, Group brickGroup, Group wallGroup, Paddle myPaddle) {
        Collection<Node> destroyedBricks = new ArrayList<Node>();
        for (Node n1: bouncerGroup.getChildren()) {
            if (n1 instanceof Bouncer) {
                Bouncer b = (Bouncer) n1;
                testPaddleCollision(b, myPaddle);
                for (Node n2: wallGroup.getChildren()) {
                    if (n2 instanceof Wall) {
                        Wall w = (Wall) n2;
                        testWallCollision(b, w);
                    }
                }
                for (Node n3: brickGroup.getChildren()) {
                    if (n3 instanceof Brick) {
                        Brick k = (Brick) n3;
                        if (testBrickCollision(b, k)) {
                            destroyedBricks.add(n3);
                        }
                    }
                }
                Game.removeNodes(destroyedBricks, brickGroup);
                destroyedBricks.clear();
            }
        }
    }

    private static boolean checkTopBottomCollision(Rectangle r, Circle c) {
        return r.getBoundsInLocal().contains(c.getBoundsInLocal().getCenterX(), c.getBoundsInLocal().getCenterY() + c.getRadius()) ||
                r.getBoundsInLocal().contains(c.getBoundsInLocal().getCenterX(), c.getBoundsInLocal().getCenterY() - c.getRadius());
    }

    private static boolean checkLeftRightCollision(Rectangle r, Circle c) {
        return r.getBoundsInLocal().contains(c.getBoundsInLocal().getCenterX() + c.getRadius(), c.getBoundsInLocal().getCenterY()) ||
                r.getBoundsInLocal().contains(c.getBoundsInLocal().getCenterX() - c.getRadius(), c.getBoundsInLocal().getCenterY());
    }

    private static boolean shapeCollision(Shape s1, Shape s2) {
        return Shape.intersect(s1, s2).getBoundsInLocal().getWidth() != -1;
    }

    private static double[] checkRectangleBouncerCollision(Rectangle r, Circle c) {
        double[] redirect = new double[2];
        if (checkTopBottomCollision(r, c)) {
            redirect[0] = -1; redirect[1] = 0;
        } else if (checkLeftRightCollision(r, c)) {
            redirect[0] = -1; redirect[1] = Math.PI;
        }
        return redirect;
    }

    private static boolean testBrickCollision(Bouncer b, Brick k) {
        double[] redirect = checkRectangleBouncerCollision(k, b);
        if (redirect[0] != 0) {
            if (k.getBrickPower() > 0) {
                printCollision(b, "BRICK", redirect);
                basicDeflect(redirect, b);
                if (k.hitBrick(b.getBouncerDamage())) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void testWallCollision(Bouncer b, Wall w) {
        double[] redirect = checkRectangleBouncerCollision(w, b);
        if (redirect[0] != 0) {
            printCollision(b, "WALL", redirect);
            basicDeflect(redirect, b);
        }
    }

    private static void testPaddleCollision(Bouncer b, Paddle p) {
        if (shapeCollision(b, p)) {
            angleDeflect(b, p);
        }
    }

    public static boolean groupTouchingShape(Group group, Shape shape) {
        for (Node n: group.getChildren()) {
            if (n instanceof Shape) {
                Shape s = (Shape) n;
                if (shapeCollision(s, shape)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void basicDeflect(double[] redirect, Bouncer b) {
        double scale = redirect[0]; double shift = redirect[1];
        b.setBouncerTheta(b.getBouncerTheta() * scale + shift);
    }

    private static void printCollision(Bouncer b, String object, double[] redirect) {
        if (printCollisionData) {
            System.out.println("FOUND " + object + " COLLISION");
            System.out.println("scale: " + redirect[0] + ", shift: " + redirect[1]);
            System.out.println("theta(1): " + b.getBouncerTheta());
            System.out.println("theta(0): " + b.getBouncerTheta());
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
