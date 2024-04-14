package game.util.handlers;

import game.exceptions.FontHandlerException;
import services.LoggerHelper;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public abstract class FontHandler {

    public static Font getFont(String name, float size) {
        try {
            InputStream is = FontHandler.class.getResourceAsStream("/res/font/" + name);

            if (is == null) {
                LoggerHelper.logWarning("[FontHandler] Font file not found: " + name);
                return null;
            }

            Font font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(size);
            GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
            return font;
        } catch (FontFormatException | IOException e) {
            LoggerHelper.logError("[FontHandler] Error loading font file: " + name, e);
        }

        return null;
    }
}
