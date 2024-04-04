package classes.util;

import classes.util.handlers.ImageHandler;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class SpritesManager {

    private final ArrayList<BufferedImage> spriteList;
    private int currentSpriteIndex;
    private final int spriteChangeOnFrame;
    private int frameCounter;
    private final int spritesAvailablePerDirection;

    public SpritesManager(String folderName, int spriteChangeOnFrame, int spritesAvailablePerDirection) {
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
                sprites.add(ImageHandler.getBufferedImage(child));
            }
        }

        return sprites;
    }
}
