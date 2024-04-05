package classes.entities.components;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RenderComponent {

    private BufferedImage sprite;

    private Rectangle hitbox;

    private boolean isAlive;

    public BufferedImage getSprite() {
        return sprite;
    }

    public void setSprite(BufferedImage sprite) {
        this.sprite = sprite;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
    }
}
