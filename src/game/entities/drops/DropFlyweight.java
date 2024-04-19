package game.entities.drops;

import game.entities.projectile.ProjectileType;
import game.util.managers.SpritesManager;

public class DropFlyweight {

    private final DropType projectileType;

    private final SpritesManager spritesManager;

    public DropFlyweight(DropType projectileType, SpritesManager spritesManager) {
        this.projectileType = projectileType;
        this.spritesManager = spritesManager;
    }

    public DropType getProjectileType() {
        return projectileType;
    }

    public SpritesManager getSpritesManager() {
        return spritesManager;
    }
}
