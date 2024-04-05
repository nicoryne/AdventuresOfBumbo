package classes.util;

import classes.entities.MovingEntityObject;
import classes.ui.components.GamePanel;

public class CollisionChecker {

    private final GamePanel gamePanel;
    private final int tileSize;
    private int firstTile;
    private int secondTitle;
    private int entityLeftCol;
    private int entityRightCol;
    private int entityTopRow;
    private int entityBottomRow;

    public CollisionChecker(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        this.tileSize = gamePanel.getTileSize();
    }

    public void checkTileCollision(MovingEntityObject entity) {
        setEntityTopRow(getEntityNorthPosition(entity) / tileSize);
        setEntityBottomRow(getEntitySouthPosition(entity) / tileSize);
        setEntityLeftCol(getEntityWestPosition(entity) / tileSize);
        setEntityRightCol(getEntityEastPosition(entity) / tileSize);

        switch (entity.getCurrentDirection()) {
            case "NORTH":
                setEntityTopRow((getEntityNorthPosition(entity) - entity.getEntitySpeed()) / tileSize);
                setFirstTile(getTileNumber(entityLeftCol, entityTopRow));
                setSecondTitle(getTileNumber(entityRightCol, entityTopRow));
                break;
            case "SOUTH":
                setEntityBottomRow((getEntitySouthPosition(entity) + entity.getEntitySpeed()) / tileSize);
                setFirstTile(getTileNumber(entityLeftCol, entityBottomRow));
                setSecondTitle(getTileNumber(entityRightCol, entityBottomRow));
                break;
            case "WEST":
                setEntityLeftCol((getEntityWestPosition(entity) - entity.getEntitySpeed()) / tileSize);
                setFirstTile(getTileNumber(entityLeftCol, entityTopRow));
                setSecondTitle(getTileNumber(entityLeftCol, entityBottomRow));
                break;
            case "EAST":
                setEntityRightCol((getEntityEastPosition(entity) + entity.getEntitySpeed()) / tileSize);
                setFirstTile(getTileNumber(entityRightCol, entityTopRow));
                setSecondTitle(getTileNumber(entityRightCol, entityTopRow));
                break;
            default:
                return;
        }

        if (isCollidable(firstTile) || isCollidable(secondTitle)) {
            entity.setCollision(true);
        }
    }
    private int getTileNumber(int col, int row) {
        return gamePanel.getTileManager().getMapTile2DArray()[col][row];
    }

    private boolean isCollidable(int tileNum) {
        return gamePanel.getTileManager().getTileArrayList().get(tileNum).isCollidable();
    }

    private int getEntityWestPosition(MovingEntityObject entity) {
        return (int) (entity.getWorldPositionX() + entity.getSolidArea().getX());
    }

    private int getEntityEastPosition(MovingEntityObject entity) {
        return (int) (getEntityWestPosition(entity) + entity.getSolidArea().getWidth());
    }

    private int getEntityNorthPosition(MovingEntityObject entity) {
        return (int) (entity.getWorldPositionY() + entity.getSolidArea().getY());
    }

    private int getEntitySouthPosition(MovingEntityObject entity) {
        return (int) (getEntityNorthPosition(entity) + entity.getSolidArea().getHeight());
    }

    private void setEntityLeftCol(int entityLeftCol) {
        this.entityLeftCol = entityLeftCol;
    }

    private void setEntityRightCol(int entityRightCol) {
        this.entityRightCol = entityRightCol;
    }

    private void setEntityTopRow(int entityTopRow) {
        this.entityTopRow = entityTopRow;
    }

    private void setEntityBottomRow(int entityBottomRow) {
        this.entityBottomRow = entityBottomRow;
    }

    private void setFirstTile(int firstTile) {
        this.firstTile = firstTile;
    }

    private void setSecondTitle(int secondTitle) {
        this.secondTitle = secondTitle;
    }
}
