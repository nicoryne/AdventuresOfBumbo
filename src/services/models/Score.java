package services.models;

import java.sql.Time;

public class Score {

    private final int scoreId;

    private final int points;

    private final Time timeSurvived;

    private final int userId;

    public Score(int scoreId, int points, Time timeSurvived, int userId) {
        this.scoreId = scoreId;
        this.points = points;
        this.timeSurvived = timeSurvived;
        this.userId = userId;
    }

    public int getScoreId() {
        return scoreId;
    }

    public int getPoints() {
        return points;
    }

    public Time getTimeSurvived() {
        return timeSurvived;
    }

    public int getUserId() {
        return userId;
    }
}
