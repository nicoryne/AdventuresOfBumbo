package classes.entities.components;

import classes.util.Directions;

public class MovementComponent {

    private int entitySpeed;

    private double angle;

    private Directions direction;

    private boolean isColliding;

    public int getEntitySpeed() {
        return entitySpeed;
    }

    public void setEntitySpeed(int entitySpeed) {
        this.entitySpeed = entitySpeed;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public Directions getDirection() {
        return direction;
    }

    public void setDirection(Directions direction) {
        this.direction = direction;
    }

    public boolean isColliding() {
        return isColliding;
    }

    public void setColliding(boolean colliding) {
        isColliding = colliding;
    }
}
