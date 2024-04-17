package game.entities.components;

public class StatComponent {

    private double maxHitPoints;

    private double currentHitPoints;

    private int speed;

    private double damage;

    public double getCurrentHitPoints() {
        return currentHitPoints;
    }

    public void setCurrentHitPoints(double currentHitPoints) {
        this.currentHitPoints = currentHitPoints;
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

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        this.damage = damage;
    }
}
