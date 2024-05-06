package services.models;

import services.Leaderboard;
import services.server.UserDML;

public class ScoreEntry {

    private Score score;

    private User user;

    public ScoreEntry(Score score, User user) {
        this.score = score;
        this.user = user;
    }

    public Score getScore() {
        return score;
    }

    public User getUser() {
        return user;
    }
}
