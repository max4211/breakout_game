package breakout;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.awt.*;

public class SplashMenu extends Text{

    private static String MESSAGE = "Welcome to breakout! \nRules coming sooon...";
    private boolean SHOW_SPLASH = true;
    // private static final Paint SPLASH_BACKGROUND = Color.WHITE;

    public SplashMenu(int width, int height) {
        super(width, height, MESSAGE);
        // this.setFill(Color.WHITE);
        this.setText(MESSAGE);
        // this.setTextAlignment(TextAlignment.CENTER);
        this.setX(width/4);
        this.setY(height/2);
        this.setFont(Font.font(15));
    }

    private void updateSplash() {
        if (this.SHOW_SPLASH) {
            this.setText(MESSAGE);
        } else {

        }
    }

    private void showSplash() {
        this.setText(MESSAGE);
        // this.setFill(SPLASH_BACKGROUND);
    }

    private void clearSplash() {
        this.setText("");
        this.setFill(Color.TRANSPARENT);
    }

    public void toggleSplash() {
        this.SHOW_SPLASH = !(SHOW_SPLASH);
        updateSplash();
    }

    public boolean isShowing() {
        return this.SHOW_SPLASH;
    }


}
