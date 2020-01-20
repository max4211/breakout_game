package breakout;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

/**
 * DisplayMenu creates text boxes along bottom display
 * Hold data and labels, text boxes update on each step in main function
 * @author Max Smith
 */
public class DisplayMenu extends Text {

    private int myData;
    private String myLabel;
    private static final int FONT_SIZE = 16;

    /**
     *
     * @param width overall screen of play width
     * @param height overall screen height width
     * @param heightOffset additional height to factor into y
     * @param widthOffset width modulation to orient in screen
     * @param data data to be held by display object
     * @param label label of data
     */
    public DisplayMenu(int width, int height, int heightOffset, double widthOffset, int data, String label) {
        super(width, height, label);
        this.myLabel = label;
        this.myData = data;
        this.setX(width*widthOffset);
        this.setY(height + heightOffset);
        this.setFont(Font.font(FONT_SIZE));
        this.updateDisplay();
    }

    private String getLabel() {
        return myLabel;
    }

    private void updateData(int data) {
        this.myData = data;
        this.updateDisplay();
    }

    private void updateDisplay() {
        this.setText(myLabel + ": " + myData);
    }

    /**
     * Update on each step, show appropriate info to player
     * @param displayGroup holds all display items
     * @param LIVES_LEFT lives in game still available
     * @param LEVEL level of game
     * @param POINTS_SCORED total points scored
     */
    public static void updateFullDisplay(Group displayGroup, int LIVES_LEFT, int LEVEL, int POINTS_SCORED) {
        int data = 0;
        for (Node n: displayGroup.getChildren()) {
            if (n instanceof DisplayMenu) {
                DisplayMenu dm = (DisplayMenu) n;
                if (dm.getLabel().equals("LIVES")) {
                    data = LIVES_LEFT;
                } else if (dm.getLabel().equals("LEVEL")) {
                    data = LEVEL;
                } else if (dm.getLabel().equals("POINTS")) {
                    data = POINTS_SCORED;
                }
                dm.updateData(data);
            }
        }
    }


}
