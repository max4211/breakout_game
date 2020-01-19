package breakout;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;
import java.util.concurrent.CopyOnWriteArrayList;

public class Brick extends Rectangle {

    // Brick constant metadata
    private static final double BRICK_STROKE_WIDTH = 0.05;

    // dynamic variables to update based on brick collisions
    private static int BRICK_POWER;

    public Brick(double x, double y, double width, double height, int power) {
        super(x, y, width, height);
        System.out.println("Brick constructor called, new brick created");
        this.BRICK_POWER = power;
        this.setFill(Color.RED);
        this.setStrokeWidth(this.getWidth() * BRICK_STROKE_WIDTH);
        this.setStroke(Color.BLACK);
    }

    public int getBrickPower() {
        return this.BRICK_POWER;
    }

    public boolean hitBrick(int damage) {
        this.BRICK_POWER -= damage;
        updateBrickColor();
        return (this.BRICK_POWER < 1);
    }

    public void updateBrickColor() {
        if (this.BRICK_POWER < 1) {
            this.setFill(Color.TRANSPARENT);
            this.setStroke(Color.TRANSPARENT);
        } else {
            this.setFill(Color.BLUE);
        }
    }
}
