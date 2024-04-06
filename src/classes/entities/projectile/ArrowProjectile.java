package classes.entities.projectile;

import java.awt.*;

public class ArrowProjectile extends Projectile {

    public ArrowProjectile() {
        this.setProjectileType(ProjectileType.ARROW);
        ProjectileFlyweight projectileFlyweight = ProjectileFlyweightFactory.getFlyweight(getProjectileType());
        this.setSpritesManager(projectileFlyweight.getSpritesManager());

        this.getRenderComponent().setAlive(true);
        this.getMovementComponent().setSpeed(9);
        this.getRenderComponent().setHitbox(new Rectangle(0, 0, 4, 4));
    }

    public void setClone(double angle, double screenPositionX, double screenPositionY, double worldPositionX, double worldPositionY, String currentDirection) {
        this.getMovementComponent().setAngle(angle);
        this.getPositionComponent().setScreenPositionX(screenPositionX);
        this.getPositionComponent().setScreenPositionY(screenPositionY);
        this.getPositionComponent().setWorldPositionX(worldPositionX);
        this.getPositionComponent().setWorldPositionY(worldPositionY);
        this.getMovementComponent().setDirection(currentDirection);
    }
}
