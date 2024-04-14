package services.server;

import services.LoggerHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDML {

    private final Connection dbConnection;

    public UserDML(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public void addUser(String username, String password) {
        if (userExists(username)) {
            LoggerHelper.logWarning("Attempted to register user. But username already exists: " + username);
            return;
        }

        String query = "INSERT INTO `login_schema`.`users` (`username`, `password`) VALUES (?, ?)";
        executeUpdate(query, username, password);
    }

    public void updateUserPassword(String username, String newPassword) {
        if (!userExists(username)) {
            LoggerHelper.logWarning("Attempted to update user password. But username does not exist: " + username);
            return;
        }

        String query = "UPDATE `login_schema`.`users` SET `password` = ? WHERE `username` = ?";
        executeUpdate(query, newPassword, username);
    }

    public void deleteUser(String username) {
        if (!userExists(username)) {
            LoggerHelper.logWarning("Attempted to delete user. But username does not exist: " + username);
            return;
        }

        String query = "DELETE FROM `login_schema`.`users` WHERE `username` = ?";
        executeUpdate(query, username);
    }

    public boolean loginUser(String username, String password) {
        String query = "SELECT * FROM `users` WHERE `username` = ? AND `password` = ?";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            LoggerHelper.logError("Error when trying to login user: ", e);
            return false;
        }
    }

    private void executeUpdate(String query, String... parameters) {
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            for (int i = 0; i < parameters.length; i++) {
                preparedStatement.setString(i + 1, parameters[i]);
            }
            preparedStatement.executeUpdate();
            preparedStatement.close();
            LoggerHelper.logInfo("Successfully executed update query: " + query);
        } catch (SQLException e) {
            LoggerHelper.logError("Error executing update query: " + query, e);
        }
    }

    private boolean userExists(String username) {
        String query = "SELECT COUNT(*) AS count FROM `login_schema`.`users` WHERE `username` = ?";
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int count = resultSet.getInt("count");
                return count > 0;
            }
            resultSet.close();
            preparedStatement.close();
        } catch (SQLException e) {
            LoggerHelper.logError("Error checking if username exists: " + username, e);
        }
        return false;
    }
}
