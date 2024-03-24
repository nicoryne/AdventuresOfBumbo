package classes.util;

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
            currentSpriteIndex = (currentSpriteIndex + 1) % spritesAvailablePerDirection;
            frameCounter = 0;
        }
    }

    public BufferedImage getCurrentSprite(String direction) {
        int index = getCurrentSpriteIndex(direction);
        return spriteList.get(index);
    }

    private int getCurrentSpriteIndex(String direction) {
        return switch (direction) {
            case "SOUTH" -> currentSpriteIndex;
            case "EAST" -> currentSpriteIndex + 3;
            case "WEST" -> currentSpriteIndex + 6;
            case "NORTH" -> currentSpriteIndex + 9;
            default -> throw new IllegalStateException("Unexpected value: " + direction);
        };
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
