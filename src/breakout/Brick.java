package breakout;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Map;

import java.awt.*;
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
    private int BRICK_POWER;
    private static Map<Integer, javafx.scene.paint.Paint> BRICK_COLORS;

    public Brick(double x, double y, double width, double height, int power) {
        super(x, y, width, height);
        BRICK_COLORS = BrickStyler.styleBricks();
        this.BRICK_POWER = power;
        updateBrickColor();
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

    private void updateBrickColor() {
        if (this.BRICK_POWER < 1) {
            this.setFill(Color.TRANSPARENT);
            this.setStroke(Color.TRANSPARENT);
        } else {
            this.setFill(BRICK_COLORS.get(this.BRICK_POWER));
        }
    }

}
