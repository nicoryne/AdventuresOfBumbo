package classes.entities;

public abstract class MovingEntityObject extends EntityObject {
    private int entitySpeed;
    private double angle;
    private String currentDirection;
    private boolean isAlive;

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

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public boolean isAlive() {
        return this.isAlive;
    }

    public void setAlive(boolean state) {
        this.isAlive = state;
    }

}
