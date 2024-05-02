package game.entities.player;

import game.Game;
import game.entities.CharacterEntity;
import game.entities.util.ControllableEntity;
import game.equips.weapons.Weapon;
import game.util.Directions;
import game.util.handlers.CollisionHandler;
import game.util.controllers.KeyboardController;
import game.util.controllers.MouseController;
import game.util.managers.FontManager;
import game.util.managers.SpritesManager;
import game.util.handlers.SoundHandler;

import java.awt.*;
import java.awt.image.BufferedImage;


public class Player<T extends Weapon> extends CharacterEntity implements ControllableEntity {

    private KeyboardController keyboardController;
    private SpritesManager movementSpritesManager;
    private SpritesManager idleSpritesManager;
    private SpritesManager attackSpritesManager;
    private double exp;
    private int level = 1;
    private double expToLevelUp;
    private int points;
    private T weapon;
    private boolean isAttacking;

    private int attackingAnimationCounter = 0;

    @Override
    public void update() {
        CollisionHandler.checkEnemyCollision(this);
        CollisionHandler.checkDropCollision(this);
        move();
        look();
        attack();
        weapon.incrementReloadCooldown();
        incrementTakeDamageCounter();
    }

    @Override
    public void render(Graphics2D g2) {
        BufferedImage sprite = getRenderComponent().getSprite();
        int screenPositionX = getPositionComponent().getScreenPositionX().intValue();
        int screenPositionY = getPositionComponent().getScreenPositionY().intValue();

        g2.drawImage(sprite, screenPositionX, screenPositionY,null);
        showHPBar(g2);
        showXPBar(g2);
        showPoints(g2);
    }

    private void move() {
        boolean validKey = keyboardController.isUpPressed()
                || keyboardController.isDownPressed()
                || keyboardController.isLeftPressed()
                || keyboardController.isRightPressed();

        if (validKey && getRenderComponent().isAlive()) {
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

            movementSpritesManager.updateSprite();
            getRenderComponent().setSprite(movementSpritesManager.getCurrentSprite(getMovementComponent().getDirection(), true));
        } else if (!validKey && getRenderComponent().isAlive()) {
            idleSpritesManager.updateSprite();
            getRenderComponent().setSprite(idleSpritesManager.getCurrentSprite(getMovementComponent().getDirection(), true));
        }
    }

    private void look() {
        double angle = switch (getMovementComponent().getDirection()) {
            case NORTH -> Math.toRadians(0);
            case SOUTH -> Math.toRadians(180);
            case WEST -> Math.toRadians(270);
            case EAST -> Math.toRadians(90);
            case NORTH_EAST -> Math.toRadians(45);
            case NORTH_WEST -> Math.toRadians(315);
            case SOUTH_EAST -> Math.toRadians(135);
            case SOUTH_WEST -> Math.toRadians(225);
            default -> getMovementComponent().getAngle();
        };

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

        if (validKey && weapon.canAttack()) {
            weapon.attack(angle, screenX, screenY, worldPositionX, worldPositionY, direction);
            isAttacking = true;
        }

        if(isAttacking && attackingAnimationCounter < 18) {
            attackSpritesManager.updateSprite();
            getRenderComponent().setSprite(attackSpritesManager.getCurrentSprite(getMovementComponent().getDirection(), true));
            attackingAnimationCounter++;
        } else if(isAttacking && attackingAnimationCounter == 18) {
            isAttacking = false;
            attackingAnimationCounter = 0;
        }
    }

    @Override
    public int dealDamage() {
        return weapon.getDamage();
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
            case NORTH_EAST:
                getPositionComponent().setWorldPositionY(worldPositionY - diagonalSpeed);
                getPositionComponent().setWorldPositionX(worldPositionX + diagonalSpeed);
                break;
            case NORTH_WEST:
                getPositionComponent().setWorldPositionY(worldPositionY - diagonalSpeed);
                getPositionComponent().setWorldPositionX(worldPositionX - diagonalSpeed);
                break;
            case SOUTH_EAST:
                getPositionComponent().setWorldPositionY(worldPositionY + diagonalSpeed);
                getPositionComponent().setWorldPositionX(worldPositionX + diagonalSpeed);
                break;
            case SOUTH_WEST:
                getPositionComponent().setWorldPositionY(worldPositionY + diagonalSpeed);
                getPositionComponent().setWorldPositionX(worldPositionX - diagonalSpeed);
                break;
            case NORTH:
                getPositionComponent().setWorldPositionY(worldPositionY - speed);
                break;
            case SOUTH:
                getPositionComponent().setWorldPositionY(worldPositionY + speed);
                break;
            case WEST:
                getPositionComponent().setWorldPositionX(worldPositionX - speed);
                break;
            case EAST:
                getPositionComponent().setWorldPositionX(worldPositionX + speed);
                break;
        }
    }

    private void showHitbox(Graphics2D g2, int screenPositionX, int screenPositionY) {
        Rectangle hitbox = getRenderComponent().getHitbox();
        g2.setColor(Color.RED); // Set the color of the outline
        g2.drawRect((int) (screenPositionX + hitbox.getX()), (int) (screenPositionY + hitbox.getY()), (int) hitbox.getWidth(), (int) hitbox.getHeight());
    }

    private void showXPBar(Graphics2D g2) {
        Font font = FontManager.getInstance().getFont("Dofded", 16f);
        int screenWidth = Game.getInstance().getScreenWidth() - 4;
        int expWidth = (int) ((screenWidth / expToLevelUp) * exp);
        String expString = "EXP: " + exp + " / " + expToLevelUp;

        g2.setColor(Color.WHITE);
        g2.drawRect(0, 0, screenWidth, 30);
        g2.setColor(Color.BLUE);
        g2.fillRect(0, 0, expWidth, 30);

        g2.setFont(font);
        int x = getXCenteredText(expString, g2);
        g2.setColor(Color.white);
        g2.drawString(expString, x, 20);
    }

    private void showPoints(Graphics2D g2) {
        Font font = FontManager.getInstance().getFont("Dofded", 24f);
        String pointString = "Points: " + points;

        g2.setColor(Color.WHITE);
        g2.setFont(font);
        int x = Game.getInstance().getScreenWidth() / 2;
        int y = Game.getInstance().getScreenHeight() / 8;
        g2.setColor(Color.white);
        g2.drawString(pointString, x, y);
    }

    private static int getXCenteredText(String text, Graphics2D g2) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();

        return Game.getInstance().getScreenWidth() / 2 - length / 2;
    }

    private void levelUp() {
        this.exp = 0;
        level++;
        expToLevelUp = expToLevelUp + (level * 5);
    }

    public void setMovementSpritesManager(SpritesManager movementSpritesManager) {
        this.movementSpritesManager = movementSpritesManager;
    }

    public void setIdleSpritesManager(SpritesManager idleSpritesManager) {
        this.idleSpritesManager = idleSpritesManager;
    }

    public void setAttackSpritesManager(SpritesManager attackSpritesManager) {
        this.attackSpritesManager = attackSpritesManager;
    }

    @Override
    public void setKeyboardController(KeyboardController keyboardController) {
        this.keyboardController = keyboardController;
    }

    public T getWeapon() {
        return weapon;
    }

    public void setWeapon(T weapon) {
        this.weapon = weapon;
    }

    public double getExp() {
        return exp;
    }

    public void setExp(double exp) {
        this.exp = exp;
    }

    public void takeExp(double exp) {
        this.exp += exp;
        if(this.exp >= expToLevelUp) {
            levelUp();
        }
    }


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void incrementLevel() {
        this.level++;
    }

    public double getExpToLevelUp() {
        return expToLevelUp;
    }

    public void setExpToLevelUp(double expToLevelUp) {
        this.expToLevelUp = expToLevelUp;
    }

    public int getPoints() {
        return points;
    }

    public void addPoints(int points) {
        this.points += points;
    }
}