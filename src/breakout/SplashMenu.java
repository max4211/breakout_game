package breakout;

import javafx.scene.text.Font;
import javafx.scene.text.Text;
/**
 * Splash menu - shows basic text box of instructions at start of game
 * @author Max Smith
 */

public class SplashMenu extends Text{

    private static String MESSAGE = "Welcome to breakout! " +
            "\nHit the bouncer around to destroy bricks" +
            "\nMove the paddle with left/right arrows" +
            "\nEarn points as bricks are destroyed" +
            "\nYou have three lives" +
            "\nGood luck!" +
            "\n(Press enter to continue)";
    private boolean SHOW_SPLASH = true;

    public SplashMenu(int width, int height) {
        super(width, height, MESSAGE);
        this.setText(MESSAGE);
        // this.setTextAlignment(TextAlignment.CENTER);
        this.setX(width/16);
        this.setY(height/16);
        this.setFont(Font.font(16));
    }

    private void updateSplash() {
        if (this.SHOW_SPLASH) {
            showSplash();
        } else {
            clearSplash();
        }
    }

    private void changeSplashText(String text) {
        this.setText(text);
    }

    private void showSplash() {
        this.setText(MESSAGE);
    }

    private void clearSplash() {
        this.setText("");
    }

    /**
     * Splash toggle used to enter game play
     */
    public void toggleSplash() {
        this.SHOW_SPLASH = !(SHOW_SPLASH);
        updateSplash();
    }

    /**
     *  Used to validate game play (guard, no play while splash is up)
     * @return splash status
     */
    public boolean isShowing() {
        return this.SHOW_SPLASH;
    }


}
