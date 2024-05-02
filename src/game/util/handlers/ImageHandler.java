package game.util.handlers;

import game.Game;
import services.LoggerHelper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class ImageHandler {

    public static BufferedImage getBufferedImage(File file) {
        if(file.isFile() && isImageFile(file)) {
            try {
                return ImageIO.read(file);
            } catch (IOException e) {
                LoggerHelper.logError("Error loading image from file " + file.getAbsolutePath(), e);
            }
        }

        return null;
    }

    public static ImageIcon getImageIcon(String URL) {
        File file = new File(URL);
        new ImageIcon(URL);
        if(file.isFile() && isImageFile(file)) {
            return new ImageIcon(URL);
        }

        return null;
    }

    public static BufferedImage scaleImageBasedOnTileSize(BufferedImage spriteImage, double scale) {
        int tileSize = Integer.parseInt(Game.getInstance().getProperty("TILE_SIZE"));
        int scaledSize = (int) Math.floor(tileSize * scale);
        BufferedImage scaledImage = new BufferedImage(scaledSize, scaledSize, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = scaledImage.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(new Color(0, 0, 0, 0));
        g.fillRect(0, 0, scaledSize, scaledSize);
        g.drawImage(spriteImage, 0, 0, scaledSize, scaledSize, null);
        g.dispose();

        return scaledImage;
    }

    private static boolean isImageFile(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png") || name.endsWith(".gif") || name.endsWith(".bmp");
    }
}
