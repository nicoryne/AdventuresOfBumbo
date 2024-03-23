package classes.entity;

import classes.ui.components.GamePanel;
import classes.util.handlers.KeyboardController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Player extends Entity {

    private final GamePanel gamePanel;
    private final KeyboardController keyboardController;

    public Player(GamePanel gamePanel, KeyboardController keyboardController) {
        this.gamePanel = gamePanel;
        this.keyboardController = keyboardController;

        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        x = 100;
        y = 100;
        speed = 1;
        direction = "down";
    }

    public void getPlayerImage() {
        String RES_FOLDER = "src" + File.separator + "res" + File.separator;
        String SPRITES_FOLDER = RES_FOLDER + "sprites" + File.separator;
        String PLAYER_FOLDER = SPRITES_FOLDER + "player" + File.separator;
        try {
            down1 = ImageIO.read(new File(PLAYER_FOLDER + "l0_bumbo_01.png"));
            down2 = ImageIO.read(new File(PLAYER_FOLDER + "l0_bumbo_02.png"));
            down3 = ImageIO.read(new File(PLAYER_FOLDER + "l0_bumbo_03.png"));
            right1 = ImageIO.read(new File(PLAYER_FOLDER + "l0_bumbo_04.png"));
            right2 = ImageIO.read(new File(PLAYER_FOLDER + "l0_bumbo_05.png"));
            right3 = ImageIO.read(new File(PLAYER_FOLDER + "l0_bumbo_06.png"));
            left1 = ImageIO.read(new File(PLAYER_FOLDER + "l0_bumbo_07.png"));
            left2 = ImageIO.read(new File(PLAYER_FOLDER + "l0_bumbo_08.png"));
            left3 = ImageIO.read(new File(PLAYER_FOLDER + "l0_bumbo_09.png"));
            up1 = ImageIO.read(new File(PLAYER_FOLDER + "l0_bumbo_10.png"));
            up2 = ImageIO.read(new File(PLAYER_FOLDER + "l0_bumbo_11.png"));
            up3 = ImageIO.read(new File(PLAYER_FOLDER + "l0_bumbo_12.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update() {
        if(keyboardController.UP || keyboardController.DOWN || keyboardController.LEFT || keyboardController.RIGHT) {
            if(keyboardController.UP) {
                direction = "up";
                y -= speed;
            } else if (keyboardController.DOWN) {
                direction = "down";
                y += speed;
            } else if (keyboardController.LEFT) {
                direction = "left";
                x -= speed;
            } else if (keyboardController.RIGHT) {
                direction = "right";
                x += speed;
            }

            spriteCounter++;
            if(spriteCounter > 10) {
                if(spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 3;
                } else if (spriteNum == 3) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            }

        }
    }

    public void draw(Graphics2D g2) {
        g2.setColor(Color.white);

        BufferedImage image = switch (direction) {
            case "up" -> getSpriteCounter(spriteNum, up1, up2, up3);
            case "down" -> getSpriteCounter(spriteNum, down1, down2, down3);
            case "left" -> getSpriteCounter(spriteNum, left1, left2, left3);
            case "right" -> getSpriteCounter(spriteNum, right1, right2, right3);
            default -> null;
        };

        g2.drawImage(image, x, y, gamePanel.getTileSize() * 4, gamePanel.getTileSize() * 4, null);
    }

    private BufferedImage getSpriteCounter(int spriteNum, BufferedImage img1, BufferedImage img2, BufferedImage img3) {
        if(spriteNum == 1) {
            return img1;
        } else if (spriteNum == 2) {
            return img2;
        } else if (spriteNum == 3) {
            return img3;
        }
        return null;
    }
}
