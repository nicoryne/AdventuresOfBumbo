package classes.entities;

import classes.entities.EntityType;

import java.awt.*;

public interface EntityBuilder {

    void setEntityType(EntityType type);

    void setScreenPositionX(double screenPositionX);

    void setScreenPositionY(double screenPositionY);

    void setAlive();

    void setHitbox(Rectangle rectangle);

    void setSpeed(int speed);

    void setDirection(String direction);
}
