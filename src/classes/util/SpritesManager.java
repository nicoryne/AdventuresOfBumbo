package classes.util;

import classes.entities.tile.Tile;
import classes.util.handlers.ImageHandler;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class SpritesManager {

    private final ArrayList<BufferedImage> spriteList;
    private int currentSpriteIndex;
    private final int spriteChangeOnFrame;
    private int frameCounter;
    private final int spritesAvailablePerDirection;
    private static int TILE_SIZE;

    public SpritesManager(String folderName, int spriteChangeOnFrame, int spritesAvailablePerDirection, int tileSize) {
        TILE_SIZE = tileSize;
        this.spriteList = loadSpritesFromFolder(folderName);
        this.spriteChangeOnFrame = spriteChangeOnFrame;
        this.currentSpriteIndex = 0;
        this.frameCounter = 0;
        this.spritesAvailablePerDirection = spritesAvailablePerDirection;
    }

    public void updateSprite() {
        frameCounter++;
        if (frameCounter > spriteChangeOnFrame) {

            if(spritesAvailablePerDirection != 0) {
                currentSpriteIndex = (currentSpriteIndex + 1) % spritesAvailablePerDirection;
            } else {
                currentSpriteIndex = (currentSpriteIndex + 1);
            }

            frameCounter = 0;
        }
    }

    public BufferedImage getCurrentSprite(String direction) {
        int index = getCurrentSpriteIndex(direction);
        return spriteList.get(index);
    }

    public BufferedImage getCurrentSprite() {
        int index = getCurrentSpriteIndex();
        return spriteList.get(index);
    }

    private int getCurrentSpriteIndex(String direction) {
        return switch (direction) {
            case "SOUTH" -> currentSpriteIndex;
            case "EAST" -> currentSpriteIndex + spritesAvailablePerDirection;
            case "WEST" -> currentSpriteIndex + (spritesAvailablePerDirection * 2);
            case "NORTH" -> currentSpriteIndex + (spritesAvailablePerDirection * 3);
            default -> throw new IllegalStateException("Unexpected value: " + direction);
        };
    }

    private int getCurrentSpriteIndex() {
        return this.currentSpriteIndex;
    }

    private ArrayList<BufferedImage> loadSpritesFromFolder(String folderName) {
        ArrayList<BufferedImage> sprites = new ArrayList<>();
        String filePath = "sprites" + File.separator + folderName;
        File[] directoryListing = ImageHandler.getListingsFromRes(filePath);

        if (directoryListing != null) {
            for (File child : directoryListing) {
                BufferedImage spriteImage = ImageHandler.getBufferedImage(child);
                assert spriteImage != null;

                BufferedImage scaledImage = new BufferedImage(TILE_SIZE, TILE_SIZE, spriteImage.getType());
                Graphics2D g = scaledImage.createGraphics();
                g.drawImage(spriteImage, 0, 0, TILE_SIZE, TILE_SIZE, null);
                g.dispose();

                sprites.add(spriteImage);
            }
        }

        return sprites;
    }
}
