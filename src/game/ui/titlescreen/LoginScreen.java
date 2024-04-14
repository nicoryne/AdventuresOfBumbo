package game.ui.titlescreen;

import game.Game;
import game.exceptions.FontHandlerException;
import game.util.handlers.FontHandler;
import services.LoggerHelper;

import java.awt.*;

public abstract class LoginScreen {

    public static void draw(Graphics2D g2) {
        drawLogin(g2);
    }

    private static void drawLogin(Graphics2D g2) {
        Font font = FontHandler.getFont("font-1.ttf", 48f);
        g2.setFont(font);

        //  LOGIN TEXT

    }
    private static int getXCenteredText(String text, Graphics2D g2) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();

        return Game.getInstance().getScreenWidth() / 2 - length / 2;
    }
}
