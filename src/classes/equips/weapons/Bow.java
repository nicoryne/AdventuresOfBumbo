package classes.equips.weapons;

import classes.Game;
import classes.entities.projectile.ArrowProjectile;
import classes.equips.weapons.util.RangedWeapon;
import classes.util.Directions;
import classes.util.handlers.SoundHandler;

import java.util.ArrayList;

public class Bow extends RangedWeapon<ArrowProjectile> {

    public Bow() {
        this.setProjectile(new ArrowProjectile());
        this.setWeaponName("Bow");
        this.setWeaponType(WeaponType.RANGED);
        this.setDamage(30);
        this.setRange(20);
        this.setFireRate(14); // lowest should be 14 so that audio can catch up
        this.setReloadCooldown(0);

        this.setProjectileSize(0);
        this.setProjectiles(new ArrayList<>());
    }

    @Override
    public void attack(double angle, double screenPositionX, double screenPositionY, double worldPositionX, double worldPositionY, Directions currentDirection) {
        if(canAttack()) {
            ArrowProjectile projectile  = new ArrowProjectile();
            projectile.clone(angle, screenPositionX, screenPositionY, worldPositionX, worldPositionY, currentDirection);
            projectile.setProjectileSize(getProjectileSize());
            getProjectiles().add(projectile);
            Game.getInstance().addEntity(projectile);

            SoundHandler.playAudio("shoot-1", 0, 1.0f);
            setReloadCooldown(0);
        }
    }
}
