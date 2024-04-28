package game.equips.weapons;

import game.Game;
import game.entities.projectile.OrbProjectile;
import game.equips.weapons.util.RangedWeapon;
import game.util.Directions;
import game.util.handlers.SoundHandler;
import game.util.managers.SpritesManager;

import java.util.ArrayList;

public class Staff extends RangedWeapon<OrbProjectile> {

    public Staff() {
        this.setProjectile(new OrbProjectile());
        this.setWeaponName("Staff");
        this.setWeaponType(WeaponType.RANGED);
        this.setSpritesManager(new SpritesManager(""));
        this.setDamage(30);
        this.setRange(20);
        this.setFireRate(60); // lowest should be 14 so that audio can catch up
        this.setReloadCooldown(0);

        this.setProjectileSize(0);
        this.setProjectiles(new ArrayList<>());
    }

    @Override
    public void attack(double angle, double screenPositionX, double screenPositionY, double worldPositionX, double worldPositionY, Directions currentDirection) {
        if(canAttack()) {
            OrbProjectile projectile  = new OrbProjectile();
            projectile.clone(angle, screenPositionX, screenPositionY, worldPositionX, worldPositionY, currentDirection);
            projectile.setProjectileSize(getProjectileSize());
            getProjectiles().add(projectile);
            Game.getInstance().addMovingEntity(projectile);

            SoundHandler.playAudio("shoot-1", 0, 1.0f);
            setReloadCooldown(0);
        }
    }
}
