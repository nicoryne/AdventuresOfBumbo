package services;

import services.models.Score;
import services.models.ScoreEntry;
import services.models.User;
import services.server.DBConnection;

import java.util.*;

public class Leaderboard {

    public enum LeaderboardFilters{
        POINTS, TIME_SURVIVED, USER_ID, USER_ALPHABETICAL_ASC, USER_ALPHABETICAL_DESC
    }

    private ArrayList<ScoreEntry> scores;

    public Leaderboard() {
        updateLeaderboard();
    }

    public void updateLeaderboard() {
        DBConnection db = new DBConnection();
        try {
            scores = db.getScoreDML().getScores();
        } finally {
            db.close();
        }
    }

    public void sortScoresBy(LeaderboardFilters filter) {
        Comparator<ScoreEntry> comparator = switch (filter) {
            case POINTS -> Comparator.comparing((ScoreEntry entry) -> entry.getScore().getPoints()).reversed();
            case TIME_SURVIVED -> Comparator.comparing((ScoreEntry entry) -> entry.getScore().getTimeSurvived()).reversed();
            case USER_ID -> Comparator.comparing(entry -> entry.getUser().getUserId());
            case USER_ALPHABETICAL_ASC -> Comparator.comparing(entry -> entry.getUser().getUsername());
            case USER_ALPHABETICAL_DESC ->
                    Comparator.comparing((ScoreEntry entry) -> entry.getUser().getUsername())
                            .reversed();
        };
        if (comparator != null) {
            scores.sort(comparator);
        }
    }


    public ArrayList<ScoreEntry> getScores() {
        return scores;
    }
}
