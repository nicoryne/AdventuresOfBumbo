package classes.entities;

import classes.ui.components.GamePanel;
import classes.util.KeyboardController;
import classes.util.SpritesManager;

import java.awt.*;
import java.awt.image.BufferedImage;


public class Player extends MovingEntity {

    private static int TILE_SIZE;
    private final KeyboardController keyboardController;
    private final SpritesManager spritesManager;


    public Player(GamePanel gamePanel, KeyboardController keyboardController) {
        TILE_SIZE = gamePanel.getTileSize();
        this.keyboardController = keyboardController;
        this.spritesManager = new SpritesManager(
                "player",
                10,
                3);
        setScreenPositionX(gamePanel.getScreenWidth() / 2 - (TILE_SIZE / 2));
        setScreenPositionY(gamePanel.getScreenHeight() / 2 - (TILE_SIZE / 2));
        setDefaultValues();
    }

    public void setDefaultValues() {
        setSolidArea(new Rectangle(0, 0, 48, 48));

        setWorldPositionX(TILE_SIZE * 23);
        setWorldPositionY(TILE_SIZE * 21);
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
                setWorldPositionY(getWorldPositionY() - getEntitySpeed());
            }
            if (keyboardController.isDownPressed()) {
                setCurrentDirection("SOUTH");
                setWorldPositionY(getWorldPositionY() + getEntitySpeed());
            }
            if (keyboardController.isLeftPressed()) {
                setCurrentDirection("WEST");
                setWorldPositionX(getWorldPositionX() - getEntitySpeed());
            }
            if (keyboardController.isRightPressed()) {
                setCurrentDirection("EAST");
                setWorldPositionX(getWorldPositionX() + getEntitySpeed());
            }

            spritesManager.updateSprite();
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage img = spritesManager.getCurrentSprite(getCurrentDirection());

        g2.drawImage(img,
                getScreenPositionX(),
                getScreenPositionY(),
                TILE_SIZE,
                TILE_SIZE,
                null);
    }
}