package game.ui.titlescreen;

import game.Game;
import game.util.handlers.FontHandler;
import game.util.handlers.ImageHandler;
import services.models.User;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public abstract class LeaderboardScreen {

    private static final Color LEADERBOARD_A_COLOR = new Color(36, 12, 28);

    private static final Color LEADERBOARD_B_COLOR = new Color(46, 18, 37);

    private static final Color TITLE_MAIN_COLOR = new Color(102, 33, 55);

    private static User currentUser;

    private static ArrayList<User> users;

    private static int menuCounter = 0;

    private static int menuItems;

    public static void draw(Graphics2D g2, int menuCounter) {
        currentUser = Game.getInstance().getUser();
        g2.drawImage(getBackgroundImage(), 0, 0, Game.getInstance().getScreenWidth(), Game.getInstance().getScreenHeight(), null);
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

        g2.setColor(LEADERBOARD_A_COLOR);
        g2.drawRect(x, y, width, height);
        g2.setColor(Color.GRAY);
        g2.fillRect(x, y, width, height);

        height = 64;
        drawPlayerProfile(x, y, width, height, g2, LEADERBOARD_A_COLOR, currentUser);
        y+=64;
        drawPlayerProfile(x, y, width, height, g2, LEADERBOARD_B_COLOR, currentUser);
        y+=64;
        drawPlayerProfile(x, y, width, height, g2, LEADERBOARD_A_COLOR, currentUser);
        y+=64;
        drawPlayerProfile(x, y, width, height, g2, LEADERBOARD_B_COLOR, currentUser);
        y+=128;
        drawPlayerProfile(x, y, width, height, g2, LEADERBOARD_A_COLOR, currentUser);
    }

    private static void drawBackButton(Graphics2D g2) {
        String text = "<";
        Font font = FontHandler.getFont("font-1.ttf", 48f);
        g2.setFont(font);

        int x = Game.getInstance().getScreenWidth() / 14;
        int y = Game.getInstance().getScreenHeight() / 10;

        // shadowing
        g2.setColor(LEADERBOARD_A_COLOR);
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
        g2.setColor(LEADERBOARD_A_COLOR);
        g2.drawString(text, x, y);

        // main color
        g2.setColor(TITLE_MAIN_COLOR);
        g2.drawString(text, x, y);
    }

    private static void drawPlayerProfile(int x, int y, int width, int height, Graphics2D g2, Color bgColor, User user) {
        Font font = FontHandler.getFont("font-1.ttf", 32f);
        g2.setFont(font);
        g2.setColor(bgColor);
        g2.drawRect(x, y, width, height);
        g2.setColor(bgColor);
        g2.fillRect(x, y, width, height);

        String name = user.getUsername();
        String birthday = user.getBirthday().toString();

        x += 16;
        y += 32;
        g2.setColor(Color.white);
        g2.drawString(name, x, y );
        g2.drawString(birthday, x * 4, y);
    }


    private static int getXCenteredText(String text, Graphics2D g2) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();

        return Game.getInstance().getScreenWidth() / 2 - length / 2;
    }

    private static Image getBackgroundImage() {
        String path = "src/res/img/leaderboard_bg.gif";
        return Objects.requireNonNull(ImageHandler.getImageIcon(path)).getImage();
    }

    public static int getMenuCounter() {
        return menuCounter;
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
}
