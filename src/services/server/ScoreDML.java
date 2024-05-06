package services.server;

import game.Game;
import services.FunnyEncryptor;
import services.LoggerHelper;
import services.models.Score;
import services.models.ScoreEntry;
import services.models.User;

import java.sql.*;
import java.util.*;
import java.util.Date;

public class ScoreDML {

    private final Connection dbConnection;

    public ScoreDML(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public boolean addUScore(Score score) {
        if (!userExists(score.getUserId())) {
            LoggerHelper.logWarning("Attempted to add score. But userId does not exist: " + score.getUserId());
            return false;
        }

        String query = "INSERT INTO scores (points, time_survived, user_id, weapon_used) VALUES (?, ?, ?, ?)";
        executeUpdate(query, score.getPoints(), score.getTimeSurvived(), score.getUserId(), score.getWeaponUsed());
        return true;
    }

    public ArrayList<ScoreEntry> getScores() {
        ArrayList<ScoreEntry> scores = new ArrayList<>();
        String query = "SELECT * FROM scores";

        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(query)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    int scoreId = resultSet.getInt("score_id");
                    int points = resultSet.getInt("points");
                    Time timeSurvived = resultSet.getTime("time_survived");
                    int userId = resultSet.getInt("user_id");
                    String weaponUsed = resultSet.getString("weapon_used");

                    Score newScore = new Score(scoreId, points, timeSurvived, userId, weaponUsed);
                    scores.add(new ScoreEntry(newScore, getUser(newScore.getUserId())));
                }
            }
        } catch (SQLException e) {
            LoggerHelper.logError("Error when trying to retrieve scores: ", e);
        }

        return scores;
    }

    private User getUser(int userId) {
        String query = "SELECT * FROM users WHERE user_id = ?";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    String firstName = resultSet.getString("firstname");
                    String lastName = resultSet.getString("lastname");
                    String username = resultSet.getString("username");
                    Date birthdate = resultSet.getDate("birthdate");

                    return new User(userId, firstName, lastName, username, birthdate);
                }
            }
        } catch (SQLException e) {
            LoggerHelper.logError("User retrieving from user id: " + userId, e);
        }

        return null;
    }

    private void executeUpdate(String query, int points, Time timeSurvived, int userId, String weaponUsed) {
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setInt(1, points);
            preparedStatement.setTime(2, timeSurvived);
            preparedStatement.setInt(3, userId);
            preparedStatement.setString(4, weaponUsed);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            LoggerHelper.logInfo("Successfully executed update query: " + query);
        } catch (SQLException e) {
            LoggerHelper.logError("Error executing update query: " + query, e);
        }
    }

    private boolean userExists(int userId) {
        String query = "SELECT COUNT(*) AS count FROM users WHERE user_id = ?";
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count > 0;
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            LoggerHelper.logError("Error checking if user exists: UserID: " + userId, e);
        }
        return false;
    }
}
