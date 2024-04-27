package game.entities.enemy;

import game.Game;
import game.entities.CharacterEntity;
import game.entities.EntityType;
import game.entities.drops.DropStandardExp;
import game.entities.player.Player;
import game.equips.weapons.Weapon;
import game.util.Directions;
import game.util.handlers.CollisionHandler;
import game.util.handlers.RenderHandler;
import game.util.managers.SpritesManager;
import services.LoggerHelper;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Enemy extends CharacterEntity {

    private EnemyType enemyType;

    private SpritesManager spritesManager;

    private double expDropped;

    private static final int SPRITE_MOVE_DELAY_MS = 2;

    private int spriteMoveDelayCounter = 0;

    private int pointsGiven;

    public Enemy() {
        this.setEntityType(EntityType.ENEMY);
    }

    @Override
    public void update() {
        super.update();
        switchDirection();
        move();
    }

    @Override
    public void render(Graphics2D g2) {
        double worldX = getPositionComponent().getWorldPositionX().doubleValue();
        double worldY = getPositionComponent().getWorldPositionY().doubleValue();
        Directions direction = getMovementComponent().getDirection();

        if(RenderHandler.isViewableOnScreen(worldX, worldY)) {
            BufferedImage sprite = spritesManager.getCurrentSprite(direction);
            RenderHandler.renderOnScreen(worldX, worldY, sprite, g2);
        }
    }

    private void switchDirection() {
        Player<Weapon> player = Game.getInstance().getPlayer();
        handleDirections(getPlayerCenterX(player), getPlayerCenterY(player), getEntityLeftX(), getEntityTopY());
    }

    private void move() {
        int speed = calculateSpeed();
        int diagonalSpeed = (int) (speed / Math.sqrt(2));
        getMovementComponent().setColliding(false);
        CollisionHandler.checkTileCollision(this, speed);
        CollisionHandler.checkPlayerCollision(this);
        CollisionHandler.checkOtherEnemyCollision(this);

        if(!getMovementComponent().isColliding()) {
            int worldPositionY = getPositionComponent().getWorldPositionY().intValue();
            int worldPositionX = getPositionComponent().getWorldPositionX().intValue();

            handleMovement(worldPositionX, worldPositionY, speed, diagonalSpeed);
        }

        if(spriteMoveDelayCounter >= SPRITE_MOVE_DELAY_MS) {
            spritesManager.updateSprite();
            spriteMoveDelayCounter = 0;
        } else {
            spriteMoveDelayCounter++;
        }
    }
    private void handleDirections(int nextX, int nextY, int entityLeftX, int entityTopY) {
        Directions direction;

        if (entityLeftX < nextX) {
            if (entityTopY < nextY) {
                direction = Directions.SOUTH_EAST;
            } else if (entityTopY > nextY) {
                direction = Directions.NORTH_EAST;
            } else {
                direction = Directions.EAST;
            }
        } else if (entityLeftX > nextX) {
            if (entityTopY < nextY) {
                direction = Directions.SOUTH_WEST;
            } else if (entityTopY > nextY) {
                direction = Directions.NORTH_WEST;
            } else {
                direction = Directions.WEST;
            }
        } else {
            if (entityTopY < nextY) {
                direction = Directions.SOUTH;
            } else if (entityTopY > nextY) {
                direction = Directions.NORTH;
            } else {
                direction = getMovementComponent().getDirection();
            }
        }

        getMovementComponent().setDirection(direction);
        CollisionHandler.checkTileCollision(this, calculateSpeed());
        if(getMovementComponent().isColliding()) {
            reverseDirection();
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

    private void reverseDirection() {
        switch (getMovementComponent().getDirection()) {
            case NORTH_EAST:
                getMovementComponent().setDirection(Directions.SOUTH_WEST);
                break;
            case NORTH_WEST:
                getMovementComponent().setDirection(Directions.SOUTH_EAST);
                break;
            case SOUTH_EAST:
                getMovementComponent().setDirection(Directions.NORTH_WEST);
                break;
            case SOUTH_WEST:
                getMovementComponent().setDirection(Directions.NORTH_EAST);
                break;
            case NORTH:
                getMovementComponent().setDirection(Directions.SOUTH);
                break;
            case SOUTH:
                getMovementComponent().setDirection(Directions.NORTH);
                break;
            case WEST:
                getMovementComponent().setDirection(Directions.EAST);
                break;
            case EAST:
                getMovementComponent().setDirection(Directions.WEST);
                break;
        }
    }

    @Override
    public void kill() {
        super.kill();
        spawnExp();
        Game.getInstance().getPlayer().addPoints(pointsGiven);
    }

    public void spawnExp() {
        DropStandardExp dropStandardExp = new DropStandardExp();
        dropStandardExp.spawn(this.getPositionComponent().getWorldPositionX().doubleValue(), this.getPositionComponent().getWorldPositionY().doubleValue(), expDropped);
        Game.getInstance().addObject(dropStandardExp);
    }

    private int getEntityLeftX() {
        int worldPositionX = this.getPositionComponent().getWorldPositionX().intValue();
        int xHitbox = (int) this.getRenderComponent().getHitbox().getX();
        return worldPositionX + xHitbox;
    }

    private int getEntityTopY() {
        int worldPositionY = this.getPositionComponent().getWorldPositionY().intValue();
        int yHitbox = (int) this.getRenderComponent().getHitbox().getY();
        return worldPositionY + yHitbox;
    }

    private <T extends Weapon> int getPlayerCenterX(Player<T> player) {
        int playerWorldPositionX = player.getPositionComponent().getWorldPositionX().intValue();
        int playerXHitbox = (int) player.getRenderComponent().getHitbox().getX();
        int widthHitbox = (int) player.getRenderComponent().getHitbox().getWidth();
        return playerWorldPositionX + playerXHitbox + widthHitbox / 2;
    }

    private <T extends Weapon> int getPlayerCenterY(Player<T> player) {
        int playerWorldPositionY = player.getPositionComponent().getWorldPositionY().intValue();
        int playerYHitbox = (int) player.getRenderComponent().getHitbox().getY();
        int heightHitbox = (int) player.getRenderComponent().getHitbox().getHeight();
        return playerWorldPositionY + playerYHitbox + heightHitbox / 2;
    }

    public double getExpDropped() {
        return expDropped;
    }

    public void setExpDropped(double expDropped) {
        this.expDropped = expDropped;
    }

    public EnemyType getEnemyType() {
        return enemyType;
    }

    public void setEnemyType(EnemyType enemyType) {
        this.enemyType = enemyType;
    }

    public SpritesManager getSpritesManager() {
        return spritesManager;
    }

    public void setSpritesManager(SpritesManager spritesManager) {
        this.spritesManager = spritesManager;
    }

    public int getPointsGiven() {
        return pointsGiven;
    }

    public void setPointsGiven(int pointsGiven) {
        this.pointsGiven = pointsGiven;
    }
}
