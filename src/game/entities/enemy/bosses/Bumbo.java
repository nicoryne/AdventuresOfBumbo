package game.entities.enemy.bosses;

import game.util.Directions;
import game.util.managers.SpritesManager;

import java.awt.*;

public class Bumbo extends Boss {

    public Bumbo() {
        super(BossName.BUMBO);
        this.getRenderComponent().setAlive(true);
        this.getMovementComponent().setEntitySpeed(1);
        this.getRenderComponent().setHitbox(new Rectangle(8, 8, 20, 20));
        this.getMovementComponent().setDirection(Directions.SOUTH);
        this.getStatComponent().setMaxHitPoints(1000);
        this.getStatComponent().setSpeed(10);
        this.getStatComponent().setDamage(10);
        this.setExpDropped(3.0);
        this.setPointsGiven(50);
    }

    public void initializeSprites() {
        setMovementSpritesManager(new SpritesManager("demon_slime/02_demon_walk", 1, 10));
        setIdleSpritesManager(new SpritesManager("demon_slime/01_demon_idle", 1, 10));
        setAttackSpritesManager(new SpritesManager("demon_slime/03_demon_cleave", 1, 10));
        setDamagedSpritesManager(new SpritesManager("demon_slime/04_demon_take_hit", 1, 10));
        setDyingSpritesManager(new SpritesManager("demon_slime/05_demon_death", 1, 10));
    }
}
