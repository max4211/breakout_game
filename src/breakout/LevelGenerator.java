package breakout;

import java.io.IOException;
import java.io.PrintWriter;

public class LevelGenerator {

    // Vars visible to external devices
    private static int LEVEL;
    private static int MAX_LEVEL = 5;
    private static final int SIDE_PAD = 2;     // maintain less than 1/2 of BRICKS_PER_ROW
    private static final int TOP_PAD = 2;
    private static final int BOTTOM_PAD = 1;

    // Vars private to this class
    private static final int BRICKS_PER_ROW = 10;
    private static final int ROWS_OF_BRICKS = 6;
    private static String SEPARATOR = " ";

    public LevelGenerator(int level) throws IOException {
        LEVEL = level;
        createLevel(LEVEL);
    }

    private static String nameFile() {
        return "resources/level_" + LEVEL + ".txt";
    }

    private static void createLevel(int level) throws IOException {
        LEVEL = level;
        PrintWriter writer = new PrintWriter(nameFile());
        String brick; int localPad; boolean vertPad;
        printInt("BRICKS_PER_ROW" + SEPARATOR + BRICKS_PER_ROW, writer, true);
        printInt("ROWS_OF_BRICKS" + SEPARATOR + ROWS_OF_BRICKS, writer, true);
        for (int i = 0; i < ROWS_OF_BRICKS; i ++) {
            vertPad = clearVerticalPad(i);
            localPad = 0;
            for (int j = 0; j < BRICKS_PER_ROW; j ++) {
                localPad ++;
                if (clearHorizontalPad(localPad, vertPad)) {
                    brick = assignBrick();
                } else {
                    brick = "0";
                }
                printInt(brick, writer, false);
            }
            System.out.println();
            writer.println();
        }
        writer.close();
    }

    private static String assignBrick() {
        return Integer.toString((int)(Math.random() * LEVEL + 1));
    }

    private static boolean clearVerticalPad(int i) {
        return (i >= TOP_PAD && i < (ROWS_OF_BRICKS - BOTTOM_PAD));
    }

    private static boolean clearHorizontalPad(int localPad, boolean vertPad) {
        return (localPad > SIDE_PAD) && (localPad <= (BRICKS_PER_ROW - SIDE_PAD)) && (vertPad);
    }

    private static void generateLevels() throws IOException {
        for (int i = 1; i <=  MAX_LEVEL; i ++) {
            createLevel(i);
        }
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

    public static void main(String[] args) throws IOException {
        generateLevels();
    }

}
