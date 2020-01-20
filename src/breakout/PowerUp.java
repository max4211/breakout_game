package breakout;

import javafx.scene.Group;
import javafx.scene.shape.Circle;

public class PowerUp extends Circle {

    private char myPower;
    private static int POWER_RADIUS = 8;
    private static int FALL_SPEED = 10;

    private static String powerUpList = "";// "X";
    private static double powerProbability = 0.0; // 0.3;

    public PowerUp(char c) {
        super(POWER_RADIUS);
        myPower = c;
    }

    // TODO: Impelement power up setting
    public void usePowerUp(Paddle p, Group bouncerGroup) {
        if (myPower == 'X') {
            p.extend();
        }
    }

    public void move(double elapsedTime) {
        this.setCenterY(this.getCenterY() + this.getFallSpeed() * elapsedTime);
    }

    public int getFallSpeed() {
        return FALL_SPEED;
    }

    public String getPowerList() {
        return powerUpList;
    }

    public double getPowerProbability() {
        return powerProbability;
    }

    private static String randomPower() {
        int index = (int) (Math.random() * powerUpList.length());
        return Character.toString(powerUpList.charAt(index));
    }

}
