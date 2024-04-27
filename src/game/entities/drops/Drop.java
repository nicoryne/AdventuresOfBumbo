package game.entities.drops;

import game.Game;
import game.entities.EntityType;
import game.entities.MovingEntity;
import game.entities.player.Player;
import game.equips.weapons.Weapon;
import game.util.handlers.RenderHandler;
import game.util.managers.SpritesManager;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Drop extends MovingEntity {

    private DropType dropType;

    private SpritesManager spritesManager;

    private static double MAGNET_RADIUS = 5.0;

    public Drop(DropType dropType) {
        this.setEntityType(EntityType.DROP);
        this.dropType = dropType;
    }

    @Override
    public void update() {
//        Player<Weapon> player = Game.getInstance().getPlayer();
//        if(isPlayerFar(player)) {
//            return;
//        }
//
//        int playerWorldPositionX = player.getPositionComponent().getWorldPositionX().intValue();
//        int playerWorldPositionY = player.getPositionComponent().getWorldPositionY().intValue();
//        int playerXHitbox = (int) player.getRenderComponent().getHitbox().getX();
//        int playerYHitbox = (int) player.getRenderComponent().getHitbox().getY();
//        int widthHitbox = (int) player.getRenderComponent().getHitbox().getWidth();
//        int heightHitbox = (int) player.getRenderComponent().getHitbox().getHeight();
//        int playerCenterX = playerWorldPositionX + playerXHitbox + widthHitbox / 2;
//        int playerCenterY = playerWorldPositionY + playerYHitbox + heightHitbox / 2;
//
//        int worldPositionX = this.getPositionComponent().getWorldPositionX().intValue();
//        int worldPositionY = this.getPositionComponent().getWorldPositionY().intValue();
//        int xHitbox = (int) this.getRenderComponent().getHitbox().getX();
//        int yHitbox = (int) this.getRenderComponent().getHitbox().getY();
//        int entityLeftX = worldPositionX + xHitbox;
//        int entityTopY = worldPositionY + yHitbox;
//
//        handleMovement(playerCenterX, playerCenterY, entityLeftX, entityTopY);
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

    private void handleMovement(int nextX, int nextY, int entityLeftX, int entityTopY) {
        double dx = nextX - (entityLeftX + getRenderComponent().getHitbox().getWidth() / 2);
        double dy = nextY - (entityTopY + getRenderComponent().getHitbox().getHeight() / 2);
        double distance = Math.sqrt(dx * dx + dy * dy);

        if(distance < MAGNET_RADIUS) {
            double factor = 0.5;
            double newX = entityLeftX + dx * factor;
            double newY = entityTopY + dy * factor;

            getPositionComponent().setWorldPositionX(newX);
            getPositionComponent().setWorldPositionY(newY);
        }
    }

    private boolean isPlayerFar(MovingEntity player) {
        double distanceSquared = calculateDistanceSquared(this, player);
        double thresholdDistanceSquared = 64 * 64;

        return distanceSquared >= thresholdDistanceSquared;
    }

    private double calculateDistanceSquared(MovingEntity entity1, MovingEntity entity2) {
        double dx = entity1.getPositionComponent().getWorldPositionX().doubleValue() - entity2.getPositionComponent().getWorldPositionX().doubleValue();
        double dy = entity1.getPositionComponent().getWorldPositionY().doubleValue() - entity2.getPositionComponent().getWorldPositionY().doubleValue();
        return dx * dx + dy * dy;
    }
    public DropType getDropType() {
        return dropType;
    }

    public void setDropType(DropType dropType) {
        this.dropType = dropType;
    }

    public SpritesManager getSpritesManager() {
        return spritesManager;
    }

    public void setSpritesManager(SpritesManager spritesManager) {
        this.spritesManager = spritesManager;
    }

    public abstract void spawn(double x, double y, double expDropped);
}
