package breakout;

import java.io.IOException;

public class RandomLevelGenerator extends LevelGenerator {

    private int LEVEL;

    /**
     * Constructor to rewrite level file with unique bricks
     *
     * @param level is the level numer
     * @throws IOException if and when level file has not been created
     */
    public RandomLevelGenerator(int level) throws IOException {
        super(level);
        LEVEL = level;
    }

    private String assignBrick() {
        return Integer.toString(2);// Integer.toString((int)(Math.random() * LEVEL + 1));
    }


}
