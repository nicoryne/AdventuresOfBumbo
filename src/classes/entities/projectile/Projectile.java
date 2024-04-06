package classes.entities.projectile;

import classes.Game;
import classes.entities.EntityObject;
import classes.entities.util.CloneableEntity;
import classes.util.handlers.CollisionHandler;
import classes.util.managers.SpritesManager;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class Projectile extends EntityObject implements CloneableEntity {

    private ProjectileType projectileType;

    private SpritesManager spritesManager;

    public void setSpritesManager(SpritesManager spritesManager) {
        this.spritesManager = spritesManager;
    }

    public ProjectileType getProjectileType() {
        return projectileType;
    }

    public void setProjectileType(ProjectileType projectileType) {
        this.projectileType = projectileType;
    }

    @Override
    public void update() {
        double rad = getMovementComponent().getAngle() - Math.PI / 2.0;

        double screenPositionX = getPositionComponent().getScreenPositionX().doubleValue();
        double screenPositionY = getPositionComponent().getScreenPositionY().doubleValue();
        double worldPositionX = getPositionComponent().getWorldPositionX().doubleValue();
        double worldPositionY = getPositionComponent().getWorldPositionY().doubleValue();
        int speed = getMovementComponent().getSpeed();

        double dxScreen = screenPositionX + (speed * Math.cos(rad));
        double dyScreen = screenPositionY + (speed * Math.sin(rad));
        double dxWorld = worldPositionX + (speed * Math.cos(rad));
        double dyWorld = worldPositionY + (speed * Math.sin(rad));

        updatePositions(dxScreen, dyScreen, dxWorld, dyWorld);
        checkCollision();

        if(isProjectileOutScreen() || getMovementComponent().isColliding()) {
            kill();
        }
    }

    @Override
    public void render(Graphics2D g2) {
        BufferedImage img = spritesManager.getCurrentSprite();
        double tileSize = Double.parseDouble(Game.getInstance().getProperty("TILE_SIZE"));
        double screenPositionX = getPositionComponent().getScreenPositionX().doubleValue();
        double screenPositionY = getPositionComponent().getScreenPositionY().doubleValue();
        double angle = getMovementComponent().getAngle();
        double scaleX = tileSize / img.getWidth();
        double scaleY = tileSize / img.getHeight();

        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
        img = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR).filter(img, null);

        double tx = screenPositionX - tileSize / 2.0;
        double ty = screenPositionY - tileSize / 2.0;

        double anchorX = tileSize / 2.0;
        double anchorY = tileSize / 2.0;

        AffineTransform at = AffineTransform.getTranslateInstance(tx, ty);
        at.rotate(angle, anchorX, anchorY);

        g2.drawImage(img, at, null);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        System.out.println("render");
    }

    @Override
    public void kill() {
        super.kill();
    }

    @Override
    public void spawn(double x, double y) {
        super.spawn(x, y);
    }

    @Override
    public CloneableEntity clone() {
        try {
            return (CloneableEntity) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    private boolean isProjectileOutScreen() {
        int screenPositionX = getPositionComponent().getScreenPositionX().intValue();
        int screenPositionY = getPositionComponent().getScreenPositionY().intValue();
        int screenWidth = Game.getInstance().getScreenWidth();
        int screenHeight = Game.getInstance().getScreenHeight();

        return screenPositionX <= 0.0 || screenPositionY <= 0.0 ||
                screenPositionX >= screenWidth || screenPositionY >= screenHeight;
    }

    private void checkCollision() {
        getMovementComponent().setColliding(false);
        CollisionHandler.checkTileCollision(this);
    }

    private void updatePositions(double dxScreen, double dyScreen, double dxWorld, double dyWorld) {
        getPositionComponent().setScreenPositionX(dxScreen);
        getPositionComponent().setScreenPositionY(dyScreen);
        getPositionComponent().setWorldPositionX(dxWorld);
        getPositionComponent().setWorldPositionY(dyWorld);
    }


}
