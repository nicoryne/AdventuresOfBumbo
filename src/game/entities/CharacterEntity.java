package game.entities;

import game.entities.components.StatComponent;

public abstract class CharacterEntity extends MovingEntity{

    private StatComponent statComponent;

    public StatComponent getStatComponent() {
        if(statComponent == null) {
            statComponent = new StatComponent();
        }
        return statComponent;
    }

    public void takeDamage(double damage) {
        statComponent.setCurrentHitPoints(statComponent.getCurrentHitPoints() - damage);
    }

    public double dealDamage() {
        return statComponent.getDamage();
    }

    protected int calculateSpeed() {
        return (getMovementComponent().getEntitySpeed() + ((getStatComponent().getSpeed() / 10)));
    }

}
