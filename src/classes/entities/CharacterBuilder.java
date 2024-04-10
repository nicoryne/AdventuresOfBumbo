package classes.entities;

import classes.util.Directions;

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

    void setHitPoints(double hitPoints);

    void setMana(double mana);

    void setStrength(double strength);

    void setAgility(double agility);

    void setIntelligence(double intelligence);

    void setSpeed(int speed);
}
