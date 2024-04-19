package game.entities.drops;

import game.util.Directions;

import java.awt.*;

public class DropStandardExp extends Drop {

    private double expDropped;

    public DropStandardExp() {
        super(DropType.STANDARD_EXP);
        DropFlyweight dropFlyweight = DropFlyweightFactory.getFlyweight(getDropType());
        this.setSpritesManager(dropFlyweight.getSpritesManager());

        this.getRenderComponent().setAlive(true);
        this.getRenderComponent().setHitbox(new Rectangle(0, 0, 32, 32));
    }

    public void clone(double screenPositionX, double screenPositionY, double worldPositionX, double worldPositionY) {
        this.getPositionComponent().setScreenPositionX(screenPositionX);
        this.getPositionComponent().setScreenPositionY(screenPositionY);
        this.getPositionComponent().setWorldPositionX(worldPositionX);
        this.getPositionComponent().setWorldPositionY(worldPositionY);
    }

    @Override
    public void spawn(double x, double y, double expDropped) {
        super.spawn(x, y);
        this.expDropped = expDropped;
    }

    public double getExpDropped() {
        return expDropped;
    }
}
