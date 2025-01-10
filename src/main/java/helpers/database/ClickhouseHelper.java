package helpers.database;

import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import io.qameta.allure.Step;

import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static utils.ConfigFactory.*;
import static utils.Utils.getCurrentTimestampDbFormat;
import static utils.Utils.getRandomUuidString;

import java.sql.*;

public class ClickhouseHelper {

    @Step("Make query: {query}")
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

    public static void createSimpleClient(String ucid, String firstName, String lastName)
            throws ReflectiveOperationException, SQLException {
        String lovercaseBrand = ucid.split("-")[0];
        String brand = lovercaseBrand.substring(0, 1).toUpperCase() + lovercaseBrand.substring(1);
        CrmTbUserObject testUser = new CrmTbUserObject(Integer.parseInt(ucid.split("-")[1]), ucid, brand, "FCA", "2024-10-23", firstName, lastName, "male", "1975-05-11", "Cyprus", "CY", "CY", "en", "RUS", "DUrksdLPlqZB6byC9vfKk6qm9BpUmsOS", "BjrbbdAHkwhBFLnPclfvbg==", "996", "1", "2FA", "2", "1", "1", 1, 2, 3, "APPROVED", getCurrentTimestampDbFormat(), "2024-10-23 14:56:59", getRandomUuidString(), "nationalityId");
        System.out.println(testUser.toString());
        insertObjectToDb("vindex_test.crm___tb_user", testUser);
    }

    public static void deleteClient(String ucid) throws SQLException {
        deleteEntryFromDb("vindex_test.crm___tb_user", "ucid = '" + ucid + "'");
    }

}
