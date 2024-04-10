package game.ui.components.titlescreen;

import game.Game;
import game.exceptions.FontHandlerException;
import game.util.handlers.FontHandler;

import java.awt.*;
import java.io.IOException;

public abstract class LoginScreen {

    public enum LoginScreenState {
        LOGIN, SUCCESSFUL, FAILED
    }

    protected static LoginScreenState loginScreenState = LoginScreenState.LOGIN;

    public static void draw(Graphics2D g2) throws IOException, FontFormatException {

        switch(loginScreenState) {
            case LOGIN -> drawLogin(g2);
            case SUCCESSFUL -> drawSuccessful(g2);
            case FAILED -> drawFailed(g2);
        }
    }

    private static void drawLogin(Graphics2D g2) throws IOException, FontFormatException {
        Font font;
        try {
            font = FontHandler.getFont("font-1.ttf", 48f);
        } catch (FontHandlerException e) {
            throw new RuntimeException(e);
        }

        g2.setFont(font);

        //  LOGIN TEXT
        String text = "LOGIN";
        int x = getXCenteredText(text , g2);
        int y = Game.getInstance().getScreenHeight() / 2;

        g2.setColor(TitleScreen.MENU_SHADOW_COLOR);
        g2.drawString(text, x + 5, y + 5);

        g2.setColor(TitleScreen.MENU_SELECTED_COLOR);
        g2.drawString(text, x, y);
    }

    private static void drawSuccessful(Graphics2D g2) throws IOException, FontFormatException {
        Font font;
        try {
            font = FontHandler.getFont("font-1.ttf", 48f);
        } catch (FontHandlerException e) {
            throw new RuntimeException(e);
        }

        g2.setFont(font);

        //  LOGIN TEXT
        String text = "LOGIN SUCCESSFUL";
        int x = getXCenteredText(text , g2);
        int y = Game.getInstance().getScreenHeight() / 2;

        g2.setColor(TitleScreen.MENU_SHADOW_COLOR);
        g2.drawString(text, x + 5, y + 5);

        g2.setColor(TitleScreen.MENU_SELECTED_COLOR);
        g2.drawString(text, x, y);
    }

    private static void drawFailed(Graphics2D g2) throws IOException, FontFormatException {
        Font font;
        try {
            font = FontHandler.getFont("font-1.ttf", 48f);
        } catch (FontHandlerException e) {
            throw new RuntimeException(e);
        }

        g2.setFont(font);

        //  LOGIN TEXT
        String text = "LOGIN FAILED";
        int x = getXCenteredText(text , g2);
        int y = Game.getInstance().getScreenHeight() / 2;

        g2.setColor(TitleScreen.MENU_SHADOW_COLOR);
        g2.drawString(text, x + 5, y + 5);

        g2.setColor(TitleScreen.MENU_SELECTED_COLOR);
        g2.drawString(text, x, y);
    }

    private static int getXCenteredText(String text, Graphics2D g2) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();

        return Game.getInstance().getScreenWidth() / 2 - length / 2;
    }
}
