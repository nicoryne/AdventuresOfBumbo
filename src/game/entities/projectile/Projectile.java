package game.entities.projectile;

import game.entities.EntityType;
import game.entities.MovingEntity;
import game.util.Directions;
import game.util.handlers.CollisionHandler;
import game.util.handlers.RenderHandler;
import game.util.managers.SpritesManager;
import services.LoggerHelper;

import java.awt.*;
import java.awt.geom.AffineTransform;
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
        checkEntityCollision();

        if(isProjectileOutOfScreen() || getMovementComponent().isColliding()) {
            kill();
        }
    }

    @Override
    public void render(Graphics2D g2) {
        double worldX = getPositionComponent().getWorldPositionX().doubleValue();
        double worldY = getPositionComponent().getWorldPositionY().doubleValue();

        if(RenderHandler.isViewableOnScreen(worldX, worldY)) {
            this.getRenderComponent().setSprite(spritesManager.getCurrentSprite(this.getMovementComponent().getDirection(), true));
            RenderHandler.renderOnScreen(worldX, worldY, getRenderComponent().getSprite()   , g2);
        }
    }



    private boolean isProjectileOutOfScreen() {
        return !(RenderHandler.isViewableOnScreen(getPositionComponent().getWorldPositionX().doubleValue(),
                getPositionComponent().getWorldPositionY().doubleValue()));
    }

    private void checkTileCollision() {
        getMovementComponent().setColliding(false);
        CollisionHandler.checkTileCollision(this, getMovementComponent().getEntitySpeed());
    }

    private void checkEntityCollision() {
        CollisionHandler.checkProjectileCollision(this);
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
