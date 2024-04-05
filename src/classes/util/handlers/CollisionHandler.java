package classes.util.handlers;

import classes.Game;
import classes.entities.EntityObject;
import classes.util.managers.TileManager;

public abstract class CollisionHandler {

    private static final int TILE_SIZE = Integer.parseInt(Game.getInstance().getProperty("TILE_SIZE"));
    private static int firstTile;
    private static int secondTile;
    private static int entityLeftCol;
    private static int entityRightCol;
    private static int entityTopRow;
    private static int entityBottomRow;

    public static void checkTileCollision(EntityObject entity) {
        entityTopRow = getEntityNorthPosition(entity) / TILE_SIZE;
        entityBottomRow = getEntitySouthPosition(entity) / TILE_SIZE;
        entityLeftCol = getEntityWestPosition(entity) / TILE_SIZE;
        entityRightCol = getEntityEastPosition(entity) / TILE_SIZE;
        int speed = entity.getMovementComponent().getSpeed();

        switch (entity.getMovementComponent().getDirection()) {
            case "NORTH":
                handleNorthDirection(entity, speed);
                break;
            case "SOUTH":
                handleSouthDirection(entity, speed);
                break;
            case "WEST":
                handleWestDirection(entity, speed);
                break;
            case "EAST":
                handleEastDirection(entity, speed);
                break;
            default:
                return;
        }

        if (isCollidable(firstTile) || isCollidable(secondTile)) {
            entity.getMovementComponent().setColliding(true);
        }
    }
    
    private static void handleNorthDirection(EntityObject entity, int speed) {
        entityTopRow = (getEntityNorthPosition(entity) - speed) / TILE_SIZE;
        firstTile = getTileNumber(entityLeftCol, entityTopRow);
        secondTile = getTileNumber(entityRightCol, entityTopRow);
    }
    
    private static void handleSouthDirection(EntityObject entity, int speed) {
        entityBottomRow = (getEntitySouthPosition(entity) - speed) / TILE_SIZE;
        firstTile = getTileNumber(entityLeftCol, entityBottomRow);
        secondTile = getTileNumber(entityRightCol, entityBottomRow);
    }

    private static void handleWestDirection(EntityObject entity, int speed) {
        entityLeftCol = (getEntityWestPosition(entity) - speed) / TILE_SIZE;
        firstTile = getTileNumber(entityLeftCol, entityTopRow);
        secondTile = getTileNumber(entityLeftCol, entityBottomRow);
    }

    private static void handleEastDirection(EntityObject entity, int speed) {
        entityLeftCol = (getEntityEastPosition(entity) - speed) / TILE_SIZE;
        firstTile = getTileNumber(entityRightCol, entityTopRow);
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

        return worldPositionX + widthHitbox;
    }

    private static int getEntityNorthPosition(EntityObject entity) {
        int worldPositionY = entity.getPositionComponent().getWorldPositionY().intValue();
        int yHitbox = (int) entity.getRenderComponent().getHitbox().getY();

        return worldPositionY + yHitbox;
    }

    private static int getEntitySouthPosition(EntityObject entity) {
        int worldPositionY = entity.getPositionComponent().getWorldPositionY().intValue();
        int heightHitbox = (int) entity.getRenderComponent().getHitbox().getHeight();

        return worldPositionY + heightHitbox;
    }
}
