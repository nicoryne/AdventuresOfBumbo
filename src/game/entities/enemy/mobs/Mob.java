package game.entities.enemy.mobs;

import game.entities.enemy.Enemy;
import game.entities.enemy.EnemyType;

public class Mob extends Enemy {

    private MobName mobName;

    public Mob(MobName mobName) {
        this.mobName = mobName;
        setEnemyType(EnemyType.MOB);
    }

    public MobName getMobName() {
        return mobName;
    }

    public void setMobName(MobName mobName) {
        this.mobName = mobName;
    }
}
