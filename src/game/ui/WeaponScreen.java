package game.ui;

import game.Game;
import game.util.handlers.ImageHandler;
import game.util.managers.FontManager;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

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

        BufferedImage mage = ImageHandler.getBufferedImage(new File("src/res/sprites/mage/idle/mage-idle-south-01.png"));
        mage = ImageHandler.scaleImageBasedOnTileSize(mage, 3);
        y += tileSize * 2;

        int xMage = getXCenteredText("Staff", g2);

        if(menuCounter == 2) {
            drawWeaponSelection(xMage, y, mage, g2, "Staff", Color.gray);
        } else {
            drawWeaponSelection(xMage, y, mage, g2, "Staff", Color.white);
        }

        BufferedImage warrior = ImageHandler.getBufferedImage(new File("src/res/sprites/warrior/idle/warrior-idle-south-00.png"));
        warrior = ImageHandler.scaleImageBasedOnTileSize(warrior, 3);
        int xWarrior = getXCenteredText("Sword", g2) + (xMage - width - (tileSize * 2)) / 2;
        if(menuCounter == 1) {
            drawWeaponSelection(xWarrior, y, warrior, g2, "Sword", Color.gray);
        } else {
            drawWeaponSelection(xWarrior, y, warrior, g2, "Sword", Color.white);
        }

        BufferedImage archer = ImageHandler.getBufferedImage(new File("src/res/sprites/archer/idle/archer-idle-south-00.png"));
        archer = ImageHandler.scaleImageBasedOnTileSize(archer, 3);

        int xArcher = getXCenteredText("Bow", g2) - (xMage - width + (tileSize * 3)) / 2;

        if(menuCounter == 3) {
            drawWeaponSelection(xArcher, y, archer, g2, "Bow", Color.gray);
        } else {
            drawWeaponSelection(xArcher, y, archer, g2, "Bow", Color.white);
        }
        x = tileSize;
        y = tileSize;
        Font font = FontManager.getInstance().getFont("Dofded", 48f);
        g2.setFont(font);
        if(menuCounter == 0) {
            g2.setColor(Color.gray);
            g2.drawString("<", x, y);
        } else {
            g2.setColor(Color.white);
            g2.drawString("<", x, y);
        }

        font = FontManager.getInstance().getFont("Dofded", 36f);
        g2.setFont(font);
        g2.setColor(Color.white);
        String text = "Select your weapon";
        x = getXCenteredText(text, g2);
        y = (Game.getInstance().getScreenHeight() / 2) - tileSize;
        g2.drawString(text, x, y);
    }

    private static void drawWeaponSelection(int x, int y, BufferedImage sprite, Graphics2D g2, String name, Color strokeColor) {
        int tileSize = Integer.parseInt(Game.getInstance().getProperty("TILE_SIZE"));
        Font font = FontManager.getInstance().getFont("Dofded", 24f);
        g2.setFont(font);
        g2.drawImage(sprite, x, y, null);
        g2.setColor(strokeColor);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x , y, sprite.getWidth(), sprite.getHeight() , 35, 35);
        int stringY = y + tileSize / 2;
        int length = (int) g2.getFontMetrics().getStringBounds(name, g2).getWidth();
        int stringX = x + (sprite.getWidth() / 2) - (length / 2);

        g2.drawString(name, stringX, stringY);
    }



    private static int getXCenteredText(String text, Graphics2D g2) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();

        return Game.getInstance().getScreenWidth() / 2 - length / 2;
    }
}
