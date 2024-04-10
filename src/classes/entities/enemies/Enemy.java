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

    private static final int MOVE_DELAY = 120;

    private int moveDelayCounter = 0;

    private boolean onPath = true;

    public Enemy() {
        this.setEntityType(EntityType.ENEMY);
    }

    @Override
    public void update() {
        super.update();
        incrementMoveDelayCounter();

        if (moveDelayCounter == MOVE_DELAY) {
            switchDirection();
            resetMoveDelayCounter();
        }

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
        if(onPath) {
            int tileSize = Integer.parseInt(Game.getInstance().getProperty("TILE_SIZE"));
            Player<Weapon> player = Game.getInstance().getPlayer();
            int worldPositionX = player.getPositionComponent().getWorldPositionX().intValue();
            int worldPositionY = player.getPositionComponent().getWorldPositionY().intValue();
            int xHitbox = (int) player.getRenderComponent().getHitbox().getX();
            int yHitbox = (int) player.getRenderComponent().getHitbox().getY();
            int goalCol = (worldPositionX + xHitbox) / tileSize;
            int goalRow = (worldPositionY + yHitbox) / tileSize;

            searchPath(goalCol, goalRow);
        } else {
            Random random = new Random();
            int i = random.nextInt(101);

            if (i <= 25) {
                getMovementComponent().setDirection(Directions.NORTH);
            }

            if (i > 25 && i <= 50) {
                getMovementComponent().setDirection(Directions.SOUTH);
            }

            if (i > 50 && i <= 75) {
                getMovementComponent().setDirection(Directions.WEST);
            }

            if (i > 75) {
                getMovementComponent().setDirection(Directions.EAST);
            }

        }
    }

    private void move() {
        int speed = calculateSpeed();
        getMovementComponent().setColliding(false);
        CollisionHandler.checkTileCollision(this, speed);
        CollisionHandler.checkPlayerCollision(this);

        if(!getMovementComponent().isColliding()) {
            int worldPositionY = getPositionComponent().getWorldPositionY().intValue();
            int worldPositionX = getPositionComponent().getWorldPositionX().intValue();

            switch (getMovementComponent().getDirection()) {
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
        int widthHitbox = (int) getRenderComponent().getHitbox().getWidth();
        int heightHitbox = (int) getRenderComponent().getHitbox().getHeight();

        int startCol = (worldPositionX + xHitbox) / tileSize;
        int startRow = (worldPositionY + yHitbox) / tileSize;

        pathFinder.setNodes(startCol, startRow, goalCol, goalRow);

        if (pathFinder.search()) {
            int nextX = pathFinder.getPathNodes().get(0).getCol() * tileSize;
            int nextY = pathFinder.getPathNodes().get(0).getRow() * tileSize;

            int entityLeftX = worldPositionX + xHitbox;
            int entityRightX = worldPositionX + xHitbox + widthHitbox;
            int entityTopY = worldPositionY + yHitbox;
            int entityBottomY = worldPositionY + yHitbox + heightHitbox;

            if(entityTopY > nextY && entityLeftX >= nextX && entityRightX < nextX + tileSize) {
                getMovementComponent().setDirection(Directions.NORTH);
            } else if(entityTopY < nextY && entityLeftX >= nextX && entityRightX < nextX + tileSize) {
                getMovementComponent().setDirection(Directions.SOUTH);
            } else if(entityTopY >= nextY && entityBottomY < nextY + tileSize) {
                if(entityLeftX > nextX) {
                    getMovementComponent().setDirection(Directions.WEST);
                } else if (entityLeftX < nextX) {
                    getMovementComponent().setDirection(Directions.EAST);
                }
            } else if (entityTopY > nextY && entityLeftX > nextX) {
                getMovementComponent().setDirection(Directions.NORTH);
                CollisionHandler.checkTileCollision(this, calculateSpeed());
                if(getMovementComponent().isColliding()) {
                    getMovementComponent().setDirection(Directions.WEST);
                }
            } else if(entityTopY > nextY && entityLeftX < nextX) {
                getMovementComponent().setDirection(Directions.NORTH);
                CollisionHandler.checkTileCollision(this, calculateSpeed());
                if(getMovementComponent().isColliding()) {
                    getMovementComponent().setDirection(Directions.EAST);
                }
            } else if(entityTopY < nextY && entityLeftX > nextX) {
                getMovementComponent().setDirection(Directions.SOUTH);
                CollisionHandler.checkTileCollision(this, calculateSpeed());
                if(getMovementComponent().isColliding()) {
                    getMovementComponent().setDirection(Directions.WEST);
                }
            } else if(entityTopY < nextY && entityLeftX < nextX) {
                getMovementComponent().setDirection(Directions.SOUTH);
                CollisionHandler.checkTileCollision(this, calculateSpeed());
                if(getMovementComponent().isColliding()) {
                    getMovementComponent().setDirection(Directions.EAST);
                }
            }

//            int nextCol = pathFinder.getPathNodes().get(0).getCol();
//            int nextRow = pathFinder.getPathNodes().get(0).getRow();
//            if(nextCol == goalCol && nextRow == goalRow) {
//                onPath = false;
//                System.out.println("PATH STOP!");
//            }
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

    public void incrementMoveDelayCounter() {
        this.moveDelayCounter++;
    }

    public void resetMoveDelayCounter() {
        this.moveDelayCounter = 0;
    }

    public int getMoveDelayCounter() {
        return moveDelayCounter;
    }
}
