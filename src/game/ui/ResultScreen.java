package game.ui;

import game.Game;
import game.util.managers.FontManager;
import services.models.Score;

import java.awt.*;

public abstract class ResultScreen {

    public static void draw(Graphics2D g2, Score score) {
        String text = "score lol";
        g2.drawString(text, 0, 0);
    }

}
