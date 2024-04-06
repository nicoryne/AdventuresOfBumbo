package classes.entities.player;

import classes.entities.EntityObject;
import classes.entities.EntityType;
import classes.entities.EntityBuilder;
import classes.entities.projectile.ProjectilePrototype;
import classes.util.controllers.KeyboardController;
import classes.util.controllers.MouseController;
import classes.util.managers.SpritesManager;

import java.awt.*;
import java.util.ArrayList;

public class PlayerBuilder implements EntityBuilder {

    private Player player;

    public void reset() {
        this.player = new Player();
    }

    @Override
    public void setEntityType(EntityType type) {
        this.player.setEntityType(type);
    }

    @Override
    public void setScreenPositionX(double screenPositionX) {
        this.player.getPositionComponent().setScreenPositionX(screenPositionX);
    }

    @Override
    public void setScreenPositionY(double screenPositionY) {
        this.player.getPositionComponent().setScreenPositionY(screenPositionY);
    }

    @Override
    public void setAlive() {
        this.player.getRenderComponent().setAlive(true);
    }

    @Override
    public void setHitbox(Rectangle rectangle) {
        this.player.getRenderComponent().setHitbox(rectangle);
    }

    @Override
    public void setSpeed(int speed) {
        this.player.getMovementComponent().setSpeed(speed);
    }

    @Override
    public void setDirection(String direction) {
        this.player.getMovementComponent().setDirection(direction);
    }

    public void setKeyboardController(KeyboardController keyboardController) {
        this.player.setKeyboardController(keyboardController);
    }

    public void setMouseController(MouseController mouseController) {
        this.player.setMouseController(mouseController);
    }

    public void setSpritesManager(SpritesManager spritesManager) {
        this.player.setSpritesManager(spritesManager);
    }

    public void setProjectilesList(ArrayList<ProjectilePrototype> projectilesList) {
        this.player.setProjectiles(projectilesList);
    }

    public Player build() {
        return this.player;
    }
}
