package game.equips.weapons;

import game.equips.weapons.util.AttackingWeapon;
import game.util.Directions;
import game.util.managers.SpritesManager;

public abstract class Weapon implements AttackingWeapon {

    private String weaponName;

    private WeaponType weaponType;

    private SpritesManager spritesManager;

    private int damage;

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

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
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

    public SpritesManager getSpritesManager() {
        return spritesManager;
    }

    public void setSpritesManager(SpritesManager spritesManager) {
        this.spritesManager = spritesManager;
    }

    @Override
    public void attack(double angle, double screenPositionX, double screenPositionY, double worldPositionX, double worldPositionY, Directions currentDirection) {}
}
