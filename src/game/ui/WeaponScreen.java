package game.ui;

import game.Game;
import game.util.managers.FontManager;

import java.awt.*;

public abstract class WeaponScreen {

    public static void draw(Graphics2D g2, int menuCounter)  {
        int tileSize = Integer.parseInt(Game.getInstance().getProperty("TILE_SIZE"));
        int x = tileSize * 2;
        int y = (Game.getInstance().getScreenHeight() / 2) - (tileSize * 2);
        int width = Game.getInstance().getScreenWidth() - (tileSize * 4);
        int height = tileSize * 6;

        Color bgColor = new Color(0, 0, 0, 210);
        g2.setColor(bgColor);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        Color strokeColor = new Color(255, 255, 255);
        g2.setColor(strokeColor);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x , y, width, height , 35, 35);
    }



    private static int getXCenteredText(String text, Graphics2D g2) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();

        return Game.getInstance().getScreenWidth() / 2 - length / 2;
    }
}
