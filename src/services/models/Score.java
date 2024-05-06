package services.models;

import game.equips.weapons.WeaponNames;

import java.sql.Time;

public class Score {

    private int scoreId;

    private final int points;

    private final Time timeSurvived;

    private final int userId;

    private final String weaponUsed;

    public Score(int scoreId, int points, Time timeSurvived, int userId, String weaponUsed) {
        this.scoreId = scoreId;
        this.points = points;
        this.timeSurvived = timeSurvived;
        this.userId = userId;
        this.weaponUsed = weaponUsed;
    }

    public Score(int points, Time timeSurvived, int userId, String weaponUsed) {
        this.points = points;
        this.timeSurvived = timeSurvived;
        this.userId = userId;
        this.weaponUsed = weaponUsed;
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

    public String getWeaponUsed() {
        return weaponUsed;
    }
}
