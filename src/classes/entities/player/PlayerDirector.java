package classes.entities.player;

import classes.Game;
import classes.entities.EntityType;
import classes.equips.weapons.Weapon;
import classes.util.controllers.KeyboardController;
import classes.util.controllers.MouseController;
import classes.util.managers.SpritesManager;

import java.awt.*;

public class PlayerDirector {

    public <W extends Weapon> void constructPlayer(PlayerBuilder<W> builder, KeyboardController keyboardController, MouseController mouseController, W weapon) {
        Game game = Game.getInstance();
        int tileSize = Integer.parseInt(game.getProperty("TILE_SIZE"));

        builder.reset();
        builder.setEntityType(EntityType.PLAYER);
        builder.setAlive();
        builder.setScreenPositionX(game.getScreenWidth() / 2.0 - (tileSize / 2.0));
        builder.setScreenPositionY(game.getScreenHeight() / 2.0 - (tileSize / 2.0));
        builder.setHitbox(new Rectangle(16, 16, 16, 20));
        builder.setSpeed(3);
        builder.setDirection("NORTH");
        builder.setSpritesManager(new SpritesManager("player", 3));
        builder.setKeyboardController(keyboardController);
        builder.setMouseController(mouseController);
        builder.setWeapon(weapon);
    }

}
