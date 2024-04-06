package classes.equips.weapons;

import classes.Game;
import classes.entities.projectile.ArrowProjectile;
import classes.entities.util.CloneableEntity;
import classes.equips.weapons.util.RangedWeapon;
import classes.util.handlers.SoundHandler;

import java.util.ArrayList;

public class Bow extends RangedWeapon<ArrowProjectile> {

    public Bow() {
        this.setProjectile(new ArrowProjectile());
        this.setWeaponName("Bow");
        this.setWeaponType(WeaponType.RANGED);
        this.setDamage(30);
        this.setRange(20);
        this.setFireRate(13); // lowest should be 13 so that audio can catch up
        this.setReloadCooldown(0);

        this.setProjectileSize(5);
        this.setProjectiles(new ArrayList<>());
    }

    @Override
    public void attack(double angle, double screenPositionX, double screenPositionY, double worldPositionX, double worldPositionY, String currentDirection) {
        if(canAttack()) {

            ArrowProjectile arrowProjectile  = new ArrowProjectile();
            arrowProjectile.setClone(angle, screenPositionX, screenPositionY, worldPositionX, worldPositionY, currentDirection);
            getProjectiles().add(arrowProjectile);
            Game.getInstance().addEntity(arrowProjectile);

            SoundHandler.playAudio("shoot-1", 0, 1.0f);
            setReloadCooldown(0);
        }
    }
}
