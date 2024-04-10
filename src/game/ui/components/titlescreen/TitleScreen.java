package game.ui.components.titlescreen;

import game.Game;
import game.exceptions.FontHandlerException;
import game.util.handlers.FontHandler;
import game.util.handlers.ImageHandler;

import java.awt.*;
import java.io.IOException;
import java.util.Objects;

public abstract class TitleScreen {

    public enum TitleScreenState {
        BOARDING, LOGIN, MAIN
    }

    private static final Color TITLE_SHADOW_COLOR = new Color(36, 12, 28);

    private static final Color TITLE_MAIN_COLOR = new Color(102, 33, 55);

    protected static final Color MENU_SHADOW_COLOR = new Color(36, 12, 28);

    protected static final Color MENU_MAIN_COLOR = new Color(255, 255, 255);

    protected static final Color MENU_SELECTED_COLOR = new Color(72, 60, 63, 255);

    private static int menuCounter = 0;

    private static int menuItems;

    protected static final int tileSize = Integer.parseInt(Game.getInstance().getProperty("TILE_SIZE"));

    private static TitleScreenState titleState = TitleScreenState.BOARDING;

    public static void draw(Graphics2D g2) throws IOException, FontFormatException {
        // background
        g2.drawImage(getBackgroundImage(), 0, 0, Game.getInstance().getScreenWidth(), Game.getInstance().getScreenHeight(), null);

        // title name text
        drawTitle(g2);

        // login text
        if(titleState == TitleScreenState.BOARDING) {
            BoardingScreen.draw(g2, menuCounter);
            menuItems = 2;
        } else if (titleState == TitleScreenState.LOGIN) {
            LoginScreen.draw(g2);
            menuItems = 0;
        }
    }

    private static Image getBackgroundImage() {
        String path = "src/res/img/title_bg.gif";
        return Objects.requireNonNull(ImageHandler.getImageIcon(path)).getImage();
    }

    private static void drawTitle(Graphics2D g2) throws IOException, FontFormatException {
        String text = "BUMBO HELL";
        Font font;
        try {
            font = FontHandler.getFont("font-1.ttf", 84f);
        } catch (FontHandlerException e) {
            throw new RuntimeException(e);
        }

        g2.setFont(font);
        int x = getXCenteredText(text, g2);
        int y = Game.getInstance().getScreenHeight() / 4;

        // shadowing
        g2.setColor(TITLE_SHADOW_COLOR);
        g2.drawString(text, x + 5, y + 5);

            // main color
        g2.setColor(TITLE_MAIN_COLOR);
        g2.drawString(text, x, y);
    }

    public static void incrementMenuItem() {
        if(menuCounter < menuItems) {
                menuCounter++;
        }
    }

    public static void decrementMenuItem() {
        if(menuCounter > 0) {
                menuCounter--;
        }
    }

    private static int getXCenteredText(String text, Graphics2D g2) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();

        return Game.getInstance().getScreenWidth() / 2 - length / 2;
    }

    public static int getMenuCounter() {
        return menuCounter;
    }

    public static TitleScreenState getTitleState() {
        return titleState;
    }

    public static void setTitleState(TitleScreenState titleState) {
        TitleScreen.titleState = titleState;
    }
}
