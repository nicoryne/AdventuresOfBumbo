package classes.entities.projectile;
import classes.Game;
import classes.entities.util.CloneableEntity;
import classes.entities.EntityObject;
import classes.util.handlers.CollisionHandler;
import classes.util.managers.SpritesManager;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class ProjectilePrototype extends EntityObject implements CloneableEntity {

    private final SpritesManager spritesManager;

    public ProjectilePrototype(double screenPositionX, double screenPositionY, double angle) {
        super();
        this.spritesManager = new SpritesManager("projectiles/test", 10, 0);
        getRenderComponent().setAlive(true);
        getMovementComponent().setSpeed(9);
        getPositionComponent().setScreenPositionX(screenPositionX);
        getPositionComponent().setScreenPositionY(screenPositionY);
        getMovementComponent().setAngle(angle);
        getRenderComponent().setHitbox(new Rectangle(0, 0, 4, 4));
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

        getPositionComponent().setScreenPositionX(dxScreen);
        getPositionComponent().setScreenPositionY(dyScreen);
        getPositionComponent().setWorldPositionX(dxWorld);
        getPositionComponent().setWorldPositionY(dyWorld);

        getMovementComponent().setColliding(false);
        CollisionHandler.checkTileCollision(this);

        if(isProjectileOutScreen() || getMovementComponent().isColliding()) {
            kill();
        }
    }

    @Override
    public void render(Graphics2D g) {
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

        g.drawImage(img, at, null);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    public void setClone(double angle, double screenPositionX, double screenPositionY, double worldPositionX, double worldPositionY, String currentDirection) {
        getRenderComponent().setAlive(true);
        getMovementComponent().setAngle(angle);
        getPositionComponent().setScreenPositionX(screenPositionX);
        getPositionComponent().setScreenPositionY(screenPositionY);
        getPositionComponent().setWorldPositionX(worldPositionX);
        getPositionComponent().setWorldPositionY(worldPositionY);
        getMovementComponent().setDirection(currentDirection);
        getRenderComponent().setSprite(getRenderComponent().getSprite());
        getRenderComponent().setHitbox(getRenderComponent().getHitbox());
    }

    @Override
    public CloneableEntity clone() {
        try {
            return (CloneableEntity) (ProjectilePrototype) super.clone();
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
}
