package classes.entities.components;

public class StatComponent {

    double maxHitPoints;

    double hitPoints;

    double maxMana;

    double mana;

    int speed;

    double damage;

    public double getHitPoints() {
        return hitPoints;
    }

    public void setHitPoints(double hitPoints) {
        this.hitPoints = hitPoints;
    }

    public double getMana() {
        return mana;
    }

    public void setMana(double mana) {
        this.mana = mana;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public double getMaxHitPoints() {
        return maxHitPoints;
    }

    public void setMaxHitPoints(double maxHitPoints) {
        this.maxHitPoints = maxHitPoints;
    }

    public double getMaxMana() {
        return maxMana;
    }

    public void setMaxMana(double maxMana) {
        this.maxMana = maxMana;
    }

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }
}
