package classes.util;

import classes.ui.components.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RenderHelper {
    private final GamePanel gamePanel;

    public RenderHelper(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public boolean isViewableOnScreen(double worldPositionX, double worldPositionY) {
        int tileSize = gamePanel.getTileSize();
        double playerViewableMinX = gamePanel.getPlayerViewableMinX();
        double playerViewableMinY = gamePanel.getPlayerViewableMinY();
        double playerViewableMaxX = gamePanel.getPlayerViewableMaxX();
        double playerViewableMaxY = gamePanel.getPlayerViewableMaxY();

        return worldPositionX + tileSize > playerViewableMinX &&
                worldPositionX - tileSize < playerViewableMaxX &&
                worldPositionY + tileSize > playerViewableMinY &&
                worldPositionY - tileSize < playerViewableMaxY;
    }

    public void renderOnScreen(double worldPositionX, double worldPositionY, BufferedImage image, Graphics2D g) {
        int screenX = (int) calculateScreenX(worldPositionX);
        int screenY = (int) calculateScreenY(worldPositionY);
        int tileSize = gamePanel.getTileSize();

        g.drawImage(image, screenX, screenY, tileSize, tileSize, null);
    }

    private double calculateScreenX(double worldX) {
        double playerViewableMinX = gamePanel.getPlayerViewableMinX();
        return worldX - playerViewableMinX;
    }

    private double calculateScreenY(double worldY) {
        double playerViewableMinY = gamePanel.getPlayerViewableMinY();
        return worldY - playerViewableMinY;
    }
}
