package game.util;

import game.Game;
import game.util.handlers.ImageHandler;
import game.util.managers.FontManager;
import services.LoggerHelper;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.logging.Logger;

public class Stopwatch {

    private long start;
    private long startPauseTime;
    private long accumulatedPauseTime;
    private long lastActiveTime;
    private boolean isActive;
    private boolean isPaused;
    private BufferedImage stopwatchImage;

    public void start() {
        this.start = System.currentTimeMillis();
        BufferedImage originalStopwatchImage = ImageHandler.getBufferedImage(new File("src/res/hud/clock.png"));
        stopwatchImage = ImageHandler.scaleImageBasedOnTileSize(originalStopwatchImage, 1);
        isActive = true;
        resume();
    }

    public void pause() {
        isPaused = true;
    }

    public void resume() {
        isPaused = false;
    }

    public double elapsedTime() {
        if(!isPaused) {
            long now = System.currentTimeMillis();
            if(startPauseTime != 0) {
                accumulatedPauseTime = now - startPauseTime;
                startPauseTime = 0;
                return (accumulatedPauseTime - start) / 1000.0;
            }
            lastActiveTime = (now - start);

        } else {
            startPauseTime = System.currentTimeMillis();
        }

        return lastActiveTime / 1000.0;
    }

    public long getTime() {
        return lastActiveTime;
    }

    public boolean isActive() {
        return isActive;
    }

    public void draw(Graphics2D g2) {
        int tileSize = Integer.parseInt(Game.getInstance().getProperty("TILE_SIZE"));
        Font font = FontManager.getInstance().getFont("Dofded", 24f);

        double elapsedTime = elapsedTime();
        int seconds = (int) elapsedTime % 60;
        int minutes = (int) elapsedTime / 60;

        int x = tileSize * 5;
        int y = tileSize + (tileSize / 2);
        String timeString = String.format("%02d:%02d", minutes, seconds);
        g2.drawImage(stopwatchImage, x - 45, y - 45, null);
        g2.setFont(font);
        g2.setColor(Color.white);
        g2.drawString(timeString, x, y);
    }
}
