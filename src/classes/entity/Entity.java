package classes.entity;

import java.util.UUID;

public abstract class Entity {
    private int positionX, positionY;
    private final UUID UNIQUE_ENTITY_ID = UUID.randomUUID();

    public void setPositionX(int xCoordinate) {
        this.positionX = xCoordinate;
    }

    public int getPositionX() {
        return this.positionX;
    }

    public void setPositionY(int yCoordinate) {
        this.positionY = yCoordinate;
    }

    public int getPositionY() {
        return this.positionY;
    }

    public UUID getUniqueEntityId() {
        return this.UNIQUE_ENTITY_ID;
    }

}
