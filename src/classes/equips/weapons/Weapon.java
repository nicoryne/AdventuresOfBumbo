package classes.equips.weapons;

import classes.equips.weapons.util.AttackingWeapon;
import classes.util.Directions;

public abstract class Weapon implements AttackingWeapon {

    private String weaponName;

    private WeaponType weaponType;

    private double damage;

    private int range;

    private int fireRate;

    private int reloadCooldown;

    public String getWeaponName() {
        return weaponName;
    }

    public void setWeaponName(String weaponName) {
        this.weaponName = weaponName;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public int getFireRate() {
        return fireRate;
    }

    public void setFireRate(int fireRate) {
        this.fireRate = fireRate;
    }

    public int getReloadCooldown() {
        return reloadCooldown;
    }

    public void setReloadCooldown(int reloadCooldown) {this.reloadCooldown = reloadCooldown;}

    public void incrementReloadCooldown() {
        this.reloadCooldown++;
    }

    public boolean canAttack() {
        return reloadCooldown >= fireRate;
    }

    @Override
    public void attack(double angle, double screenPositionX, double screenPositionY, double worldPositionX, double worldPositionY, Directions currentDirection) {}
}
