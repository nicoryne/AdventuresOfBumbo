package game.util.managers;

import game.util.handlers.FileHandler;
import services.LoggerHelper;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ConcurrentHashMap;

public class FontManager {

    private final ConcurrentHashMap<String, Font> fontConcurrentHashMap;

    private static FontManager instance;

    private FontManager() {
        this.fontConcurrentHashMap = new ConcurrentHashMap<>();

        loadFonts();
    }

    public static FontManager getInstance() {
        if(instance == null) {
            instance = new FontManager();
        }

        return instance;
    }

    private void loadFonts() {
       File[] directoryListing = FileHandler.getListingsFromRes("font");

       if(directoryListing == null) {
           return;
       }

       for(File child : directoryListing) {
           if(!isFontFile(child)) {
               continue;
           }

           Font newFont = readFont(child.getName());
           if(newFont != null) {
               fontConcurrentHashMap.put(newFont.getFontName(), newFont);
               LoggerHelper.logInfo("[FontManager] Loaded font: " + newFont.getFontName());
           }
       }
    }

    private Font readFont(String name) {
        try {
            String path = "/res/font/";
            InputStream is = getClass().getResourceAsStream(path + name);

            if(is == null) {
                LoggerHelper.logWarning("[FontManager] Font file not found: " + path + name);
                return null;
            }

            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
            return font;
        } catch (FontFormatException | IOException e) {
            LoggerHelper.logError("[FontManager] Error loading font file: " + name, e);
        }

        return null;
    }

    private boolean isFontFile(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".ttf") || name.endsWith(".otf");
    }

    public Font getFont(String name, float size) {
        Font font = fontConcurrentHashMap.get(name);
        return font.deriveFont(size);
    }
}
