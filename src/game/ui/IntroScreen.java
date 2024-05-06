package game.ui;

import game.Game;
import game.util.handlers.ImageHandler;
import game.util.managers.FontManager;

import java.awt.*;
import java.util.Objects;

public abstract class IntroScreen {

    public static void draw(Graphics2D g2, int paragraphState) {
        g2.drawImage(getBackgroundImage(), 0, 0, Game.getInstance().getScreenWidth(), Game.getInstance().getScreenHeight(), null);

        Color mask = new Color(0, 0, 0, 210);
        g2.setColor(mask);

        g2.fillRect(0, 0, Game.getInstance().getScreenWidth(), Game.getInstance().getScreenHeight());

        Font font = FontManager.getInstance().getFont("Pixellari", 24f);
        g2.setColor(Color.white);
        g2.setFont(font);
        String paragraph = Game.getInstance().getIntroDialogue(String.valueOf(paragraphState));

        int x = getXCenteredText(paragraph, g2);
        g2.drawString(paragraph, x, Game.getInstance().getScreenHeight() / 2);
    }

    private static int getXCenteredText(String text, Graphics2D g2) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();

        return Game.getInstance().getScreenWidth() / 2 - length / 2;
    }

    private static Image getBackgroundImage() {
        String path = "src/res/img/forest_bg.gif";
        return Objects.requireNonNull(ImageHandler.getImageIcon(path)).getImage();
    }

}
