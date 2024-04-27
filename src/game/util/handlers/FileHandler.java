package game.util.handlers;

import java.io.File;

public abstract class FileHandler {

    public static File[] getListingsFromRes(String folderName) {
        String folderPath = "src" + File.separator + "res" + File.separator + folderName + File.separator;
        File dir = new File(folderPath);

        return dir.listFiles();
    }
}
