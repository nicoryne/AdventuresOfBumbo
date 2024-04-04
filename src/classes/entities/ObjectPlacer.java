package classes.entities;

import classes.entities.item.WeaponRedSwordObject;
import classes.ui.components.GamePanel;

public class ObjectPlacer {

    private final GamePanel gamePanel;

    public ObjectPlacer(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void setObject() {
       WeaponRedSwordObject testSwordObject = new WeaponRedSwordObject(gamePanel);
       testSwordObject.setWorldPositionX(23 * gamePanel.getTileSize());
       testSwordObject.setWorldPositionY(30 * gamePanel.getTileSize());


       gamePanel.getItemObjectArrayList().add(testSwordObject);

    }
}
