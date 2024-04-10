package classes.entities.enemies;

import classes.util.managers.SpritesManager;

import java.util.HashMap;

public class EnemyFlyweightFactory {

    private static final HashMap<EnemyType, EnemyFlyweight> enemyFlyweightHashmap = new HashMap<>();

    public static EnemyFlyweight getFlyweight(EnemyType enemyType) {
        return enemyFlyweightHashmap.get(enemyType);
    }

    public static void addProjectFlyweight(EnemyType type, SpritesManager spritesManager) {
        EnemyFlyweight enemyFlyweight = new EnemyFlyweight(type, spritesManager);
        enemyFlyweightHashmap.put(type, enemyFlyweight);
    }

    public static void initializeFlyweightEnemies() {
        addProjectFlyweight(EnemyType.BUMBO, new SpritesManager("enemies/bumbo", 3));
    }
}
