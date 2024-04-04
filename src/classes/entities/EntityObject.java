package classes.entities;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.UUID;

public abstract class EntityObject implements RenderableEntity {

    private final UUID UNIQUE_ENTITY_ID = UUID.randomUUID();
    private BufferedImage entityImage;
    private double worldPositionX;
    private double worldPositionY;
    private double screenPositionX;
    private double screenPositionY;
    private Rectangle solidArea;
    private boolean isCollisionOn;


    public void setWorldPositionX(double xCoordinate) {
        this.worldPositionX = xCoordinate;
    }

    public double getWorldPositionX() {
        return this.worldPositionX;
    }

    public void setWorldPositionY(double yCoordinate) {
        this.worldPositionY = yCoordinate;
    }

    public double getWorldPositionY() {
        return this.worldPositionY;
    }

    public double getScreenPositionX() {
        return screenPositionX;
    }

    public void setScreenPositionX(double screenPositionX) {
        this.screenPositionX = screenPositionX;
    }

    public double getScreenPositionY() {
        return screenPositionY;
    }

    public void setScreenPositionY(double screenPositionY) {
        this.screenPositionY = screenPositionY;
    }

    public UUID getUniqueEntityId() {
        return this.UNIQUE_ENTITY_ID;
    }

    public Rectangle getSolidArea() {
        return this.solidArea;
    }

    public void setSolidArea(Rectangle solidArea) {
        this.solidArea = solidArea;
    }

    public boolean isCollisionOn() {
        return isCollisionOn;
    }

    public void setCollision(boolean collisionOn) {
        isCollisionOn = collisionOn;
    }

    public BufferedImage getEntityImage() {
        return entityImage;
    }

    public void setEntityImage(BufferedImage entityImage) {
        this.entityImage = entityImage;
    }
}
