package game.entities.enemy.bosses;

import game.Game;
import game.entities.enemy.Enemy;
import game.entities.enemy.EnemyType;
import game.entities.player.Player;
import game.equips.weapons.Weapon;
import game.util.Directions;
import game.util.handlers.CollisionHandler;
import game.util.managers.FontManager;
import game.util.managers.SpritesManager;
import services.LoggerHelper;

import java.awt.*;

public class Boss extends Enemy {

    private SpritesManager movementSpritesManager;
    private SpritesManager idleSpritesManager;
    private SpritesManager attackSpritesManager;
    private SpritesManager damagedSpritesManager;
    private SpritesManager dyingSpritesManager;
    private BossName bossName;

    private int notificationCounter = 0;

    public Boss(BossName bossName) {
        this.bossName = bossName;
        setEnemyType(EnemyType.BOSS);
    }

    @Override
    public void update() {
        switchDirection();
        move();
    }

    @Override
    public void render(Graphics2D g2) {
        super.render(g2);
        if(notificationCounter < 120) {
            drawNotifier(g2);
            notificationCounter++;
        }
    }

    private void switchDirection() {
        Player<Weapon> player = Game.getInstance().getPlayer();
        handleDirections(getPlayerCenterX(player), getPlayerCenterY(player), getEntityLeftX(), getEntityTopY());
    }

    private void handleDirections(int nextX, int nextY, int entityLeftX, int entityTopY) {
        Directions direction;

        if (entityLeftX < nextX) {
            if (entityTopY < nextY) {
                direction = Directions.SOUTH_EAST;
            } else if (entityTopY > nextY) {
                direction = Directions.NORTH_EAST;
            } else {
                direction = Directions.EAST;
            }
        } else if (entityLeftX > nextX) {
            if (entityTopY < nextY) {
                direction = Directions.SOUTH_WEST;
            } else if (entityTopY > nextY) {
                direction = Directions.NORTH_WEST;
            } else {
                direction = Directions.WEST;
            }
        } else {
            if (entityTopY < nextY) {
                direction = Directions.SOUTH;
            } else if (entityTopY > nextY) {
                direction = Directions.NORTH;
            } else {
                direction = getMovementComponent().getDirection();
            }
        }

        getMovementComponent().setDirection(direction);
        CollisionHandler.checkTileCollision(this, calculateSpeed());
        if(getMovementComponent().isColliding()) {
            reverseDirection();
        }
    }

    private void reverseDirection() {
        switch (getMovementComponent().getDirection()) {
            case NORTH_EAST:
                getMovementComponent().setDirection(Directions.SOUTH_WEST);
                break;
            case NORTH_WEST:
                getMovementComponent().setDirection(Directions.SOUTH_EAST);
                break;
            case SOUTH_EAST:
                getMovementComponent().setDirection(Directions.NORTH_WEST);
                break;
            case SOUTH_WEST:
                getMovementComponent().setDirection(Directions.NORTH_EAST);
                break;
            case NORTH:
                getMovementComponent().setDirection(Directions.SOUTH);
                break;
            case SOUTH:
                getMovementComponent().setDirection(Directions.NORTH);
                break;
            case WEST:
                getMovementComponent().setDirection(Directions.EAST);
                break;
            case EAST:
                getMovementComponent().setDirection(Directions.WEST);
                break;
        }
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

        movementSpritesManager.updateSprite();
        this.getRenderComponent().setSprite(movementSpritesManager.getCurrentSprite());
    }

    private void showHitbox(Graphics2D g2, int screenPositionX, int screenPositionY) {
        Rectangle hitbox = CollisionHandler.handleSolidArea(this);
        g2.setColor(Color.RED); // Set the color of the outline
        g2.drawRect((int) (screenPositionX + hitbox.getX()), (int) (screenPositionY + hitbox.getY()), (int) hitbox.getWidth(), (int) hitbox.getHeight());
    }

    private void drawNotifier(Graphics2D g2) {
        String text = "BUMBO HAS SPAWNED!";
        g2.setColor(Color.white);

        Font font = FontManager.getInstance().getFont("Dofded", 34f);

        g2.setFont(font);

        int x = getXCenteredText(text, g2);
        int y = Game.getInstance().getScreenHeight() / 2;

        g2.drawString(text, x, y);
    }

    private static int getXCenteredText(String text, Graphics2D g2) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();

        return Game.getInstance().getScreenWidth() / 2 - length / 2;
    }

    @Override
    public void spawn(double x, double y) {
        super.spawn(x, y);
    }

    private int getEntityLeftX() {
        int worldPositionX = this.getPositionComponent().getWorldPositionX().intValue();
        int xHitbox = (int) this.getRenderComponent().getHitbox().getX();
        return worldPositionX + xHitbox;
    }

    private int getEntityTopY() {
        int worldPositionY = this.getPositionComponent().getWorldPositionY().intValue();
        int yHitbox = (int) this.getRenderComponent().getHitbox().getY();
        return worldPositionY + yHitbox;
    }

    private <T extends Weapon> int getPlayerCenterX(Player<T> player) {
        int playerWorldPositionX = player.getPositionComponent().getWorldPositionX().intValue();
        int playerXHitbox = (int) player.getRenderComponent().getHitbox().getX();
        int widthHitbox = (int) player.getRenderComponent().getHitbox().getWidth();
        return playerWorldPositionX + playerXHitbox + widthHitbox / 2;
    }

    private <T extends Weapon> int getPlayerCenterY(Player<T> player) {
        int playerWorldPositionY = player.getPositionComponent().getWorldPositionY().intValue();
        int playerYHitbox = (int) player.getRenderComponent().getHitbox().getY();
        int heightHitbox = (int) player.getRenderComponent().getHitbox().getHeight();
        return playerWorldPositionY + playerYHitbox + heightHitbox / 2;
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
