package game.equips.weapons.util;

import game.entities.projectile.Projectile;
import game.equips.weapons.Weapon;

import java.util.ArrayList;

public class RangedWeapon<T extends Projectile> extends Weapon{

    private ArrayList<T> projectiles;

    private Projectile projectile;

    private double projectileSize;

    public ArrayList<T> getProjectiles() {
        return projectiles;
    }

    public void setProjectiles(ArrayList<T> projectiles) {
        this.projectiles = projectiles;
    }

    public double getProjectileSize() {return projectileSize;}

    public void setProjectileSize(double projectileSize) {
        this.projectileSize = projectileSize;
    }

    public Projectile getProjectile() {return projectile;}

    public void setProjectile(Projectile projectile) {this.projectile = projectile;}
}
