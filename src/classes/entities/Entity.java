package classes.entities;

import java.awt.*;
import java.util.UUID;

public abstract class Entity {
    private int worldPositionX, worldPositionY;
    private final UUID UNIQUE_ENTITY_ID = UUID.randomUUID();
    private Rectangle solidArea;
    private boolean isCollisionOn;

    public void setWorldPositionX(int xCoordinate) {
        this.worldPositionX = xCoordinate;
    }

    public int getWorldPositionX() {
        return this.worldPositionX;
    }

    public void setWorldPositionY(int yCoordinate) {
        this.worldPositionY = yCoordinate;
    }

    public int getWorldPositionY() {
        return this.worldPositionY;
    }

    public UUID getUniqueEntityId() {
        return this.UNIQUE_ENTITY_ID;
    }

    public Rectangle getSolidArea() {
        return solidArea;
    }

    public void setSolidArea(Rectangle solidArea) {
        this.solidArea = solidArea;
    }

    public boolean isCollisionOn() {
        return isCollisionOn;
    }

    public void setCollisionOn(boolean collisionOn) {
        isCollisionOn = collisionOn;
    }
}
