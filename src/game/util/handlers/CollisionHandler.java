package game.util.handlers;

import game.Game;
import game.entities.CharacterEntity;
import game.entities.EntityObject;
import game.entities.MovingEntity;
import game.entities.enemy.Enemy;
import game.entities.player.Player;
import game.entities.projectile.Projectile;
import game.equips.weapons.Weapon;
import game.util.managers.TileManager;
import services.LoggerHelper;

import java.awt.*;
import java.util.ArrayList;

public abstract class CollisionHandler {

    private static final int TILE_SIZE = Integer.parseInt(Game.getInstance().getProperty("TILE_SIZE"));
    private static int firstTile;
    private static int secondTile;
    private static int entityLeftCol;
    private static int entityRightCol;
    private static int entityTopRow;
    private static int entityBottomRow;

    public static void checkProjectileCollision(Projectile projectile) {
        ArrayList<MovingEntity> entities = Game.getInstance().getEntities();

       for(MovingEntity targetEntity : entities) {
            Rectangle entityHitbox = handleSolidArea(projectile);
            Rectangle targetHitbox = handleSolidArea(targetEntity);

            boolean valid = projectile.getEntityType() != targetEntity.getEntityType();

           if(entityHitbox.intersects(targetHitbox) && valid) {
               projectile.kill();

              if(targetEntity instanceof Enemy enemy) {
                  enemy.takeDamage(20);

                  if(enemy.getStatComponent().getCurrentHitPoints() <= 0.0) {
                      enemy.kill();
                  }
              }
           }
       }
    }

    public static void checkEnemyCollision(CharacterEntity player) {
        ArrayList<MovingEntity> entities = Game.getInstance().getEntities();

        for(MovingEntity entity : entities) {
            if(entity instanceof Enemy enemy) {

                if (isFar(enemy, player)) {
                    return;
                } else {
                    LoggerHelper.logInfo("Enemy nearby!");
                }

                Rectangle playerHitbox = handleSolidArea(player);
                Rectangle enemyHitbox = handleSolidArea(enemy);

                if(playerHitbox.intersects(enemyHitbox)) {
                    player.takeDamage(enemy.dealDamage());
                }
            }
        }
    }

    public static void checkOtherEnemyCollision(Enemy enemy) {
        ArrayList<MovingEntity> entities = Game.getInstance().getEntities();

        for (MovingEntity entity : entities) {
            if (entity instanceof Enemy otherEnemy && entity != enemy) {
                Rectangle enemyHitbox = handleSolidArea(enemy);
                Rectangle otherEnemyHitbox = handleSolidArea(otherEnemy);

                if (enemyHitbox.intersects(otherEnemyHitbox)) {
                    enemy.getMovementComponent().setColliding(true);
                    otherEnemy.getMovementComponent().setColliding(true);
                }
            }
        }
    }

    public static void checkPlayerCollision(Enemy enemy) {
        Player<Weapon> player = Game.getInstance().getPlayer();

        if (isFar(enemy, player)) {
            return;
        }

        Rectangle playerHitbox = handleSolidArea(player);
        Rectangle enemyHitbox = handleSolidArea(enemy);

        if (playerHitbox.intersects(enemyHitbox)) {
            enemy.getMovementComponent().setColliding(true);
            player.takeDamage(enemy.dealDamage());
        }
    }

    public static void checkTileCollision(MovingEntity entity, int speed) {
        entityTopRow = getEntityNorthPosition(entity) / TILE_SIZE;
        entityBottomRow = getEntitySouthPosition(entity) / TILE_SIZE;
        entityLeftCol = getEntityWestPosition(entity) / TILE_SIZE;
        entityRightCol = getEntityEastPosition(entity) / TILE_SIZE;

        switch (entity.getMovementComponent().getDirection()) {
            case NORTH:
                handleNorthDirection(entity, speed);
                break;
            case SOUTH:
                handleSouthDirection(entity, speed);
                break;
            case WEST:
                handleWestDirection(entity, speed);
                break;
            case EAST:
                handleEastDirection(entity, speed);
                break;
            case NORTH_EAST:
                handleNorthEastDirection(entity, speed);
                break;
            case NORTH_WEST:
                handleNorthWestDirection(entity, speed);
                break;
            case SOUTH_EAST:
                handleSouthEastDirection(entity, speed);
                break;
            case SOUTH_WEST:
                handleSouthWestDirection(entity, speed);
                break;
            default:
                return;
        }

        if (isCollidable(firstTile) || isCollidable(secondTile)) {
            entity.getMovementComponent().setColliding(true);
        }
    }

    private static boolean isFar(MovingEntity currentEntity, MovingEntity otherEntity) {
        double distanceSquared = calculateDistanceSquared(currentEntity, otherEntity);
        double thresholdDistanceSquared = 64 * 64;

        return distanceSquared >= thresholdDistanceSquared;
    }

    private static double calculateDistanceSquared(MovingEntity entity1, MovingEntity entity2) {
        double dx = entity1.getPositionComponent().getWorldPositionX().doubleValue() - entity2.getPositionComponent().getWorldPositionX().doubleValue();
        double dy = entity1.getPositionComponent().getWorldPositionY().doubleValue() - entity2.getPositionComponent().getWorldPositionY().doubleValue();
        return dx * dx + dy * dy;
    }
    
    private static void handleNorthDirection(EntityObject entity, int speed) {
        entityTopRow = (getEntityNorthPosition(entity) - speed) / TILE_SIZE;
        firstTile = getTileNumber(entityLeftCol, entityTopRow);
        secondTile = getTileNumber(entityRightCol, entityTopRow);
    }
    
    private static void handleSouthDirection(EntityObject entity, int speed) {
        entityBottomRow = (getEntitySouthPosition(entity) + speed) / TILE_SIZE;
        firstTile = getTileNumber(entityLeftCol, entityBottomRow);
        secondTile = getTileNumber(entityRightCol, entityBottomRow);
    }

    private static void handleWestDirection(EntityObject entity, int speed) {
        entityLeftCol = (getEntityWestPosition(entity) - speed) / TILE_SIZE;
        firstTile = getTileNumber(entityLeftCol, entityTopRow);
        secondTile = getTileNumber(entityLeftCol, entityBottomRow);
    }

    private static void handleEastDirection(EntityObject entity, int speed) {
        entityRightCol = (getEntityEastPosition(entity) + speed) / TILE_SIZE;
        firstTile = getTileNumber(entityRightCol, entityTopRow);
        secondTile = getTileNumber(entityRightCol, entityBottomRow);
    }

    private static void handleNorthEastDirection(EntityObject entity, int speed) {
        entityTopRow = (getEntityNorthPosition(entity) - speed) / TILE_SIZE;
        entityRightCol = (getEntityEastPosition(entity) + speed) / TILE_SIZE;
        firstTile = getTileNumber(entityLeftCol, entityTopRow);
        secondTile = getTileNumber(entityRightCol, entityTopRow);
    }

    private static void handleNorthWestDirection(EntityObject entity, int speed) {
        entityTopRow = (getEntityNorthPosition(entity) - speed) / TILE_SIZE;
        entityLeftCol = (getEntityWestPosition(entity) - speed) / TILE_SIZE;
        firstTile = getTileNumber(entityLeftCol, entityTopRow);
        secondTile = getTileNumber(entityRightCol, entityTopRow);
    }

    private static void handleSouthEastDirection(EntityObject entity, int speed) {
        entityBottomRow = (getEntitySouthPosition(entity) + speed) / TILE_SIZE;
        entityRightCol = (getEntityEastPosition(entity) + speed) / TILE_SIZE;
        firstTile = getTileNumber(entityLeftCol, entityBottomRow);
        secondTile = getTileNumber(entityRightCol, entityBottomRow);
    }

    private static void handleSouthWestDirection(EntityObject entity, int speed) {
        entityBottomRow = (getEntitySouthPosition(entity) + speed) / TILE_SIZE;
        entityLeftCol = (getEntityWestPosition(entity) - speed) / TILE_SIZE;
        firstTile = getTileNumber(entityLeftCol, entityBottomRow);
        secondTile = getTileNumber(entityRightCol, entityBottomRow);
    }


    private static int getTileNumber(int col, int row) {
        TileManager tileManager = Game.getInstance().getGameManagerComponents().getTileManager();
        return tileManager.getMapTile2DArray()[col][row];
    }

    private static boolean isCollidable(int tileNum) {
        TileManager tileManager = Game.getInstance().getGameManagerComponents().getTileManager();
        return tileManager.getTileArrayList().get(tileNum).isCollidable();
    }

    private static int getEntityWestPosition(EntityObject entity) {
        int worldPositionX = entity.getPositionComponent().getWorldPositionX().intValue();
        int xHitbox = (int) entity.getRenderComponent().getHitbox().getX();

        return worldPositionX + xHitbox;
    }

    private static int getEntityEastPosition(EntityObject entity) {
        int worldPositionX = entity.getPositionComponent().getWorldPositionX().intValue();
        int widthHitbox = (int) entity.getRenderComponent().getHitbox().getWidth();
        int xHitbox = (int) entity.getRenderComponent().getHitbox().getX();

        return worldPositionX + xHitbox + widthHitbox;
    }

    private static int getEntityNorthPosition(EntityObject entity) {
        int worldPositionY = entity.getPositionComponent().getWorldPositionY().intValue();
        int yHitbox = (int) entity.getRenderComponent().getHitbox().getY();

        return worldPositionY + yHitbox;
    }

    private static int getEntitySouthPosition(EntityObject entity) {
        int worldPositionY = entity.getPositionComponent().getWorldPositionY().intValue();
        int heightHitbox = (int) entity.getRenderComponent().getHitbox().getHeight();
        int yHitbox = (int) entity.getRenderComponent().getHitbox().getY();

        return worldPositionY + yHitbox + heightHitbox;
    }

    private static Rectangle handleSolidArea(EntityObject entity) {
        return new Rectangle(getEntityWestPosition(entity),
                getEntityNorthPosition(entity),
                (int) entity.getRenderComponent().getHitbox().getWidth(),
                (int) entity.getRenderComponent().getHitbox().getHeight());
    }
}
