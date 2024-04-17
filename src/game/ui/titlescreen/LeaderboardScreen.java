package game.ui.titlescreen;

import game.Game;
import game.util.handlers.FontHandler;
import services.models.User;

import java.awt.*;
import java.util.ArrayList;

public abstract class LeaderboardScreen {

    private static final Color TITLE_SHADOW_COLOR = new Color(36, 12, 28);

    private static final Color TITLE_MAIN_COLOR = new Color(102, 33, 55);

    private static User currentUser;

    private static ArrayList<User> users;

    public static void draw(Graphics2D g2, int menuCounter) {
        currentUser = Game.getInstance().getUser();
        drawTitle(g2);
        drawBackButton(g2);
        drawBodyContainer(g2);
    }

    private static void drawBodyContainer(Graphics2D g2) {
        Font font = FontHandler.getFont("font-1.ttf", 32f);

        int tileSize = Integer.parseInt(Game.getInstance().getProperty("TILE_SIZE"));
        int x = (Game.getInstance().getScreenWidth() / 14);
        int y = (Game.getInstance().getScreenHeight() / 6);
        int width = (Game.getInstance().getScreenWidth() - (tileSize * 2));
        int height = (Game.getInstance().getScreenHeight() - (tileSize * 4));

        g2.setColor(TITLE_SHADOW_COLOR);
        g2.drawRoundRect(x, y, width, height, 45, 45);
        g2.setColor(Color.GRAY);
        g2.fillRoundRect(x, y, width, height, 45, 45);

        String text = String.valueOf(currentUser.getAge());
        g2.setColor(Color.WHITE);
        g2.setFont(font);

        x = getXCenteredText(text, g2);
        y = (Game.getInstance().getScreenHeight() / 2);

        // Draw the text
        g2.drawString(text, x, y);
    }

    private static void drawBackButton(Graphics2D g2) {
        String text = "<";
        Font font = FontHandler.getFont("font-1.ttf", 48f);
        g2.setFont(font);

        int x = Game.getInstance().getScreenWidth() / 14;
        int y = Game.getInstance().getScreenHeight() / 10;

        // shadowing
        g2.setColor(TITLE_SHADOW_COLOR);
        g2.drawString(text, x, y);

        // main color
        g2.setColor(TITLE_MAIN_COLOR);
        g2.drawString(text, x, y);
    }

    private static void drawTitle(Graphics2D g2)  {
        String text = "LEADERBOARD";
        Font font = FontHandler.getFont("font-1.ttf", 48f);
        g2.setFont(font);

        int x = getXCenteredText(text, g2);
        int y = Game.getInstance().getScreenHeight() / 10;

        // shadowing
        g2.setColor(TITLE_SHADOW_COLOR);
        g2.drawString(text, x, y);

        // main color
        g2.setColor(TITLE_MAIN_COLOR);
        g2.drawString(text, x, y);
    }


    private static int getXCenteredText(String text, Graphics2D g2) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();

        return Game.getInstance().getScreenWidth() / 2 - length / 2;
    }
}
