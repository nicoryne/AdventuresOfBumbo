package game.equips.weapons;

import game.Game;
import game.entities.projectile.ArrowProjectile;
import game.equips.weapons.util.RangedWeapon;
import game.util.Directions;
import game.util.handlers.SoundHandler;
import game.util.managers.SpritesManager;

import java.util.ArrayList;

public class Bow extends RangedWeapon<ArrowProjectile> {

    public Bow() {
        this.setProjectile(new ArrowProjectile());
        this.setWeaponName(WeaponNames.BOW);
        this.setWeaponType(WeaponType.RANGED);
        this.setDamage(30);
        this.setRange(20);
        this.setFireRate(40);
        this.setReloadCooldown(0);
        this.setProjectileSize(0);
        this.setProjectiles(new ArrayList<>());
    }

    @Override
    public void attack(double angle, double screenPositionX, double screenPositionY, double worldPositionX, double worldPositionY, Directions currentDirection) {
        ArrowProjectile projectile  = new ArrowProjectile();
        projectile.clone(angle, screenPositionX, screenPositionY, worldPositionX, worldPositionY, currentDirection);
        projectile.setProjectileSize(getProjectileSize());
        getProjectiles().add(projectile);
        Game.getInstance().addMovingEntity(projectile);

        SoundHandler.playAudio("bow_virtue", 0, 0.8f);
        setReloadCooldown(0);
    }
}
