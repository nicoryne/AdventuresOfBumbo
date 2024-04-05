package classes.entities;

import classes.entities.projectile.PlayerProjectile;
import classes.entities.projectile.ProjectilePrototype;
import classes.ui.components.GamePanel;
import classes.util.CollisionChecker;
import classes.util.controllers.KeyboardController;
import classes.util.controllers.MouseController;
import classes.util.SpritesManager;
import classes.util.handlers.SoundHandler;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;


public class Player extends MovingEntityObject implements RenderableEntity {

    private static int shootCooldown = 0;
    private static int reloadTime = 20;
    private static int TILE_SIZE;
    private final KeyboardController keyboardController;
    private final MouseController mouseController;
    private final SpritesManager spritesManager;
    private final CollisionChecker collisionChecker;
    private final ArrayList<ProjectilePrototype> projectiles = new ArrayList<>();
    private final ProjectilePrototype projectilePrototype;

    public Player(GamePanel gamePanel, KeyboardController keyboardController, MouseController mouseController) {
        TILE_SIZE = gamePanel.getTileSize();
        this.collisionChecker = gamePanel.getCollisionChecker();
        this.mouseController = mouseController;
        this.keyboardController = keyboardController;
        this.spritesManager = new SpritesManager(
                "player",
                10,
                3,
                TILE_SIZE);
        this.projectilePrototype = new PlayerProjectile(gamePanel, 0,0, 0);

        setScreenPositionX(gamePanel.getScreenWidth() / 2.0 - (TILE_SIZE / 2.0));
        setScreenPositionY(gamePanel.getScreenHeight() / 2.0 - (TILE_SIZE / 2.0));

        setDefaultValues();
    }

    public void setDefaultValues() {
        setSolidArea(new Rectangle(16, 16, 16, 20));

        setWorldPositionX(TILE_SIZE * 24);
        setWorldPositionY(TILE_SIZE * 20);
        setEntitySpeed(3);
        setCurrentDirection("NORTH"); // 'NORTH, SOUTH, EAST, WEST'
    }

    @Override
    public void update() {
        shootCooldown++;
        move();
        look();
        shoot();
        updateProjectiles();
    }

    @Override
    public void render(Graphics2D g) {
        BufferedImage img = spritesManager.getCurrentSprite(getCurrentDirection());

//        double scaleX = (double) TILE_SIZE / img.getWidth();
//        double scaleY = (double) TILE_SIZE / img.getHeight();
//        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
//        img = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR).filter(img, null);
//
//        double tx = getScreenPositionX() - TILE_SIZE / 2.0;
//        double ty = getScreenPositionY() - TILE_SIZE / 2.0;
//
//        double anchorX = TILE_SIZE / 2.0;
//        double anchorY = TILE_SIZE / 2.0;
//
//        AffineTransform at = AffineTransform.getTranslateInstance(tx, ty);
//        at.rotate(getAngle(), anchorX, anchorY);
//
//        g.drawImage(img, at, null);

        int screenX = (int) getScreenPositionX();
        int screenY = (int) getScreenPositionY();
        g.drawImage(img, screenX, screenY, TILE_SIZE, TILE_SIZE, null);

//        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    private void move() {
        boolean validKey = keyboardController.isUpPressed()
                || keyboardController.isDownPressed()
                || keyboardController.isLeftPressed()
                || keyboardController.isRightPressed();


        if (validKey) {
            if (keyboardController.isUpPressed()) {
                setCurrentDirection("NORTH");
            }
            if (keyboardController.isDownPressed()) {
                setCurrentDirection("SOUTH");
            }
            if (keyboardController.isLeftPressed()) {
                setCurrentDirection("WEST");
            }
            if (keyboardController.isRightPressed()) {
                setCurrentDirection("EAST");
            }


            setCollision(false);
            collisionChecker.checkTileCollision(this);

            if(!isCollisionOn()) {
                switch(getCurrentDirection()) {
                    case "NORTH":
                        setWorldPositionY(getWorldPositionY() - getEntitySpeed());
                        break;
                    case "SOUTH":
                        setWorldPositionY(getWorldPositionY() + getEntitySpeed());
                        break;
                    case "WEST":
                        setWorldPositionX(getWorldPositionX() - getEntitySpeed());
                        break;
                    case "EAST":
                        setWorldPositionX(getWorldPositionX() + getEntitySpeed());
                        break;
                }
            }

            spritesManager.updateSprite();
        }
    }

    private void look() {
        double angle = (Math.atan2(
                getScreenPositionY() - mouseController.getMousePositionY(),
                getScreenPositionX() - mouseController.getMousePositionX())) - Math.PI / 2;

        setAngle(angle);
    }

    private void shoot() {
        boolean validKey = keyboardController.isShooting();
        double screenX = getScreenPositionX() + getSolidArea().getWidth();
        double screenY = getScreenPositionY() + getSolidArea().getHeight();

        if (validKey && shootCooldown >= reloadTime) {
            CloneableEntity newProjectile = projectilePrototype.clone();
            PlayerProjectile newPlayerProjectile = (PlayerProjectile) newProjectile;
            newPlayerProjectile.setClone(getAngle(), screenX, screenY, getWorldPositionX(), getWorldPositionY(), getCurrentDirection());
            projectiles.add(newPlayerProjectile);

            SoundHandler.playAudio("shoot-1", 0, 1);
            shootCooldown = 0;
        }
    }

    private void updateProjectiles() {
        Iterator<ProjectilePrototype> iterator = projectiles.iterator();
        while (iterator.hasNext()) {
            ProjectilePrototype projectile = iterator.next();
            if (projectile.isAlive()) {
                projectile.update();
            } else {
                iterator.remove();
            }
        }
    }

    public ArrayList<ProjectilePrototype> getProjectiles() {
        return projectiles;
    }
}