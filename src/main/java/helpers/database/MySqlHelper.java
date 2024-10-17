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
        Connection connection;
        Statement statement;
        connection = getConnection();

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            System.out.println("Attempt " + attempt + ": Trying to get data");
            statement = connection.createStatement();
            result = statement.executeQuery(query);
            System.out.println("Data received on attempt " + attempt + ": " + result);
            if (result.next()) {
                break; // Exit the loop if successful
            } else {
                System.out.println("Data not received on attempt " + attempt);
            }
            Thread.sleep(500);
        }
        return result; // If it fails after all attempts
    }
}
