package game.entities.enemy.bosses;

import game.Game;
import game.util.Directions;
import game.util.managers.SpritesManager;

import java.awt.*;

public class Bumbo extends Boss {

    public Bumbo() {
        super(BossName.BUMBO);

        int tileSize = Integer.parseInt(Game.getInstance().getProperty("TILE_SIZE"));
        int size = tileSize * 8;
        this.getRenderComponent().setAlive(true);
        this.getMovementComponent().setEntitySpeed(1);
        this.getRenderComponent().setHitbox(new Rectangle(size / 2 - tileSize, size / 2, 128, 256));
        this.getMovementComponent().setDirection(Directions.SOUTH);
        this.getStatComponent().setMaxHitPoints(100);
        this.getStatComponent().setCurrentHitPoints(0);
        this.getStatComponent().setSpeed(10);
        this.getStatComponent().setDamage(10);
        this.setExpDropped(3.0);
        this.setPointsGiven(50);
    }

    public void initializeSprites() {
        int scale = 8;
        setMovementSpritesManager(new SpritesManager("demon_slime/02_demon_walk", 12, scale));
        setIdleSpritesManager(new SpritesManager("demon_slime/01_demon_idle", 6, scale));
        setAttackSpritesManager(new SpritesManager("demon_slime/03_demon_cleave", 15, scale));
        setDamagedSpritesManager(new SpritesManager("demon_slime/04_demon_take_hit", 5, scale));
        setDyingSpritesManager(new SpritesManager("demon_slime/05_demon_death", 2, scale));
    }
}
