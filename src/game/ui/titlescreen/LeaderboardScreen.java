package game.ui.titlescreen;

import com.mysql.cj.log.Log;
import game.Game;
import game.util.handlers.ImageHandler;
import game.util.managers.FontManager;
import services.Leaderboard;
import services.LoggerHelper;
import services.models.Score;
import services.models.ScoreEntry;
import services.models.User;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public abstract class LeaderboardScreen {

    private static int startIndex = 0;
    private static int selectedIndex;
    private static int lastMenuCounter;
    private static boolean isMenuCounterDifferent;
    private static boolean isMenuIncremented;
    private static final ArrayList<ScoreEntry> activeScores = new ArrayList<>();
    private static Leaderboard leaderboard;

    public static void draw(Graphics2D g2, int menuCounter) {
        leaderboard = Game.getInstance().getLeaderboard();
        selectedIndex = menuCounter - 1;
        isMenuCounterDifferent = menuCounter != lastMenuCounter;
        isMenuIncremented = menuCounter > lastMenuCounter;

        leaderboard.sortScoresBy(Leaderboard.LeaderboardFilters.POINTS);
        int tileSize = Integer.parseInt(Game.getInstance().getProperty("TILE_SIZE"));
        int x = tileSize * 2;
        int y = (Game.getInstance().getScreenHeight() / 8) + tileSize;
        int width = Game.getInstance().getScreenWidth() - (tileSize * 4);
        int height = tileSize * 8;

        Color bgColor = new Color(0, 0, 0, 210);
        g2.setColor(bgColor);
        g2.fillRoundRect(x, y, width, height, 35, 35);

        Color strokeColor = new Color(255, 255, 255);
        g2.setColor(strokeColor);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x , y, width, height , 35, 35);

        Font font = FontManager.getInstance().getFont("Dofded", 48f);
        g2.setFont(font);
        y = Game.getInstance().getScreenHeight() / 8;
        if(menuCounter == 0) {
            g2.setColor(Color.gray);
            g2.drawString("<", x, y);
        } else {
            g2.setColor(Color.white);
            g2.drawString("<", x, y);
        }

        y = Game.getInstance().getScreenHeight() / 4;
        font = FontManager.getInstance().getFont("Dofded", 36f);
        g2.setFont(font);
        g2.setColor(Color.white);


        if (menuCounter == 0) {
            startIndex = 0;
        } else if (isMenuCounterDifferent && selectedIndex >= 4 && isMenuIncremented && (leaderboard.getScores().size() - startIndex > 4)) {
            startIndex++;
        } else if (isMenuCounterDifferent && (selectedIndex == startIndex - 1) && startIndex > 0 && !isMenuIncremented) {
            startIndex--;
        }

        updateArrayList(startIndex);


        if(leaderboard.getScores().isEmpty()) {
            String noEntries = "NO ENTRIES";
            x = getXCenteredText(noEntries, g2);
            g2.drawString("NO ENTRIES", x, y + tileSize * 6);
        } else {
            x = tileSize * 2;
            for (int i = startIndex, ctr = 0; i < leaderboard.getScores().size() && ctr < activeScores.size(); i++, ctr++) {
                ScoreEntry currentScoreEntry = activeScores.get(ctr);
                Score score = currentScoreEntry.getScore();
                User user = currentScoreEntry.getUser();
                drawScoreRow(score, user, x, y - 32, g2, i == selectedIndex, i);
                y += tileSize * 2;
            }
        }


        lastMenuCounter = menuCounter;
    }

    private static void updateArrayList(int startIndex) {
        activeScores.clear();
        for(int i = startIndex, ctr = 0; i < leaderboard.getScores().size() && ctr < 4; i++, ctr++) {
            activeScores.add(leaderboard.getScores().get(i));
        }
    }

    private static void drawScoreRow(Score score, User user, int x, int y, Graphics2D g2, boolean isSelected, int index) {
        int tileSize = Integer.parseInt(Game.getInstance().getProperty("TILE_SIZE"));
        int width = Game.getInstance().getScreenWidth() - (tileSize * 4);
        int height = tileSize * 2;
        Color bgColor = new Color(0, 0, 0, 210);
        Color bgSelectedColor = new Color(36, 12, 28, 210);
        if(isSelected) {
            g2.setColor(bgSelectedColor);
        } else {
            g2.setColor(bgColor);
        }
        g2.fillRoundRect(x, y, width, height, 35, 35);

        Color strokeColor = new Color(255, 255, 255);
        g2.setColor(strokeColor);
        g2.setStroke(new BasicStroke(5));
        g2.drawRoundRect(x , y, width, height , 35, 35);

        BufferedImage shieldImage = Game.getInstance().getGamePanel().getHudImage("shield.png");

        g2.drawImage(shieldImage, x + 10, y + 40, null);
        if(user.getUserId() == Game.getInstance().getUser().getUserId()) {
            g2.setColor(Color.orange);
            g2.drawString(String.valueOf(index + 1), (x + 10) + tileSize, y + (height / 2) + 10);
            g2.drawString("you", x + tileSize * 3, y + (height / 2) + 10);
            g2.setColor(Color.white);
        } else {
            g2.drawString(String.valueOf(index + 1), (x + 10) + tileSize, y + (height / 2) + 10);
            g2.drawString(user.getUsername(), x + tileSize * 3, y + (height / 2) + 10);
        }

        BufferedImage pointHudImage = Game.getInstance().getGamePanel().getHudImage("star.png");

        g2.drawImage(pointHudImage, x + (tileSize * 8), y + 20, null);
        g2.drawString(String.valueOf(score.getPoints()), x + (tileSize * 9), y + (height / 2) + 10);

        BufferedImage stopwatchImage = Game.getInstance().getGamePanel().getHudImage("clock.png");

        g2.drawImage(stopwatchImage, x + (tileSize * 12), y + 24, null);
        g2.drawString(score.getTimeSurvived().toString(), x + (tileSize * 13), y + (height / 2) + 10);
}

    private static int getXCenteredText(String text, Graphics2D g2) {
        int length = (int) g2.getFontMetrics().getStringBounds(text, g2).getWidth();

        return Game.getInstance().getScreenWidth() / 2 - length / 2;
    }
}
