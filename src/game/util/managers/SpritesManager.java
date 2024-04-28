package game.util.managers;

import game.util.Directions;
import game.util.handlers.FileHandler;
import game.util.handlers.ImageHandler;
import services.LoggerHelper;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class SpritesManager {

    private final ArrayList<BufferedImage> spriteList;
    private int currentSpriteIndex;
    private final int spriteChangeOnFrame;
    private int frameCounter;
    private final int spritesAvailablePerDirection;
    private final double scale;

    public SpritesManager(String folderName, int spritesAvailablePerDirection, double scale) {
        this.scale = scale;
        this.spriteList = loadSpritesFromFolder(folderName);
        this.spriteChangeOnFrame = 10;
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

    public BufferedImage getCurrentSprite(Directions direction) {
        int index = getCurrentSpriteIndex(direction);
        return spriteList.get(index);
    }

    public BufferedImage getCurrentSprite() {
        int index = getCurrentSpriteIndex();
        return spriteList.get(index);
    }

    private int getCurrentSpriteIndex(Directions direction) {
        return switch (direction) {
            case EAST -> currentSpriteIndex;
            case NORTH -> currentSpriteIndex + spritesAvailablePerDirection;
            case NORTH_EAST -> currentSpriteIndex + (spritesAvailablePerDirection * 2);
            case NORTH_WEST -> currentSpriteIndex + (spritesAvailablePerDirection * 3);
            case SOUTH, NONE -> currentSpriteIndex + (spritesAvailablePerDirection * 4);
            case SOUTH_EAST -> currentSpriteIndex + (spritesAvailablePerDirection * 5);
            case SOUTH_WEST -> currentSpriteIndex + (spritesAvailablePerDirection * 6);
            case WEST -> currentSpriteIndex + (spritesAvailablePerDirection * 7);
        };
    }

    private int getCurrentSpriteIndex() {
        return this.currentSpriteIndex;
    }

    private ArrayList<BufferedImage> loadSpritesFromFolder(String folderName) {
        ArrayList<BufferedImage> sprites = new ArrayList<>();
        String filePath = "sprites" + File.separator + folderName;
        File[] directoryListing = FileHandler.getListingsFromRes(filePath);

        if (directoryListing != null) {
            for (File child : directoryListing) {
                BufferedImage spriteImage = ImageHandler.getBufferedImage(child);

                if(spriteImage == null) {
                    continue;
                }

                BufferedImage scaledImage = ImageHandler.scaleImageBasedOnTileSize(spriteImage, scale);
                LoggerHelper.logInfo("[SpritesManager] Loaded sprite image file: " + child.getName());
                sprites.add(scaledImage);
            }
        }

        return sprites;
    }
}
