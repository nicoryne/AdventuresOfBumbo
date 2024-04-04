package classes.entities.tile;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile {
    private final BufferedImage image;
    private boolean isCollidable;
    private Rectangle solidArea;

    public Tile(BufferedImage image, boolean isCollidable) {
        this.image = image;
        this.isCollidable = isCollidable;

        if(isCollidable) {
            setSolidArea(new Rectangle(16, 16));
        }
    }

    public BufferedImage getImage() {
        return image;
    }

    public boolean isCollidable() {
        return isCollidable;
    }

    public void toggleCollidable() {
        isCollidable = !isCollidable();
    }

    public Rectangle getSolidArea() {
        return this.solidArea;
    }

    public void setSolidArea(Rectangle solidArea) {
        this.solidArea = solidArea;
    }

}
