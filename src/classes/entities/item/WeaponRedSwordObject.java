package classes.entities.item;

import classes.entities.EntityObject;
import classes.ui.components.GamePanel;
import classes.util.handlers.ImageHandler;

import java.awt.*;
import java.io.File;

public class WeaponRedSwordObject extends ItemObject {

    public WeaponRedSwordObject(GamePanel gamePanel) {
        super(gamePanel);
        setItemName("RED_SWORD");
        setEntityImage(ImageHandler.getBufferedImage(new File("src/res/items/weapon_sword_red.png")));
    }

}
