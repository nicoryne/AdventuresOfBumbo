package game.ui.titlescreen;

import game.Game;
import game.ui.WeaponScreen;
import game.util.handlers.ImageHandler;
import game.util.managers.FontManager;

import java.awt.*;
import java.util.Objects;

public abstract class TitleScreen {

    public enum TitleScreenState {
        BOARDING, MENU, LEADERBOARD, WEAPON_SELECTION, PLAYING
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

    public static void draw(Graphics2D g2) {

        if(titleState != TitleScreenState.PLAYING && titleState != TitleScreenState.LEADERBOARD) {
            // background
            g2.drawImage(getBackgroundImage(), 0, 0, Game.getInstance().getScreenWidth(), Game.getInstance().getScreenHeight(), null);

            // title name text
            drawTitle(g2);
        }

        switch(titleState) {
            case BOARDING:
                BoardingScreen.draw(g2, menuCounter);
                menuItems = 2;
                break;
            case MENU:
                MenuScreen.draw(g2, menuCounter);
                menuItems = 2;
                break;
            case LEADERBOARD:
                LeaderboardScreen.draw(g2, menuCounter);
                menuItems = 6;
                break;
            case WEAPON_SELECTION:
                WeaponScreen.draw(g2, menuCounter);
                menuItems = 4;
                break;
        }
    }

    private static Image getBackgroundImage() {
        String path = "src/res/img/title_bg.gif";
        return Objects.requireNonNull(ImageHandler.getImageIcon(path)).getImage();
    }

    private static void drawTitle(Graphics2D g2)  {
        String text = "BUMBO HELL";
        Font font = FontManager.getInstance().getFont("Dofded", 84f);

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
