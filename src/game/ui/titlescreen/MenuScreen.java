package game.ui.titlescreen;

import game.Game;
import game.util.managers.FontManager;

import java.awt.*;

public abstract class MenuScreen {
    //TODO: maybe add splash screen text (minecraft style)

    public static void draw(Graphics2D g2, int menuCounter) {
        Font font = FontManager.getInstance().getFont("Dofded", 48f);

        g2.setFont(font);

        //  PLAY
        String text = "PLAY";
        int x = getXCenteredText(text , g2);
        int y = Game.getInstance().getScreenHeight() / 2;

        g2.setColor(TitleScreen.MENU_SHADOW_COLOR);
        g2.drawString(text, x + 5, y + 5);

        if (menuCounter == 0) {
            g2.setColor(TitleScreen.MENU_SELECTED_COLOR);
            g2.drawString(">", x - TitleScreen.tileSize, y);
        } else {
            g2.setColor(TitleScreen.MENU_MAIN_COLOR);
        }

        g2.drawString(text, x, y);

        // LEADERBOARD
        text = "LEADERBOARD";
        x = getXCenteredText(text , g2);
        y += TitleScreen.tileSize * 2;

        g2.setColor(TitleScreen.MENU_SHADOW_COLOR);
        g2.drawString(text, x + 5, y + 5);

        if(menuCounter == 1) {
            g2.setColor(TitleScreen.MENU_SELECTED_COLOR);
            g2.drawString(">", x - TitleScreen.tileSize, y);
        } else {
            g2.setColor(TitleScreen.MENU_MAIN_COLOR);
        }

        g2.drawString(text, x, y);

        // EXIT TEXT
        text = "EXIT";
        x = getXCenteredText(text , g2);
        y += TitleScreen.tileSize * 2;

        g2.setColor(TitleScreen.MENU_SHADOW_COLOR);
        g2.drawString(text, x + 5, y + 5);

        if(menuCounter == 2) {
            g2.setColor(TitleScreen.MENU_SELECTED_COLOR);
            g2.drawString(">", x - TitleScreen.tileSize, y);
        } else {
            g2.setColor(TitleScreen.MENU_MAIN_COLOR);
        }

        g2.drawString(text, x, y);
    }


    private static int getXCenteredText(String text, Graphics2D g2) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();

        return Game.getInstance().getScreenWidth() / 2 - length / 2;
    }

}
