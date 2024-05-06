package game.entities.enemy.bosses;

import game.Game;
import game.entities.enemy.Enemy;
import game.entities.enemy.EnemyType;
import game.entities.player.Player;
import game.equips.weapons.Weapon;
import game.util.handlers.CollisionHandler;
import game.util.managers.SpritesManager;

import java.awt.*;

public class Boss extends Enemy {

    private SpritesManager movementSpritesManager;
    private SpritesManager idleSpritesManager;
    private SpritesManager attackSpritesManager;
    private SpritesManager damagedSpritesManager;
    private SpritesManager dyingSpritesManager;
    private int moveDelayCounter = 0;

    private BossName bossName;

    public Boss(BossName bossName) {
        this.bossName = bossName;
        setEnemyType(EnemyType.BOSS);
    }

    @Override
    public void update() {
        move();
    }

    @Override
    public void render(Graphics2D g2) {
        super.render(g2);
    }

    private void move() {
        int speed = calculateSpeed();
        int diagonalSpeed = (int) (speed / Math.sqrt(2));
        getMovementComponent().setColliding(false);
        CollisionHandler.checkTileCollision(this, speed);
        CollisionHandler.checkPlayerCollision(this);
        CollisionHandler.checkOtherEnemyCollision(this);

        if(!getMovementComponent().isColliding()) {
            int worldPositionY = getPositionComponent().getWorldPositionY().intValue();
            int worldPositionX = getPositionComponent().getWorldPositionX().intValue();

            handleMovement(worldPositionX, worldPositionY, speed, diagonalSpeed);
        }

        if(moveDelayCounter >= 12) {
            movementSpritesManager.updateSprite();
            this.getRenderComponent().setSprite(movementSpritesManager.getCurrentSprite());
            moveDelayCounter = 0;
        } else {
            moveDelayCounter++;
        }
    }




    public BossName getBossName() {
        return bossName;
    }

    public void setBossName(BossName bossName) {
        this.bossName = bossName;
    }

    public SpritesManager getMovementSpritesManager() {
        return movementSpritesManager;
    }

    public void setMovementSpritesManager(SpritesManager movementSpritesManager) {
        this.movementSpritesManager = movementSpritesManager;
    }

    public SpritesManager getIdleSpritesManager() {
        return idleSpritesManager;
    }

    public void setIdleSpritesManager(SpritesManager idleSpritesManager) {
        this.idleSpritesManager = idleSpritesManager;
    }

    public SpritesManager getAttackSpritesManager() {
        return attackSpritesManager;
    }

    public void setAttackSpritesManager(SpritesManager attackSpritesManager) {
        this.attackSpritesManager = attackSpritesManager;
    }

    public SpritesManager getDamagedSpritesManager() {
        return damagedSpritesManager;
    }

    public void setDamagedSpritesManager(SpritesManager damagedSpritesManager) {
        this.damagedSpritesManager = damagedSpritesManager;
    }

    public SpritesManager getDyingSpritesManager() {
        return dyingSpritesManager;
    }

    public void setDyingSpritesManager(SpritesManager dyingSpritesManager) {
        this.dyingSpritesManager = dyingSpritesManager;
    }
}
