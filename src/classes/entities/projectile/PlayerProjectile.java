package classes.entities.projectile;

import classes.ui.components.GamePanel;

public class PlayerProjectile extends ProjectilePrototype {
    public PlayerProjectile(GamePanel gamePanel, double screenPositionX, double screenPositionY, double angle) {
        super(gamePanel, screenPositionX, screenPositionY, angle);
    }
}
