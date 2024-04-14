package game.entities.enemy.mobs;

import game.util.managers.SpritesManager;

public class MobFlyweight {

    private final MobName mobName;

    private final SpritesManager spritesManager;

    public MobFlyweight(MobName mobName, SpritesManager spritesManager) {
        this.spritesManager = spritesManager;
        this.mobName = mobName;
    }

    public MobName getEnemyType() {
        return mobName;
    }

    public SpritesManager getSpritesManager() {
        return spritesManager;
    }

}
