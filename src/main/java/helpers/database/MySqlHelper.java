package helpers.database;

import static utils.ConfigFactory.*;

import java.sql.*;

public class MySqlHelper {

    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        Connection connection = null;

        // Load the MySQL JDBC driver and establish connection
        Class.forName("com.mysql.cj.jdbc.Driver");
        System.out.println("Connecting to Database");
        connection = DriverManager.getConnection(
                MYSQL_STAGING_CRM_HOST, MYSQL_STAGING_CRM_LOGIN, MYSQL_STAGING_CRM_PASSWORD);
        // Check if the connection is successful
        if (connection == null) {
            System.out.println("Database Connection Failed");
        } else {
            System.out.println("Database Connection Successful");
        }
        return connection;
    }

    public static ResultSet makeQuery(String query, int maxAttempts)
            throws SQLException, ClassNotFoundException, InterruptedException {
        ResultSet result = null;
        Connection connection = null;
        Statement statement = null;

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                connection = getConnection();
                System.out.println("Attempt " + attempt + ": Trying to get data");

                statement = connection.createStatement();
                result = statement.executeQuery(query);

                System.out.println("Data received on attempt " + attempt + ": " + result);
                result.next();
                return result; // Exit the loop if successful
            } catch (SQLException e) {
                System.err.println("Attempt " + attempt + " failed: " + e.getMessage());

                if (attempt == maxAttempts) {
                    throw e; // Rethrow the exception if it's the last attempt
                }

                Thread.sleep(1000); // Wait 1 second before retrying
            }
        }

        return null; // If it fails after all attempts
    }
}
