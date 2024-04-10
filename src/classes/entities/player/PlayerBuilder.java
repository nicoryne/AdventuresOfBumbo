package classes.entities.player;

import classes.entities.EntityType;
import classes.entities.CharacterBuilder;
import classes.equips.weapons.Weapon;
import classes.util.Directions;
import classes.util.controllers.KeyboardController;
import classes.util.controllers.MouseController;
import classes.util.managers.SpritesManager;

import java.awt.*;

public class PlayerBuilder<W extends Weapon> implements CharacterBuilder {

    private Player<W> player;

    @Override
    public void reset() {
        this.player = new Player<>();
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
    public void setEntitySpeed(int entitySpeed) {
        this.player.getMovementComponent().setEntitySpeed(entitySpeed);
    }

    @Override
    public void setDirection(Directions direction) {
        this.player.getMovementComponent().setDirection(direction);
    }

    @Override
    public void setHitPoints(double hitPoints) {
        this.player.getStatComponent().setMaxHitPoints(hitPoints);
        this.player.getStatComponent().setHitPoints(hitPoints);
    }

    @Override
    public void setMana(double mana) {
        this.player.getStatComponent().setMaxMana(mana);
        this.player.getStatComponent().setMaxHitPoints(mana);
    }

    @Override
    public void setStrength(double strength) {
        this.player.getAttributeComponents().setStrength(strength);
    }

    @Override
    public void setAgility(double agility) {
        this.player.getAttributeComponents().setAgility(agility);

    }

    @Override
    public void setIntelligence(double intelligence) {
        this.player.getAttributeComponents().setIntelligence(intelligence);
    }

    @Override
    public void setSpeed(int speed) {
        this.player.getStatComponent().setSpeed(speed);
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

    public void setWeapon(W weapon) {
        this.player.setWeapon(weapon);
    }


    public Player<W> build() {
        return this.player;
    }
}
