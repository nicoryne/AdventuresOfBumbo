package classes.entities.player;

import classes.entities.CharacterEntity;
import classes.entities.MovingEntity;
import classes.entities.util.ControllableEntity;
import classes.entities.util.SpriteFilledEntity;
import classes.equips.weapons.Weapon;
import classes.util.Directions;
import classes.util.handlers.CollisionHandler;
import classes.util.controllers.KeyboardController;
import classes.util.controllers.MouseController;
import classes.util.managers.SpritesManager;
import classes.util.handlers.SoundHandler;

import java.awt.*;
import java.awt.image.BufferedImage;


public class Player<W extends Weapon> extends CharacterEntity implements SpriteFilledEntity, ControllableEntity {
    private KeyboardController keyboardController;
    private MouseController mouseController;
    private SpritesManager spritesManager;
    private W weapon;

    @Override
    public void update() {
        CollisionHandler.checkEnemyCollision(this);
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
            handleDirections();
            int speed = calculateSpeed();
            int diagonalSpeed = (int) (speed / Math.sqrt(2));
            getMovementComponent().setColliding(false);
            CollisionHandler.checkTileCollision(this, speed);

            if(!getMovementComponent().isColliding()) {
                int worldPositionY = getPositionComponent().getWorldPositionY().intValue();
                int worldPositionX = getPositionComponent().getWorldPositionX().intValue();

                handleMovement(worldPositionX, worldPositionY, speed, diagonalSpeed);
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
        Directions direction = getMovementComponent().getDirection();

        if (validKey) {
            weapon.attack(angle, screenX, screenY, worldPositionX, worldPositionY, direction);
        }
    }

    private int calculateSpeed() {
        return (int) (getMovementComponent().getEntitySpeed() + (( getStatComponent().getSpeed() / 100) + getAttributeComponents().getAgility() / 10));
    }

    private void handleDirections() {
        boolean up = keyboardController.isUpPressed();
        boolean down = keyboardController.isDownPressed();
        boolean left = keyboardController.isLeftPressed();
        boolean right = keyboardController.isRightPressed();

        int directionCode = (up ? 1 : 0) | (down ? 2 : 0) | (left ? 4 : 0) | (right ? 8 : 0);

        switch (directionCode) {
            case 1:
                getMovementComponent().setDirection(Directions.NORTH);
                break;
            case 2:
                getMovementComponent().setDirection(Directions.SOUTH);
                break;
            case 4:
                getMovementComponent().setDirection(Directions.WEST);
                break;
            case 8:
                getMovementComponent().setDirection(Directions.EAST);
                break;
            case 9:
                getMovementComponent().setDirection(Directions.NORTH_EAST);
                break;
            case 5:
                getMovementComponent().setDirection(Directions.NORTH_WEST);
                break;
            case 10:
                getMovementComponent().setDirection(Directions.SOUTH_EAST);
                break;
            case 6:
                getMovementComponent().setDirection(Directions.SOUTH_WEST);
                break;
            default:
                getMovementComponent().setDirection(Directions.NONE);
                break;
        }
    }

    private void handleMovement(int worldPositionX, int worldPositionY, int speed, int diagonalSpeed) {
        switch (getMovementComponent().getDirection()) {
            case Directions.NORTH_EAST:
                getPositionComponent().setWorldPositionY(worldPositionY - diagonalSpeed);
                getPositionComponent().setWorldPositionX(worldPositionX + diagonalSpeed);
                break;
            case Directions.NORTH_WEST:
                getPositionComponent().setWorldPositionY(worldPositionY - diagonalSpeed);
                getPositionComponent().setWorldPositionX(worldPositionX - diagonalSpeed);
                break;
            case Directions.SOUTH_EAST:
                getPositionComponent().setWorldPositionY(worldPositionY + diagonalSpeed);
                getPositionComponent().setWorldPositionX(worldPositionX + diagonalSpeed);
                break;
            case Directions.SOUTH_WEST:
                getPositionComponent().setWorldPositionY(worldPositionY + diagonalSpeed);
                getPositionComponent().setWorldPositionX(worldPositionX - diagonalSpeed);
                break;
            case Directions.NORTH:
                getPositionComponent().setWorldPositionY(worldPositionY - speed);
                break;
            case Directions.SOUTH:
                getPositionComponent().setWorldPositionY(worldPositionY + speed);
                break;
            case Directions.WEST:
                getPositionComponent().setWorldPositionX(worldPositionX - speed);
                break;
            case Directions.EAST:
                getPositionComponent().setWorldPositionX(worldPositionX + speed);
                break;
        }
    }

    private void showHitbox(Graphics2D g2, int screenPositionX, int screenPositionY) {
        Rectangle hitbox = getRenderComponent().getHitbox();
        g2.setColor(Color.RED); // Set the color of the outline
        g2.drawRect((int) (screenPositionX + hitbox.getX()), (int) (screenPositionY + hitbox.getY()), (int) hitbox.getWidth(), (int) hitbox.getHeight());
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