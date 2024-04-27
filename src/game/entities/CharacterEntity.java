package game.entities;

import game.Game;
import game.entities.components.StatComponent;
import game.entities.player.Player;
import game.util.ScreenStates;

import java.awt.*;

public abstract class CharacterEntity extends MovingEntity{

    private StatComponent statComponent;

    private static final int TAKE_DAMAGE_COOLDOWN_MS = 50;

    private int takeDamageCounter = 0;

    private boolean damaged = false;

    public StatComponent getStatComponent() {
        if(statComponent == null) {
            statComponent = new StatComponent();
        }
        return statComponent;
    }

    public void takeDamage(int damage) {
        if(takeDamageCounter >= TAKE_DAMAGE_COOLDOWN_MS) {
            statComponent.setCurrentHitPoints(statComponent.getCurrentHitPoints() - damage);
            damaged = true;
            resetTakeDamageCounter();
        }

        if(statComponent.getCurrentHitPoints() <= 0.0) {
            this.kill();
        }
    }

    protected void showHPBar(Graphics2D g2) {
        int tileSize = Integer.parseInt(Game.getInstance().getProperty("TILE_SIZE"));
        int currentHitPoints = getStatComponent().getCurrentHitPoints();
        int maxHitPoints = getStatComponent().getMaxHitPoints();

        int screenPositionX = getPositionComponent().getScreenPositionX().intValue();
        int screenPositionY = getPositionComponent().getScreenPositionY().intValue();
        int xHitbox = (int) getRenderComponent().getHitbox().getX();
        int yHitbox = (int) getRenderComponent().getHitbox().getY();
        int widthHitbox = (int) getRenderComponent().getHitbox().getWidth();
        int heightHitbox = (int)getRenderComponent().getHitbox().getHeight();
        int dX = screenPositionX - xHitbox - widthHitbox + (tileSize);
        int dY = screenPositionY + yHitbox + heightHitbox + (tileSize / 8);

        g2.setColor(Color.BLACK);
        g2.drawRoundRect(dX, dY, maxHitPoints / 3, 5, 2, 2);
        g2.setColor(Color.RED);
        if(currentHitPoints >= 0) {
            g2.fillRoundRect(dX, dY, currentHitPoints / 3, 5, 2, 2);
        }
    }

    public int dealDamage() {
        return statComponent.getDamage();
    }

    public boolean isDamaged() {
        return damaged;
    }

    protected int calculateSpeed() {
        return (getMovementComponent().getEntitySpeed() + ((getStatComponent().getSpeed() / 10)));
    }

    private void resetTakeDamageCounter() {
        takeDamageCounter = 0;
    }

    protected void incrementTakeDamageCounter() {
        takeDamageCounter++;
    }

}
