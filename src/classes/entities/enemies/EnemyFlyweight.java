package classes.entities.enemies;

import classes.util.managers.SpritesManager;

public class EnemyFlyweight {

    private final EnemyType enemyType;

    private final SpritesManager spritesManager;


    public EnemyFlyweight(EnemyType enemyType, SpritesManager spritesManager) {
        this.spritesManager = spritesManager;
        this.enemyType = enemyType;
    }

    public EnemyType getEnemyType() {
        return enemyType;
    }

    public SpritesManager getSpritesManager() {
        return spritesManager;
    }

}
