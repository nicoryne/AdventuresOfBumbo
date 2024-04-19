package game.entities;

import game.util.Directions;

import java.awt.*;

public interface CharacterBuilder {

    void reset();

    void setEntityType(EntityType type);

    void setScreenPositionX(double screenPositionX);

    void setScreenPositionY(double screenPositionY);

    void setAlive();

    void setHitbox(Rectangle rectangle);

    void setEntitySpeed(int entitySpeed);

    void setDirection(Directions direction);

    void setHitPoints(int hitPoints);


    void setSpeed(int speed);
}
