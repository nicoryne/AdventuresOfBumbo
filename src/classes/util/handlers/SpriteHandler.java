package classes.util.handlers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class SpriteHandler {

    private final ArrayList<BufferedImage> spriteList;
    private int currentSpriteIndex;
    private final int spriteChangeOnFrame;
    private int frameCounter;
    private final int spritesAvailablePerDirection;

    public SpriteHandler(String folderName, int spriteChangeOnFrame, int spritesAvailablePerDirection) {
        this.spriteList = loadSpritesFromFolder(folderName);
        this.spriteChangeOnFrame = spriteChangeOnFrame;
        this.currentSpriteIndex = 0;
        this.frameCounter = 0;
        this.spritesAvailablePerDirection = spritesAvailablePerDirection;
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

    public void updateSprite() {
        frameCounter++;
        if (frameCounter > spriteChangeOnFrame) {
            currentSpriteIndex = (currentSpriteIndex + 1) % spritesAvailablePerDirection;
            frameCounter = 0;
        }
    }

    private ArrayList<BufferedImage> loadSpritesFromFolder(String folderName) {
        ArrayList<BufferedImage> sprites = new ArrayList<>();
        String spritesFolderPath = "src" + File.separator + "res" + File.separator + "sprites" + File.separator + folderName + File.separator;
        File dir = new File(spritesFolderPath);

        File[] directoryListing = dir.listFiles();

        if (directoryListing != null) {
            for (File child : directoryListing) {
                if (child.isFile() && isImageFile(child)) {
                    try {
                        sprites.add(ImageIO.read(child));
                    } catch (IOException e) {
                        throw new RuntimeException("Error loading image from file: " + child.getAbsolutePath(), e);
                    }
                }
            }
        }

        return sprites;
    }

    private boolean isImageFile(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png") || name.endsWith(".gif") || name.endsWith(".bmp");
    }
}
