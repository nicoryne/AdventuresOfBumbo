package classes.entity;

import classes.ui.components.GamePanel;
import classes.util.controllers.KeyboardController;
import classes.util.handlers.SpriteHandler;

import java.awt.*;
import java.awt.image.BufferedImage;


public class Player extends MovingEntity {

    private final GamePanel gamePanel;
    private final KeyboardController keyboardController;
    private final SpriteHandler spriteHandler;

    public Player(GamePanel gamePanel, KeyboardController keyboardController) {
        this.gamePanel = gamePanel;
        this.keyboardController = keyboardController;
        this.spriteHandler = new SpriteHandler(
                "player",
                10,
                3);
        setDefaultValues();
    }

    public void setDefaultValues() {
        setPositionX(100);
        setPositionY(100);
        setEntitySpeed(3);
        setCurrentDirection("SOUTH"); // 'NORTH, SOUTH, EAST, WEST'
    }

    public void update() {
        boolean validKey = keyboardController.isUpPressed()
                || keyboardController.isDownPressed()
                || keyboardController.isLeftPressed()
                || keyboardController.isRightPressed();

        if (validKey) {
            if (keyboardController.isUpPressed()) {
                setCurrentDirection("NORTH");
                setPositionY(getPositionY() - getEntitySpeed());
            }
            if (keyboardController.isDownPressed()) {
                setCurrentDirection("SOUTH");
                setPositionY(getPositionY() + getEntitySpeed());
            }
            if (keyboardController.isLeftPressed()) {
                setCurrentDirection("WEST");
                setPositionX(getPositionX() - getEntitySpeed());
            }
            if (keyboardController.isRightPressed()) {
                setCurrentDirection("EAST");
                setPositionX(getPositionX() + getEntitySpeed());
            }

            spriteHandler.updateSprite();
            System.out.println("COORDINATES: {" + getPositionX() + ", " + getPositionY() + "}");
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage img = spriteHandler.getCurrentSprite(getCurrentDirection());

        g2.drawImage(img,
                getPositionX(),
                getPositionY(),
                gamePanel.getTileSize() * 4,
                gamePanel.getTileSize() * 4,
                null);
    }
}