package game.entities.enemy.bosses;

import game.entities.enemy.Enemy;
import game.entities.enemy.EnemyType;

public class Boss extends Enemy {

    private BossName bossName;

    public Boss(BossName bossName) {
        this.bossName = bossName;
        setEnemyType(EnemyType.BOSS);
    }

    public BossName getBossName() {
        return bossName;
    }

    public void setBossName(BossName bossName) {
        this.bossName = bossName;
    }
}
