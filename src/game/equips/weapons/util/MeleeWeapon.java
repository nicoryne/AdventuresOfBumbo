package game.equips.weapons.util;

import game.equips.weapons.Weapon;
import game.util.managers.SpritesManager;

public class MeleeWeapon extends Weapon {

    private SpritesManager spritesManager;

    public SpritesManager getSpritesManager() {
        return spritesManager;
    }

    public void setSpritesManager(SpritesManager spritesManager) {
        this.spritesManager = spritesManager;
    }
}
