package services.server;

import services.LoggerHelper;

import java.sql.*;

public class DBConnection {

    private static final String DB_URL = "jdbc:postgresql://aws-0-ap-southeast-1.pooler.supabase.com:5432/postgres?user=postgres.zebyozrztwbzpxjcwrtq&password=uArpJ9Jt39UhfvoHvXGDSSOo32mwMhoS";
    private Connection dbConnection;

    public DBConnection() {
        try {
            dbConnection = DriverManager.getConnection(DB_URL);
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
