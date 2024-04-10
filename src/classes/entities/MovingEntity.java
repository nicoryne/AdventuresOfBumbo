package classes.entities;

import classes.entities.components.MovementComponent;

public abstract class MovingEntity extends EntityObject {

    private MovementComponent movementComponent;

    public MovementComponent getMovementComponent() {
        if(movementComponent == null) {
            movementComponent = new MovementComponent();
        }
        return movementComponent;
    }

}
