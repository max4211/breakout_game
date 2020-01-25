package breakout;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Level generation class, creates text files that are read in during brick creation
 * When running, ensure that SIDE_PAD is less than 1/2 of BRICKS_PER_ROW
 * TOP_PAD + BOTTOM_PAD < ROWS_OF_BRICKS
 * The padding values create zeroes in place for all subsequent bricks
 * LEVEL_STYLE can also be set (current configurations are constant and random)
 * @author Max Smith
 */
public class LevelGenerator {

    // Vars visible to external devices
    private int LEVEL;
    private final int SIDE_PAD = 1;     // maintain less than 1/2 of BRICKS_PER_ROW
    private final int TOP_PAD = 0;
    private final int BOTTOM_PAD = 1;

    // Vars private to this class
    private final int BRICKS_PER_ROW = 7;
    private final int ROWS_OF_BRICKS = 3;
    private String SEPARATOR = " ";

    // private static final String LEVEL_STYLE = "Random";
    private final String LEVEL_STYLE = "Constant";


    /**
     *  Constructor to rewrite level file with unique bricks
     * @param level is the level numer
     * @throws IOException if and when level file has not been created
     */
    public LevelGenerator(int level) throws IOException {
        LEVEL = level;
        createLevel(LEVEL);
    }

    private String nameFile() throws IOException {
        String fileName = "resources/level_" + LEVEL + ".txt";
        File file = new File(fileName);
        file.createNewFile();
        return fileName;
    }

    private void createLevel(int level) throws IOException {
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
            writer.println();
        }
        writer.close();
    }

    private String assignBrick() {
        String s = "";
        if (LEVEL_STYLE.equals("Random")) {
            s = Integer.toString((int)(Math.random() * LEVEL + 1));
        } else if (LEVEL_STYLE.equals("Constant")) {
            s = Integer.toString(LEVEL);
        }
        return s;
    }

    private boolean clearVerticalPad(int i) {
        return (i >= TOP_PAD && i < (ROWS_OF_BRICKS - BOTTOM_PAD));
    }

    private boolean clearHorizontalPad(int localPad, boolean vertPad) {
        return (localPad > SIDE_PAD) && (localPad <= (BRICKS_PER_ROW - SIDE_PAD)) && (vertPad);
    }

    private void printInt(String s, PrintWriter w, boolean newLine) {
        if (newLine) {
            w.println(s);
        } else {
            w.print(s + SEPARATOR);
        }
    }

}
