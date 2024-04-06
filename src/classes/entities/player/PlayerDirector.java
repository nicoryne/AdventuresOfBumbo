package classes.entities.player;

import classes.Game;
import classes.entities.EntityType;
import classes.util.controllers.KeyboardController;
import classes.util.controllers.MouseController;
import classes.util.managers.SpritesManager;

import java.awt.*;

public class PlayerDirector {

    public void constructPlayer(PlayerBuilder builder, KeyboardController keyboardController, MouseController mouseController) {
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
        builder.setSpritesManager(new SpritesManager("player", 10, 3));
        builder.setKeyboardController(keyboardController);
        builder.setMouseController(mouseController);
    }

}
