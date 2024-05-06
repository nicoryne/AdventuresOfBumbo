package game.util;

import game.util.managers.SpritesManager;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class PlayerSpritesFactory {

    public enum PlayerSprites {
        MAGE, WARRIOR, ARCHER
    }
    private static HashMap<PlayerSprites, ArrayList<SpritesManager>> spriteMap;
    public static void load() {
        spriteMap = new HashMap<>();
        spriteMap.put(PlayerSprites.MAGE, createSpriteList(PlayerSprites.MAGE));
        spriteMap.put(PlayerSprites.WARRIOR, createSpriteList(PlayerSprites.WARRIOR));
        spriteMap.put(PlayerSprites.ARCHER, createSpriteList(PlayerSprites.ARCHER));
    }

    public static ArrayList<SpritesManager> getPlayerSprites(PlayerSprites playerSprites) {
        return spriteMap.get(playerSprites);
    }

    private static ArrayList<SpritesManager> createSpriteList(PlayerSprites playerSprites) {
        ArrayList<SpritesManager> spriteList = new ArrayList<>();
        String path = "";
        switch(playerSprites) {
            case MAGE -> path = "mage/";
            case ARCHER -> path = "archer/";
            case WARRIOR -> path = "warrior/";
        }

        spriteList.add(new SpritesManager(path + "movement", 3, 1));
        spriteList.add(new SpritesManager(path + "idle", 2, 1));
        spriteList.add(new SpritesManager(path + "damaged", 3, 1));
        spriteList.add(new SpritesManager(path + "dying", 2, 1));
        spriteList.add(new SpritesManager(path + "attack", 4, 1));

        return spriteList;
    }
}
