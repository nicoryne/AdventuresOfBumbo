package classes.entities.item;

import classes.entities.EntityObject;
import classes.ui.components.GamePanel;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ItemObject extends EntityObject {

    private String itemName;

    private final GamePanel gamePanel;

    public ItemObject(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public void update() {

    }

    @Override
    public void render(Graphics2D g) {
        double worldX = getWorldPositionX();
        double worldY = getWorldPositionY();

        if (gamePanel.getRenderHelper().isViewableOnScreen(worldX, worldY)) {
            BufferedImage image = getEntityImage();
            gamePanel.getRenderHelper().renderOnScreen(worldX, worldY, image, g);
        }
    }
}
