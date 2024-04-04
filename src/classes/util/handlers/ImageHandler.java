package classes.util.handlers;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public abstract class ImageHandler {

    public static BufferedImage getBufferedImage(File file) {
        if(file.isFile() && isImageFile(file)) {
            try {
                return ImageIO.read(file);
            } catch (IOException e) {
                throw new RuntimeException("Error loading image from file: " + file.getAbsolutePath(), e);
            }
        }

        return null;
    }

    public static File[] getListingsFromRes(String folderName) {
        String folderPath = "src" + File.separator + "res" + File.separator + folderName + File.separator;
        File dir = new File(folderPath);

        return dir.listFiles();
    }

    private static boolean isImageFile(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png") || name.endsWith(".gif") || name.endsWith(".bmp");
    }
}
