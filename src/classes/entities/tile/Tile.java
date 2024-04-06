package classes.entities.tile;

import classes.entities.EntityObject;
import classes.entities.EntityType;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile extends EntityObject {
    private final boolean isCollidable;

    public Tile(BufferedImage image, boolean isCollidable) {
        this.setEntityType(EntityType.TILE);
        this.getRenderComponent().setSprite(image);
        this.isCollidable = isCollidable;

        if(isCollidable) {
            this.getRenderComponent().setHitbox(new Rectangle(16, 16));
        }
    }


    public boolean isCollidable() {
        return isCollidable;
    }
}
