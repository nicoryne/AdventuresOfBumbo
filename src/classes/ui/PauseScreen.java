package classes.ui;

import classes.Game;
import classes.util.handlers.FontHandler;

import java.awt.*;
import java.io.IOException;

public abstract class PauseScreen {

    public static void draw(Graphics2D g2) throws IOException, FontFormatException {
        String text = "PAUSED";
        g2.setColor(Color.white);
        g2.setFont(FontHandler.getFont("font-1.ttf", 84f));
        int x = getXCenteredText(text, g2);
        int y = Game.getInstance().getScreenHeight() / 2;

        g2.drawString(text, x, y);
    }

    private static int getXCenteredText(String text, Graphics2D g2) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();

        return Game.getInstance().getScreenWidth() / 2 - length / 2;
    }

}
