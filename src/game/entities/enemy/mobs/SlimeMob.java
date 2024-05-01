package game.entities.enemy.mobs;

import game.util.Directions;

import java.awt.*;

public class SlimeMob extends Mob {
    public SlimeMob() {
        super(MobName.SLIME);
        MobFlyweight mobFlyweight = MobFlyweightFactory.getFlyweight(getMobName());
        this.setSpritesManager(mobFlyweight.getSpritesManager());
        this.getRenderComponent().setAlive(true);
        this.getMovementComponent().setEntitySpeed(1);
        this.getMovementComponent().setDirection(Directions.SOUTH);
        this.getRenderComponent().setHitbox(new Rectangle(8, 8, 18, 18));
        this.getStatComponent().setMaxHitPoints(100);
        this.getStatComponent().setSpeed(10);
        this.getStatComponent().setDamage(10);
        this.setExpDropped(3.0);
        this.setPointsGiven(50);
    }
}
