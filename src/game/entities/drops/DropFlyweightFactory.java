package game.entities.drops;

import game.entities.projectile.ProjectileFlyweight;
import game.entities.projectile.ProjectileType;
import game.util.managers.SpritesManager;

import java.util.HashMap;

public abstract class DropFlyweightFactory {

    private static final HashMap<DropType, DropFlyweight> dropFlyweightHashMap = new HashMap<>();

    public static DropFlyweight getFlyweight(DropType dropType) {
        return dropFlyweightHashMap.get(dropType);
    }

    public static void addDropFlyweight(DropType type, SpritesManager spritesManager) {
        DropFlyweight dropFlyweight = new DropFlyweight(type, spritesManager);
        dropFlyweightHashMap.put(type, dropFlyweight);
    }

    public static void initializeFlyweightDrops() {
        addDropFlyweight(DropType.STANDARD_EXP, new SpritesManager("drops/standard_exp", 0, 0.8));
    }
}
