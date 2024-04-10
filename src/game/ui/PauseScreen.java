package game.ui;

import game.Game;
import game.exceptions.FontHandlerException;
import game.util.handlers.FontHandler;
import services.LoggerHelper;

import java.awt.*;
import java.io.IOException;

public abstract class PauseScreen {

    public static void draw(Graphics2D g2) throws IOException, FontFormatException {
        String text = "PAUSED";
        g2.setColor(Color.white);

        try {
            Font font = FontHandler.getFont("font-1.ttf", 84f);
            g2.setFont(font);
        } catch (FontHandlerException e) {
            LoggerHelper.logError("Error setting font: ", e);
        }

        int x = getXCenteredText(text, g2);
        int y = Game.getInstance().getScreenHeight() / 2;

        g2.drawString(text, x, y);
    }

    private static int getXCenteredText(String text, Graphics2D g2) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();

        return Game.getInstance().getScreenWidth() / 2 - length / 2;
    }
}
