package classes.util.handlers;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public abstract class FontHandler {

    public static Font getFont(String name, float size) throws FontFormatException, IOException {
        GraphicsEnvironment GE = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font FONT = null;

        try {
            InputStream IS = FontHandler.class.getResourceAsStream("/res/font/" + name);
            if(IS != null) {
                FONT = Font.createFont(Font.TRUETYPE_FONT, IS).deriveFont(size);
            }
        } catch (FontFormatException e) {
            throw new FontFormatException("FontHandler Error: Font file is bad.");
        } catch (IOException e) {
            throw new IOException();
        }

        GE.registerFont(FONT);
        return FONT;
    }
}
