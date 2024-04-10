package classes.entities.projectile;

import classes.util.Directions;

import java.awt.*;

public class OrbProjectile extends Projectile {

    public OrbProjectile() {
        this.setProjectileType(ProjectileType.ORB);
        ProjectileFlyweight projectileFlyweight = ProjectileFlyweightFactory.getFlyweight(getProjectileType());
        this.setSpritesManager(projectileFlyweight.getSpritesManager());

        this.getRenderComponent().setAlive(true);
        this.getMovementComponent().setEntitySpeed(6);
        this.getRenderComponent().setHitbox(new Rectangle(8, 8, 32, 32));
    }

    public void clone(double angle, double screenPositionX, double screenPositionY, double worldPositionX, double worldPositionY, Directions currentDirection) {
        this.getMovementComponent().setAngle(angle);
        this.getPositionComponent().setScreenPositionX(screenPositionX);
        this.getPositionComponent().setScreenPositionY(screenPositionY);
        this.getPositionComponent().setWorldPositionX(worldPositionX);
        this.getPositionComponent().setWorldPositionY(worldPositionY);
        this.getMovementComponent().setDirection(currentDirection);
    }

}
