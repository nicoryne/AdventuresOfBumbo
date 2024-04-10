package services.server;

import services.LoggerHelper;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DBConnection {

    private Connection dbConnection;

    public DBConnection() {
       try {
           dbConnection = DriverManager.getConnection(
                   "jdbc:mysql://127.0.0.1:3306/login_schema",
                   "root",
                   "csct-002"
           );
//           while(resultSet.next()) {
//               System.out.println(resultSet.getString("username"));
//               System.out.println(resultSet.getString("password"));
//           }
       } catch (SQLException e) {
           LoggerHelper.logError("Error connecting to database: ", e);
       }
    }

    public void addUser() {

    }

    private ResultSet queryDB(String query)  {
        ResultSet resultSet = null;

        try {
            Statement statement = dbConnection.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            LoggerHelper.logError("Error querying from database: ", e);
        }

        return resultSet;
    }
}
