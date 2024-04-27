package game.util;

import game.Game;
import game.util.handlers.FontHandler;

import java.awt.*;

public class Stopwatch {

    private long startTime;
    private long currentTime;
    private boolean isActive;

    public void start() {
        this.startTime = System.currentTimeMillis();
        isActive = true;
    }
    public double elapsedTime() {
        currentTime = System.currentTimeMillis();
        return (currentTime - startTime) / 1000.0;
    }

    public boolean isActive() {
        return isActive;
    }

    public void draw(Graphics2D g2) {
        Font font = FontHandler.getFont("font-1.ttf", 24f);
        double elapsedTime = elapsedTime();
        int seconds = (int) elapsedTime % 60;
        int minutes = (int) elapsedTime / 60;

        int x = Game.getInstance().getScreenWidth() / 4;
        int y = Game.getInstance().getScreenHeight() / 8;
        String timeString = String.format("%02d:%02d", minutes, seconds);
        g2.setFont(font);
        g2.setColor(Color.white);
        g2.drawString(timeString, x, y);
    }
}
