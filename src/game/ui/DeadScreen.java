package game.ui;

import game.Game;
import game.exceptions.FontHandlerException;
import game.util.handlers.FontHandler;
import services.LoggerHelper;

import java.awt.*;
import java.io.IOException;

public abstract class DeadScreen {

    public static void draw(Graphics2D g2) {
        String text = "YOU DIED";
        g2.setColor(Color.white);

        Font font = FontHandler.getFont("font-1.ttf", 84f);
        g2.setFont(font);

        int x = getXCenteredText(text, g2);
        int y = Game.getInstance().getScreenHeight() / 2;

        g2.drawString(text, x, y);
    }

    private static int getXCenteredText(String text, Graphics2D g2) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();

        return Game.getInstance().getScreenWidth() / 2 - length / 2;
    }
}
