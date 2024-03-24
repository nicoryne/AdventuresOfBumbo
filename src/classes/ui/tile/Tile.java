package classes.ui.tile;

import java.awt.image.BufferedImage;

public class Tile {

    private final BufferedImage image;

    private boolean isCollidable;

    public Tile(BufferedImage image, boolean isSolid) {
        this.image = image;
        this.isCollidable = isSolid;
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

}
