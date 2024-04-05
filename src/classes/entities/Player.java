package classes.entities;

import classes.Game;
import classes.entities.projectile.PlayerProjectile;
import classes.entities.projectile.ProjectilePrototype;
import classes.ui.GamePanel;
import classes.util.handlers.CollisionHandler;
import classes.util.controllers.KeyboardController;
import classes.util.controllers.MouseController;
import classes.util.managers.SpritesManager;
import classes.util.handlers.SoundHandler;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;


public class Player extends EntityObject {

    private static int reloadCooldown = 0;
    private static int reloadTime = 20;
    private static int TILE_SIZE;
    private final KeyboardController keyboardController;
    private final MouseController mouseController;
    private final SpritesManager spritesManager;
    private final ArrayList<ProjectilePrototype> projectiles = new ArrayList<>();
    private final ProjectilePrototype projectilePrototype;

    public Player(KeyboardController keyboardController, MouseController mouseController) {
        TILE_SIZE = Integer.parseInt(Game.getInstance().getProperty("TILE_SIZE"));
        this.mouseController = mouseController;
        this.keyboardController = keyboardController;
        this.spritesManager = new SpritesManager("player", 10, 3);
        this.projectilePrototype = new PlayerProjectile(0,0, 0);

        setDefaultValues();
    }

    public void setDefaultValues() {
        double spawnX = TILE_SIZE * 24;
        double spawnY = TILE_SIZE * 20;
        getRenderComponent().setAlive(true);
        getPositionComponent().setScreenPositionX(Game.getInstance().getScreenWidth() / 2.0 - (TILE_SIZE / 2.0));
        getPositionComponent().setScreenPositionY(Game.getInstance().getScreenHeight() / 2.0 - (TILE_SIZE / 2.0));
        spawn(spawnX, spawnY);
        getRenderComponent().setHitbox(new Rectangle(16, 16, 16, 20));
        getMovementComponent().setSpeed(3);
        getMovementComponent().setDirection("NORTH"); // 'NORTH, SOUTH, EAST, WEST'
    }

    @Override
    public void update() {
        reloadCooldown++;
        move();
        look();
        shoot();
        updateProjectiles();
    }

    @Override
    public void render(Graphics2D g2) {
        BufferedImage sprite = spritesManager.getCurrentSprite(getMovementComponent().getDirection());

        int screenPositionX = getPositionComponent().getScreenPositionX().intValue();
        int screenPositionY = getPositionComponent().getScreenPositionY().intValue();

        g2.drawImage(sprite, screenPositionX, screenPositionY,null);
    }

    private void move() {
        boolean validKey = keyboardController.isUpPressed()
                || keyboardController.isDownPressed()
                || keyboardController.isLeftPressed()
                || keyboardController.isRightPressed();


        if (validKey) {
            if (keyboardController.isUpPressed()) {
                getMovementComponent().setDirection("NORTH");
            }
            if (keyboardController.isDownPressed()) {
                getMovementComponent().setDirection("SOUTH");
            }
            if (keyboardController.isLeftPressed()) {
                getMovementComponent().setDirection("WEST");
            }
            if (keyboardController.isRightPressed()) {
                getMovementComponent().setDirection("EAST");
            }


            getMovementComponent().setColliding(false);
            CollisionHandler.checkTileCollision(this);

            if(!getMovementComponent().isColliding()) {
                int worldPositionY = getPositionComponent().getWorldPositionY().intValue();
                int worldPositionX = getPositionComponent().getWorldPositionX().intValue();
                int speed = getMovementComponent().getSpeed();

                switch(getMovementComponent().getDirection()) {
                    case "NORTH":
                        getPositionComponent().setWorldPositionY(worldPositionY - speed);
                        break;
                    case "SOUTH":
                        getPositionComponent().setWorldPositionY(worldPositionY + speed);
                        break;
                    case "WEST":
                        getPositionComponent().setWorldPositionX(worldPositionX - speed);
                        break;
                    case "EAST":
                        getPositionComponent().setWorldPositionX(worldPositionX + speed);
                        break;
                }
            } else {
                SoundHandler.playAudio("bump-1", 0, 1.0f);
            }

            spritesManager.updateSprite();

        }
    }

    private void look() {
        int screenPositionX = getPositionComponent().getScreenPositionX().intValue();
        int screenPositionY = getPositionComponent().getScreenPositionY().intValue();

        int dx = screenPositionX - mouseController.getMousePositionX();
        int dy = screenPositionY - mouseController.getMousePositionY();

        double angle = (Math.atan2(dy, dx)) - Math.PI / 2.0;

        getMovementComponent().setAngle(angle);
    }

    private void shoot() {
        boolean validKey = keyboardController.isShooting();
        double screenX = getPositionComponent().getScreenPositionX().doubleValue() + getRenderComponent().getHitbox().getWidth();
        double screenY = getPositionComponent().getScreenPositionY().doubleValue() + getRenderComponent().getHitbox().getHeight();
        double angle = getMovementComponent().getAngle();
        double worldPositionX = getPositionComponent().getWorldPositionX().doubleValue();
        double worldPositionY = getPositionComponent().getWorldPositionY().doubleValue();
        String direction = getMovementComponent().getDirection();

        if (validKey && reloadCooldown >= reloadTime) {
            PlayerProjectile newPlayerProjectile = new PlayerProjectile(screenX, screenY, angle);
            newPlayerProjectile.setClone(angle, screenX, screenY, worldPositionX, worldPositionY, direction);
            projectiles.add(newPlayerProjectile);

            SoundHandler.playAudio("shoot-1", 0, 1.0f);
            reloadCooldown = 0;
        }
    }

    private void updateProjectiles() {
        Iterator<ProjectilePrototype> iterator = projectiles.iterator();
        while (iterator.hasNext()) {
            ProjectilePrototype projectile = iterator.next();
            if (projectile.getRenderComponent().isAlive()) {
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