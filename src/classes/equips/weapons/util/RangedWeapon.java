package classes.equips.weapons.util;

import classes.entities.projectile.Projectile;
import classes.equips.weapons.Weapon;

import java.util.ArrayList;

public class RangedWeapon<P extends Projectile> extends Weapon{

    private ArrayList<P> projectiles;

    private Projectile projectile;

    private double projectileSize;


    public ArrayList<P> getProjectiles() {
        return projectiles;
    }

    public void setProjectiles(ArrayList<P> projectiles) {
        this.projectiles = projectiles;
    }

    public double getProjectileSize() {return projectileSize;}

    public void setProjectileSize(double projectileSize) {
        this.projectileSize = projectileSize;
    }

    public Projectile getProjectile() {return projectile;}

    public void setProjectile(Projectile projectile) {this.projectile = projectile;}
}
