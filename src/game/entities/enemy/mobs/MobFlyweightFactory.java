package game.entities.enemy.mobs;

import game.util.managers.SpritesManager;

import java.util.HashMap;

public class MobFlyweightFactory {

    private static final HashMap<MobName, MobFlyweight> mobFlyweightHashmap = new HashMap<>();

    public static MobFlyweight getFlyweight(MobName mobName) {
        return mobFlyweightHashmap.get(mobName);
    }

    public static void addMobFlyweight(MobName mobName, SpritesManager spritesManager) {
        MobFlyweight mobFlyweight = new MobFlyweight(mobName, spritesManager);
        mobFlyweightHashmap.put(mobName, mobFlyweight);
    }

    public static void initializeFlyweightEnemies() {
        addMobFlyweight(MobName.BUMBO, new SpritesManager("enemies/bumbo", 3, 1));
        addMobFlyweight(MobName.CHORTLE, new SpritesManager("enemies/chortle", 2, 1));
        addMobFlyweight(MobName.SLIME, new SpritesManager("enemies/slime", 2, 1));
    }
}
