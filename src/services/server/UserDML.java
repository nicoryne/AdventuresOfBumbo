package services.server;

import game.Game;
import game.ui.titlescreen.TitleScreen;
import services.FunnyEncryptor;
import services.LoggerHelper;
import services.models.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserDML {

    private final Connection dbConnection;

    public UserDML(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    public boolean addUser(String username, char[] password, String firstName, String lastName, String birthday) {
        if (userExists(username)) {
            LoggerHelper.logWarning("Attempted to register user. But username already exists: " + username);
            return false;
        }

        String query = "INSERT INTO users (username, password, firstname, lastname, birthdate) VALUES (?, ?, ?, ?, ?)";
        String encryptedPassword = FunnyEncryptor.encrypt(password);
        java.sql.Date birthDate = java.sql.Date.valueOf(birthday);
        LoggerHelper.logInfo(birthDate.toString());
        executeUpdate(query, birthDate, username, encryptedPassword, firstName, lastName);
        return true;
    }

    public void updateUserPassword(String username, String newPassword) {
        if (!userExists(username)) {
            LoggerHelper.logWarning("Attempted to update user password. But username does not exist: " + username);
            return;
        }

        String query = "UPDATE users SET password = ? WHERE username = ?";
        executeUpdate(query, newPassword, username);
    }

    public void deleteUser(String username) {
        if (!userExists(username)) {
            LoggerHelper.logWarning("Attempted to delete user. But username does not exist: " + username);
            return;
        }

        String query = "DELETE FROM users WHERE username = ?";
        executeUpdate(query, username);
    }

    public boolean loginUser(String username, char[] password) {
        if (!userExists(username)) {
            LoggerHelper.logWarning("Attempted to login user. But username does not exist: " + username);
            return false;
        }

        String query = "SELECT * FROM users WHERE username = ?";
        try (PreparedStatement preparedStatement = dbConnection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if(resultSet.next()) {
                    String dbEncryptedPassword = resultSet.getString("password");
                    String decryptedPassword = FunnyEncryptor.decrypt(dbEncryptedPassword);
                    String givenString = new String(password);

                    if (givenString.equals(decryptedPassword)) {
                        int userId = resultSet.getInt("user_id");
                        String firstName = resultSet.getString("firstName");
                        String lastName = resultSet.getString("lastName");
                        Date birthday = resultSet.getDate("birthdate");

                        User user = new User(userId, firstName, lastName, username, birthday);
                        Game.getInstance().setUser(user);
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            LoggerHelper.logError("Error when trying to login user: ", e);
        }

        return false;
    }

    private void executeUpdate(String query, java.sql.Date birthDate, String... parameters) {
        try {
            PreparedStatement preparedStatement = dbConnection.prepareStatement(query);
            for (int i = 0; i < parameters.length; i++) {
                preparedStatement.setString(i + 1, parameters[i]);
            }
            preparedStatement.setDate(parameters.length + 1, birthDate);
            preparedStatement.executeUpdate();
            preparedStatement.close();
            LoggerHelper.logInfo("Successfully executed update query: " + query);
        } catch (SQLException e) {
            LoggerHelper.logError("Error executing update query: " + query, e);
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
        String query = "SELECT COUNT(*) AS count FROM users WHERE username = ?";
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
