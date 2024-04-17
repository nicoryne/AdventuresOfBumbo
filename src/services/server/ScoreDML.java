package services.server;

import game.Game;
import services.FunnyEncryptor;
import services.LoggerHelper;
import services.models.User;

import java.sql.*;
import java.util.Date;

public class ScoreDML {

    private final Connection dbConnection;

    public ScoreDML(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public boolean addUScore(int points, Time timeSurvived, int userId) {
        if (!userExists(userId)) {
            LoggerHelper.logWarning("Attempted to add score. But userId does not exist: " + userId);
            return false;
        }

        String query = "INSERT INTO `login_schema`.`scores` (`points`, `timeSurvived`, `userId`) VALUES (?, ?, ?)";
        executeUpdate(query, points, timeSurvived, userId);
        return true;
    }

    private void executeUpdate(String query, int points, Time timeSurvived, int userId) {
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setInt(1, points);
            preparedStatement.setTime(2, timeSurvived);
            preparedStatement.setInt(3, userId);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            LoggerHelper.logInfo("Successfully executed update query: " + query);
        } catch (SQLException e) {
            LoggerHelper.logError("Error executing update query: " + query, e);
        }
    }

    private boolean userExists(int userId) {
        String query = "SELECT COUNT(*) AS count FROM `login_schema`.`users` WHERE `userId` = ?";
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
