package classes.entities.player;

import classes.entities.EntityObject;
import classes.entities.util.ControllableEntity;
import classes.entities.util.SpriteFilledEntity;
import classes.equips.weapons.Bow;
import classes.equips.weapons.Weapon;
import classes.equips.weapons.util.RangedWeapon;
import classes.util.handlers.CollisionHandler;
import classes.util.controllers.KeyboardController;
import classes.util.controllers.MouseController;
import classes.util.managers.SpritesManager;
import classes.util.handlers.SoundHandler;

import java.awt.*;
import java.awt.image.BufferedImage;


public class Player<W extends Weapon> extends EntityObject implements SpriteFilledEntity, ControllableEntity {
    private KeyboardController keyboardController;
    private MouseController mouseController;
    private SpritesManager spritesManager;
    private W weapon;

    public Player() {}

    @Override
    public void update() {
        move();
        look();
        attack();
        weapon.incrementReloadCooldown();
    }

    @Override
    public void render(Graphics2D g2) {
        BufferedImage sprite = spritesManager.getCurrentSprite(getMovementComponent().getDirection());

        int screenPositionX = getPositionComponent().getScreenPositionX().intValue();
        int screenPositionY = getPositionComponent().getScreenPositionY().intValue();

        g2.drawImage(sprite, screenPositionX, screenPositionY,null);
    }

    private void move() {
        boolean validKey = keyboardController.isUpPressed()
                || keyboardController.isDownPressed()
                || keyboardController.isLeftPressed()
                || keyboardController.isRightPressed();


        if (validKey) {
            if (keyboardController.isUpPressed()) {
                getMovementComponent().setDirection("NORTH");
            }
            if (keyboardController.isDownPressed()) {
                getMovementComponent().setDirection("SOUTH");
            }
            if (keyboardController.isLeftPressed()) {
                getMovementComponent().setDirection("WEST");
            }
            if (keyboardController.isRightPressed()) {
                getMovementComponent().setDirection("EAST");
            }


            getMovementComponent().setColliding(false);
            CollisionHandler.checkTileCollision(this);

            if(!getMovementComponent().isColliding()) {
                int worldPositionY = getPositionComponent().getWorldPositionY().intValue();
                int worldPositionX = getPositionComponent().getWorldPositionX().intValue();
                int speed = getMovementComponent().getSpeed();

                switch(getMovementComponent().getDirection()) {
                    case "NORTH":
                        getPositionComponent().setWorldPositionY(worldPositionY - speed);
                        break;
                    case "SOUTH":
                        getPositionComponent().setWorldPositionY(worldPositionY + speed);
                        break;
                    case "WEST":
                        getPositionComponent().setWorldPositionX(worldPositionX - speed);
                        break;
                    case "EAST":
                        getPositionComponent().setWorldPositionX(worldPositionX + speed);
                        break;
                }
            } else {
                SoundHandler.playAudio("bump-1", 0, 1.0f);
            }

            spritesManager.updateSprite();
        }
    }

    private void look() {
        int screenPositionX = getPositionComponent().getScreenPositionX().intValue();
        int screenPositionY = getPositionComponent().getScreenPositionY().intValue();

        int dx = screenPositionX - mouseController.getMousePositionX();
        int dy = screenPositionY - mouseController.getMousePositionY();

        double angle = (Math.atan2(dy, dx)) - Math.PI / 2.0;

        getMovementComponent().setAngle(angle);
    }

    private void attack() {
        boolean validKey = keyboardController.isAttacking();
        double screenX = getPositionComponent().getScreenPositionX().doubleValue() + getRenderComponent().getHitbox().getWidth();
        double screenY = getPositionComponent().getScreenPositionY().doubleValue() + getRenderComponent().getHitbox().getHeight();
        double angle = getMovementComponent().getAngle();
        double worldPositionX = getPositionComponent().getWorldPositionX().doubleValue();
        double worldPositionY = getPositionComponent().getWorldPositionY().doubleValue();
        String direction = getMovementComponent().getDirection();

        if (validKey) {
            weapon.attack(angle, screenX, screenY, worldPositionX, worldPositionY, direction);
            System.out.println(weapon.getWeaponName());
        }
    }

    @Override
    public void setSpritesManager(SpritesManager spritesManager) {
        this.spritesManager = spritesManager;
    }

    @Override
    public void setKeyboardController(KeyboardController keyboardController) {
        this.keyboardController = keyboardController;
    }

    @Override
    public void setMouseController(MouseController mouseController) {
        this.mouseController = mouseController;
    }

    public W getWeapon() {
        return weapon;
    }

    public void setWeapon(W weapon) {
        this.weapon = weapon;
    }
}