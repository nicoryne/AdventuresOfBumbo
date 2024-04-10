package game.equips.weapons.util;

import game.util.Directions;

public interface AttackingWeapon {

    void attack(double angle, double screenPositionX, double screenPositionY, double worldPositionX, double worldPositionY, Directions currentDirection);

}
