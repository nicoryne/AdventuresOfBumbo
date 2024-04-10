package classes.equips.weapons.util;

import classes.util.Directions;

public interface AttackingWeapon {

    void attack(double angle, double screenPositionX, double screenPositionY, double worldPositionX, double worldPositionY, Directions currentDirection);

}
