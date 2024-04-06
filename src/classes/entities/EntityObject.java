package classes.entities;

import classes.entities.components.MovementComponent;
import classes.entities.components.PositionComponent;
import classes.entities.components.RenderComponent;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class EntityObject implements Entity {

    private PositionComponent<Number> positionComponent;

    private RenderComponent renderComponent;

    private MovementComponent movementComponent;

    private EntityType entityType;


    @Override
    public void update() {}

    @Override
    public void render(Graphics2D g2) {
        BufferedImage sprite = getRenderComponent().getSprite();

        int screenPositionX = getPositionComponent().getScreenPositionX().intValue();
        int screenPositionY = getPositionComponent().getScreenPositionY().intValue();


        g2.drawImage(sprite, screenPositionX, screenPositionY, null);
    }

    @Override
    public void kill() {
        getRenderComponent().setAlive(false);
    }

    @Override
    public void spawn(double x, double y) {
        getPositionComponent().setWorldPositionX(x);
        getPositionComponent().setWorldPositionY(y);
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public PositionComponent<Number> getPositionComponent() {
        if(positionComponent == null) {
            positionComponent = new PositionComponent<>();
        }
        return positionComponent;
    }

    public RenderComponent getRenderComponent() {
        if(renderComponent == null) {
            renderComponent = new RenderComponent();
        }
        return renderComponent;
    }

    public MovementComponent getMovementComponent() {
        if(movementComponent == null) {
            movementComponent = new MovementComponent();
        }
        return movementComponent;
    }
}
