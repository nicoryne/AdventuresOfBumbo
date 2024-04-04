package classes.entities.projectile;
import classes.entities.CloneableEntity;
import classes.entities.MovingEntityObject;
import classes.entities.RenderableEntity;
import classes.ui.components.GamePanel;
import classes.util.CollisionChecker;
import classes.util.SpritesManager;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

public class ProjectilePrototype extends MovingEntityObject implements CloneableEntity, RenderableEntity {

    private final SpritesManager spritesManager;
    private final GamePanel gamePanel;
    private final CollisionChecker collisionChecker;

    public ProjectilePrototype(GamePanel gamePanel, double screenPositionX, double screenPositionY, double angle) {
        super();
        this.gamePanel = gamePanel;
        this.spritesManager = new SpritesManager(
                "projectiles/test",
                10,
                0);
        this.collisionChecker = gamePanel.getCollisionChecker();
        setEntitySpeed(5);
        setScreenPositionX(screenPositionX);
        setScreenPositionY(screenPositionY);

        setAngle(angle);
        setAlive(true);
        setSolidArea(new Rectangle(8, 8, 8, 8));
    }

    @Override
    public void update() {
        double rad = getAngle() - Math.PI / 2;
        setScreenPositionX((getScreenPositionX() + (getEntitySpeed() * Math.cos(rad))));
        setScreenPositionY((getScreenPositionY() + (getEntitySpeed() * Math.sin(rad))));
        setWorldPositionX((getWorldPositionX() + (getEntitySpeed() * Math.cos(rad))));
        setWorldPositionY((getWorldPositionY() + (getEntitySpeed() * Math.sin(rad))));

        setCollision(false);
        collisionChecker.checkTileCollision(this);
        if(isProjectileOutScreen() || isCollisionOn()) {
            setAlive(false);
        }
    }

    @Override
    public void render(Graphics2D g) {
        BufferedImage img = spritesManager.getCurrentSprite();

        double scaleX = (double) gamePanel.getTileSize() / img.getWidth();
        double scaleY = (double) gamePanel.getTileSize() / img.getHeight();
        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
        img = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR).filter(img, null);

        double tx = getScreenPositionX() - gamePanel.getTileSize() / 2.0;
        double ty = getScreenPositionY() - gamePanel.getTileSize() / 2.0;

        double anchorX = gamePanel.getTileSize() / 2.0;
        double anchorY = gamePanel.getTileSize() / 2.0;

        AffineTransform at = AffineTransform.getTranslateInstance(tx, ty);
        at.rotate(getAngle(), anchorX, anchorY);

        g.drawImage(img, at, null);

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    public void setClone(double angle, double screenPositionX, double screenPositionY, double worldPositionX, double worldPositionY, String currentDirection) {
        setAngle(angle);
        setScreenPositionX(screenPositionX);
        setScreenPositionY(screenPositionY);
        setWorldPositionX(worldPositionX);
        setWorldPositionY(worldPositionY);
        setEntityImage(getEntityImage());
        setSolidArea(getSolidArea());
        setCurrentDirection(currentDirection);
    }

    @Override
    public CloneableEntity clone() {
        try {
            CloneableEntity clonedObject = (ProjectilePrototype) super.clone();
            return clonedObject;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }

    private boolean isProjectileOutScreen() {
        return getScreenPositionX() <= 0.0 || getScreenPositionY() <= 0.0 ||
                getScreenPositionX() >= gamePanel.getScreenWidth() || getScreenPositionY() >= gamePanel.getScreenHeight();
    }
}
