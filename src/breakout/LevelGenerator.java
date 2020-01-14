package breakout;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class LevelGenerator {

    public static int LEVEL_DIFFICULTY = 5;
    public static int BRICKS_PER_ROW = 10;
    public static int ROWS_OF_BRICKS = 10;
    public static String FILE_NAME = "resources/level_test.txt";

    public static void createLevel() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(FILE_NAME);
        int brick;
        for (int i = 0; i < ROWS_OF_BRICKS; i ++) {
            for (int j = 0; j < BRICKS_PER_ROW; j ++) {
                brick = (int)(Math.random() * LEVEL_DIFFICULTY + 1);
                System.out.print(brick);
                writer.print(brick);
            }
            System.out.println();
            writer.println();
        }
        writer.close();
    }

    public static void main (String[] args) throws FileNotFoundException {
        createLevel();
    }

}
