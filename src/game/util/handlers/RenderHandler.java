package game.util.handlers;

import game.Game;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class RenderHandler {

    public static boolean isViewableOnScreen(double worldPositionX, double worldPositionY) {
        int tileSize = Integer.parseInt(Game.getInstance().getProperty("TILE_SIZE"));

        double playerViewableMinX = Game.getInstance().getPlayerMinViewableX();
        double playerViewableMinY = Game.getInstance().getPlayerMinViewableY();
        double playerViewableMaxX = Game.getInstance().getPlayerMaxViewableX();
        double playerViewableMaxY = Game.getInstance().getPlayerMaxViewableY();

        return worldPositionX + tileSize > playerViewableMinX &&
                worldPositionX - tileSize < playerViewableMaxX &&
                worldPositionY + tileSize > playerViewableMinY &&
                worldPositionY - tileSize < playerViewableMaxY;
    }

    public static void renderOnScreen(double worldPositionX, double worldPositionY, BufferedImage image, Graphics2D g2) {
        int screenX = calculateScreenX(worldPositionX).intValue();
        int screenY = calculateScreenY(worldPositionY).intValue();

        g2.drawImage(image, screenX, screenY, null);
    }

    private static Number calculateScreenX(double worldX) {
        return (worldX - Game.getInstance().getPlayerMinViewableX());
    }

    private static Number calculateScreenY(double worldY) {
        return (worldY - Game.getInstance().getPlayerMinViewableY());
    }
}
