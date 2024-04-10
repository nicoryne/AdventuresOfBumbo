package classes.entities.enemies;

import classes.Game;
import classes.entities.CharacterEntity;
import classes.entities.EntityType;
import classes.entities.player.Player;
import classes.equips.weapons.Weapon;
import classes.util.Directions;
import classes.util.handlers.CollisionHandler;
import classes.util.handlers.RenderHandler;
import classes.util.managers.SpritesManager;
import classes.util.pathfinding.PathFinder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Enemy extends CharacterEntity {

    private EnemyType enemyType;

    private SpritesManager spritesManager;


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
        int tileSize = Integer.parseInt(Game.getInstance().getProperty("TILE_SIZE"));
        Player<Weapon> player = Game.getInstance().getPlayer();
        int worldPositionX = player.getPositionComponent().getWorldPositionX().intValue();
        int worldPositionY = player.getPositionComponent().getWorldPositionY().intValue();
        int xHitbox = (int) player.getRenderComponent().getHitbox().getX();
        int yHitbox = (int) player.getRenderComponent().getHitbox().getY();
        int widthHitbox = (int) getRenderComponent().getHitbox().getWidth();
        int heightHitbox = (int) getRenderComponent().getHitbox().getHeight();
        int playerCenterX = worldPositionX + xHitbox + widthHitbox / 2;
        int playerCenterY = worldPositionY + yHitbox + heightHitbox / 2;
        int playerTileCol = playerCenterX / tileSize;
        int playerTileRow = playerCenterY / tileSize;

        searchPath(playerTileCol, playerTileRow);
    }

    private void move() {
        int speed = calculateSpeed();
        int diagonalSpeed = (int) (speed / Math.sqrt(2));
        getMovementComponent().setColliding(false);
        CollisionHandler.checkTileCollision(this, speed);
        CollisionHandler.checkPlayerCollision(this);

        if(!getMovementComponent().isColliding()) {
            int worldPositionY = getPositionComponent().getWorldPositionY().intValue();
            int worldPositionX = getPositionComponent().getWorldPositionX().intValue();

            handleMovement(worldPositionX, worldPositionY, speed, diagonalSpeed);

            spritesManager.updateSprite();
        }
    }

    private int calculateSpeed() {
        return (int) (getMovementComponent().getEntitySpeed() + ((getStatComponent().getSpeed() / 100) + getAttributeComponents().getAgility() / 10));
    }

    private void searchPath(int goalCol, int goalRow) {
        PathFinder pathFinder = Game.getInstance().getPathFinder();
        int tileSize = Integer.parseInt(Game.getInstance().getProperty("TILE_SIZE"));
        int worldPositionX = getPositionComponent().getWorldPositionX().intValue();
        int worldPositionY = getPositionComponent().getWorldPositionY().intValue();
        int xHitbox = (int) getRenderComponent().getHitbox().getX();
        int yHitbox = (int) getRenderComponent().getHitbox().getY();

        int startCol = (worldPositionX + xHitbox) / tileSize;
        int startRow = (worldPositionY + yHitbox) / tileSize;

        pathFinder.setNodes(startCol, startRow, goalCol, goalRow);

        if (pathFinder.search()) {
            int nextX = pathFinder.getPathNodes().get(0).getCol() * tileSize;
            int nextY = pathFinder.getPathNodes().get(0).getRow() * tileSize;

            int entityLeftX = worldPositionX + xHitbox;
            int entityTopY = worldPositionY + yHitbox;


            handleDirections(nextX, nextY, entityLeftX, entityTopY);
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

    private void reverseDirection() {
        switch (getMovementComponent().getDirection()) {
            case Directions.NORTH_EAST:
                getMovementComponent().setDirection(Directions.SOUTH_WEST);
                break;
            case Directions.NORTH_WEST:
                getMovementComponent().setDirection(Directions.SOUTH_EAST);
                break;
            case Directions.SOUTH_EAST:
                getMovementComponent().setDirection(Directions.NORTH_WEST);
                break;
            case Directions.SOUTH_WEST:
                getMovementComponent().setDirection(Directions.NORTH_EAST);
                break;
            case Directions.NORTH:
                getMovementComponent().setDirection(Directions.SOUTH);
                break;
            case Directions.SOUTH:
                getMovementComponent().setDirection(Directions.NORTH);
                break;
            case Directions.WEST:
                getMovementComponent().setDirection(Directions.EAST);
                break;
            case Directions.EAST:
                getMovementComponent().setDirection(Directions.WEST);
                break;
        }
    }


    @Override
    public void kill() {
        super.kill();
    }

    @Override
    public void spawn(double x, double y) {
        super.spawn(x, y);
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
}
