package classes.entities.projectile;

import classes.entities.EntityType;
import classes.entities.MovingEntity;
import classes.util.handlers.CollisionHandler;
import classes.util.handlers.RenderHandler;
import classes.util.managers.SpritesManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Projectile extends MovingEntity {

    private ProjectileType projectileType;

    private SpritesManager spritesManager;

    private double projectileSize;

    public Projectile() {
        this.setEntityType(EntityType.PROJECTILE);
    }

    @Override
    public void update() {
        double rad = getMovementComponent().getAngle() - Math.PI / 2.0;

        double screenPositionX = getPositionComponent().getScreenPositionX().doubleValue();
        double screenPositionY = getPositionComponent().getScreenPositionY().doubleValue();
        double worldPositionX = getPositionComponent().getWorldPositionX().doubleValue();
        double worldPositionY = getPositionComponent().getWorldPositionY().doubleValue();
        int speed = getMovementComponent().getEntitySpeed();

        double dxScreen = screenPositionX + (speed * Math.cos(rad));
        double dyScreen = screenPositionY + (speed * Math.sin(rad));
        double dxWorld = worldPositionX + (speed * Math.cos(rad));
        double dyWorld = worldPositionY + (speed * Math.sin(rad));

        updatePositions(dxScreen, dyScreen, dxWorld, dyWorld);
        checkTileCollision();
        checkEntityCollision(this);

        if(isProjectileOutScreen() || getMovementComponent().isColliding()) {
            kill();
        }
    }

    @Override
    public void render(Graphics2D g2) {
        double worldX = getPositionComponent().getWorldPositionX().doubleValue();
        double worldY = getPositionComponent().getWorldPositionY().doubleValue();

        if(RenderHandler.isViewableOnScreen(worldX, worldY)) {
            BufferedImage sprite = spritesManager.getCurrentSprite();
            RenderHandler.renderOnScreen(worldX, worldY, sprite, g2);
        }
    }

    @Override
    public void kill() {
        super.kill();
    }

    @Override
    public void spawn(double x, double y) {
        super.spawn(x, y);
    }


    private boolean isProjectileOutScreen() {
        return !(RenderHandler.isViewableOnScreen(getPositionComponent().getWorldPositionX().doubleValue(),
                getPositionComponent().getWorldPositionY().doubleValue()));
    }

    private void checkTileCollision() {
        getMovementComponent().setColliding(false);
        CollisionHandler.checkTileCollision(this, getMovementComponent().getEntitySpeed());
    }

    private void checkEntityCollision(Projectile projectile) {
        CollisionHandler.checkEntityCollision(projectile);
    }

    private void updatePositions(double dxScreen, double dyScreen, double dxWorld, double dyWorld) {
        getPositionComponent().setScreenPositionX(dxScreen);
        getPositionComponent().setScreenPositionY(dyScreen);
        getPositionComponent().setWorldPositionX(dxWorld);
        getPositionComponent().setWorldPositionY(dyWorld);
    }

    public void setSpritesManager(SpritesManager spritesManager) {
        this.spritesManager = spritesManager;
    }

    public ProjectileType getProjectileType() {
        return projectileType;
    }

    public void setProjectileType(ProjectileType projectileType) {
        this.projectileType = projectileType;
    }

    public double getProjectileSize() {
        return projectileSize;
    }

    public void setProjectileSize(double projectileSize) {
        this.projectileSize = projectileSize;
    }

}
