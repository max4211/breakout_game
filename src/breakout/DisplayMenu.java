package breakout;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class DisplayMenu extends Text {

    private int myData;
    private String myLabel;
    private static final int FONT_SIZE = 12;

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
        updateDisplay();
    }

    private void updateDisplay() {
        this.setText(myLabel + ": " + myData);
    }

    public static void updateFullDisplay(Group displayGroup, int LIVES_LEFT, int LEVEL, int POINTS_SCORED) {
        int data = 0;
        for (Node n: displayGroup.getChildren()) {
            if (n instanceof DisplayMenu) {
                DisplayMenu dm = (DisplayMenu) n;
                if (dm.getLabel().equals("LIVES_LEFT")) {
                    data = LIVES_LEFT;
                } else if (dm.getLabel().equals("LEVEL")) {
                    data = LEVEL;
                } else if (dm.getLabel().equals("POINTS_SCORED")) {
                    data = POINTS_SCORED;
                }
                dm.updateData(data);
            }
        }
    }


}
