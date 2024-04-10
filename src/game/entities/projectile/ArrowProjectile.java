package game.entities.projectile;

import game.util.Directions;

import java.awt.*;

public class ArrowProjectile extends Projectile {

    public ArrowProjectile() {
        this.setProjectileType(ProjectileType.ARROW);
        ProjectileFlyweight projectileFlyweight = ProjectileFlyweightFactory.getFlyweight(getProjectileType());
        this.setSpritesManager(projectileFlyweight.getSpritesManager());

        this.getRenderComponent().setAlive(true);
        this.getMovementComponent().setEntitySpeed(9);
        this.getRenderComponent().setHitbox(new Rectangle(0, 0, 32, 32));
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
