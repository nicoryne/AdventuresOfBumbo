package game.entities.projectile;

import game.util.managers.SpritesManager;

import java.util.HashMap;

public abstract class ProjectileFlyweightFactory {

    private static final HashMap<ProjectileType, ProjectileFlyweight> projectileFlyweightHashMap = new HashMap<>();

    public static ProjectileFlyweight getFlyweight(ProjectileType projectileType) {
        return projectileFlyweightHashMap.get(projectileType);
    }

    public static void addProjectileFlyweight(ProjectileType type, SpritesManager spritesManager) {
        ProjectileFlyweight projectileFlyweight = new ProjectileFlyweight(type, spritesManager);
        projectileFlyweightHashMap.put(type, projectileFlyweight);
    }

    public static void initializeFlyweightProjectiles() {
        addProjectileFlyweight(ProjectileType.ARROW, new SpritesManager("projectiles/arrow", 0, 1));
        addProjectileFlyweight(ProjectileType.ORB, new SpritesManager("projectiles/test", 0, 1));
        addProjectileFlyweight(ProjectileType.FIREBALL, new SpritesManager("projectiles/fireball", 5, 1));
    }

}
