package breakout;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class LevelGenerator {

    // Vars visible to external devices
    public static String FILE_NAME = "resources/level_test.txt";
    public static final int SIDE_PAD = 2;     // maintain less than 1/2 of BRICKS_PER_ROW
    public static int LEVEL_DIFFICULTY = 6;

    // Vars private to this class
    private static final int BRICKS_PER_ROW = 10;
    private static final int ROWS_OF_BRICKS = 3;
    private static String SEPARATOR = " ";

    // TODO: Incorporate side padding to level construction
    public static void createLevel() throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(FILE_NAME);
        int brick; int localPad;
        printInt("BRICKS_PER_ROW" + SEPARATOR + BRICKS_PER_ROW, writer, true);
        printInt("ROWS_OF_BRICKS" + SEPARATOR + ROWS_OF_BRICKS, writer, true);
        for (int i = 0; i < ROWS_OF_BRICKS; i ++) {
            localPad = 0;
            for (int j = 0; j < BRICKS_PER_ROW; j ++) {
                localPad ++;
                if ((localPad > SIDE_PAD) && (localPad <= (BRICKS_PER_ROW - SIDE_PAD))) {
                    brick = (int)(Math.random() * LEVEL_DIFFICULTY + 1);
                } else {
                    brick = 0;
                }
                printInt(Integer.toString(brick), writer, false);
            }
            System.out.println();
            writer.println();
        }
        writer.close();
    }

    private static void printInt(String s, PrintWriter w, boolean newLine) {
        if (newLine) {
            System.out.println(s);
            w.println(s);
        } else {
            System.out.print(s + SEPARATOR);
            w.print(s + SEPARATOR);
        }

    }

    public static void main (String[] args) throws FileNotFoundException {
        createLevel();
    }

}
