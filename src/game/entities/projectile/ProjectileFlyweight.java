package game.entities.projectile;

import game.util.managers.SpritesManager;

public class ProjectileFlyweight {

    private final ProjectileType projectileType;

    private final SpritesManager spritesManager;

    public ProjectileFlyweight(ProjectileType projectileType, SpritesManager spritesManager) {
        this.projectileType = projectileType;
        this.spritesManager = spritesManager;
    }

    public ProjectileType getProjectileType() {
        return projectileType;
    }

    public SpritesManager getSpritesManager() {
        return spritesManager;
    }
}
