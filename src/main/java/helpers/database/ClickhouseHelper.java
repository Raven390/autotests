package helpers.database;

import static utils.ConfigFactory.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ClickhouseHelper {
    public static ResultSet makeQuery(String query) {
        // JDBC URL for ClickHouse
        String url = CLICKHOUSE_HOST; // Replace with your ClickHouse URL

        // Username and password (if authentication is required)
        String user = CLICKHOUSE_USER; // Use your ClickHouse username
        String password = CLICKHOUSE_PASSWORD; // Use your ClickHouse password (if any)

        ResultSet resultSet = null;
        try {
            // Establish the connection
            Connection connection = DriverManager.getConnection(url, user, password);

            // Create a statement object to execute queries
            Statement statement = connection.createStatement();

            // Execute a query and retrieve the result
            resultSet = statement.executeQuery(query);

            // Iterate over the result set
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1)); // Replace with actual column index or name
            }

            // Close the connection
            // connection.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }
}
