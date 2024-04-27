package services.server;

import services.LoggerHelper;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection {

    private static final String JDBC_URL = "jdbc:mysql://127.0.0.1:3306/login_schema";

    private static final String USERNAME = "root";

    private static final String PASSWORD = "root";

    private Connection dbConnection;

    public DBConnection() {
        try {
            dbConnection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            LoggerHelper.logError("Error connecting to database", e);
        }
    }

    public void close() {
        if (dbConnection != null) {
            try {
                dbConnection.close();
            } catch (SQLException e) {
                LoggerHelper.logError("Error closing database connection: ", e);
            }
        }
    }

    public UserDML getUserDML() {
        return new UserDML(dbConnection);
    }

    public ScoreDML getScoreDML() {
        return new ScoreDML(dbConnection);
    }
}
