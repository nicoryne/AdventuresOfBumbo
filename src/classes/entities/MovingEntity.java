package classes.entities;

public abstract class MovingEntity extends Entity {
    private int entitySpeed;
    private String currentDirection;
    private int screenPositionX;
    private int screenPositionY;

    public void setEntitySpeed(int entitySpeed) {
        this.entitySpeed = entitySpeed;
    }
    public int getEntitySpeed() {
        return this.entitySpeed;
    }
    public void setCurrentDirection(String direction) {
        this.currentDirection = direction;
    }
    public String getCurrentDirection() {
        return this.currentDirection;
    }

    public int getScreenPositionX() {
        return screenPositionX;
    }

    public void setScreenPositionX(int screenPositionX) {
        this.screenPositionX = screenPositionX;
    }

    public int getScreenPositionY() {
        return screenPositionY;
    }

    public void setScreenPositionY(int screenPositionY) {
        this.screenPositionY = screenPositionY;
    }
}
