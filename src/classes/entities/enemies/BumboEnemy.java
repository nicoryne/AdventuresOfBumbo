package classes.entities.enemies;

import classes.util.Directions;

import java.awt.*;

public class BumboEnemy extends Enemy {

    public BumboEnemy() {
        this.setEnemyType(EnemyType.BUMBO);
        EnemyFlyweight enemyFlyweight = EnemyFlyweightFactory.getFlyweight(getEnemyType());
        this.setSpritesManager(enemyFlyweight.getSpritesManager());

        this.getRenderComponent().setAlive(true);
        this.getMovementComponent().setEntitySpeed(1);
        this.getMovementComponent().setDirection(Directions.SOUTH);
        this.getRenderComponent().setHitbox(new Rectangle(8, 8, 18, 18));
        this.getStatComponent().setMaxHitPoints(100);
        this.getStatComponent().setMaxMana(100);
        this.getStatComponent().setSpeed(50);
        this.getAttributeComponents().setAgility(10);
        this.getAttributeComponents().setIntelligence(10);
        this.getAttributeComponents().setStrength(10);
    }

}
