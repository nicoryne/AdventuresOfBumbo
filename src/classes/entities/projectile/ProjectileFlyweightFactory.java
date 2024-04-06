package classes.entities.projectile;

import classes.util.managers.SpritesManager;

import java.util.HashMap;

public abstract class ProjectileFlyweightFactory {

    private static final HashMap<ProjectileType, ProjectileFlyweight> projectileFlyweightHashMap = new HashMap<>();

    public static ProjectileFlyweight getFlyweight(ProjectileType projectileType) {
        return projectileFlyweightHashMap.get(projectileType);
    }

    public static void addProjectFlyweight(ProjectileType type, SpritesManager spritesManager) {
        ProjectileFlyweight projectileFlyweight = new ProjectileFlyweight(type, spritesManager);
        projectileFlyweightHashMap.put(type, projectileFlyweight);
    }

    public static void initializeFlyweightProjectiles() {
        addProjectFlyweight(ProjectileType.ARROW, new SpritesManager("projectiles/arrow", 0));
        addProjectFlyweight(ProjectileType.ORB, new SpritesManager("projectiles/test", 0));
    }

}
