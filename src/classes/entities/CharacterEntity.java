package classes.entities;

import classes.entities.components.AttributeComponents;
import classes.entities.components.StatComponent;

public class CharacterEntity extends MovingEntity{

    private AttributeComponents attributeComponents;

    private StatComponent statComponent;

    public AttributeComponents getAttributeComponents() {
        if(attributeComponents == null) {
            attributeComponents = new AttributeComponents();
        }
        return attributeComponents;
    }

    public StatComponent getStatComponent() {
        if(statComponent == null) {
            statComponent = new StatComponent();
        }
        return statComponent;
    }

}
