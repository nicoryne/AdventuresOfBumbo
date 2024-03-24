package classes.entity;

public abstract class MovingEntity extends Entity {
    private int entitySpeed;
    private String currentDirection;

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

}
