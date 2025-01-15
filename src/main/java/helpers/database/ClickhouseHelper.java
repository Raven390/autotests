package helpers.database;

import businessObjects.db.clickhouse.crmTbAccount.CrmTbAccountObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import io.qameta.allure.Step;
import net.datafaker.Faker;

import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static utils.ConfigFactory.*;
import static utils.Constants.CRM_ACCOUNT_TABLE_NAME;
import static utils.Constants.CRM_USER_TABLE_NAME;
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
        String lowercaseBrand = ucid.split("-")[0];
        String brand = lowercaseBrand.substring(0, 1).toUpperCase() + lowercaseBrand.substring(1);
        CrmTbUserObject testUser = new CrmTbUserObject(Integer.parseInt(ucid.split("-")[1]), ucid, brand, "FCA", "2024-10-23", firstName, lastName, "male", "1975-05-11", "Cyprus", "CY", "CY", "en", "RUS", "DUrksdLPlqZB6byC9vfKk6qm9BpUmsOS", "BjrbbdAHkwhBFLnPclfvbg==", "996", "1", "2FA", "2", "1", "1", 1, 2, 3, "APPROVED", getCurrentTimestampDbFormat(), "2018-10-23 16:32:41.885000000", getRandomUuidString(), "nationalityId");
        System.out.println("created user for injection is " + testUser.toString());
        insertObjectToDb(CRM_USER_TABLE_NAME, testUser);
    }

    public static void createSimpleClient(String ucid)
            throws ReflectiveOperationException, SQLException {
        Faker faker = new Faker();
        String lowercaseBrand = ucid.split("-")[0];
        String brand = lowercaseBrand.substring(0, 1).toUpperCase() + lowercaseBrand.substring(1);
        CrmTbUserObject testUser = new CrmTbUserObject(Integer.parseInt(ucid.split("-")[1]), ucid, brand, "FCA", "2024-10-23", faker.name().firstName(), faker.name().lastName(), "male", "1975-05-11", "Cyprus", "CY", "CY", "en", "RUS", "DUrksdLPlqZB6byC9vfKk6qm9BpUmsOS", "BjrbbdAHkwhBFLnPclfvbg==", "996", "1", "2FA", "2", "1", "1", 1, 2, 3, "APPROVED", getCurrentTimestampDbFormat(), "2018-10-23 16:32:41.885000000", getRandomUuidString(), "nationalityId");
        System.out.println("created user for injection is " + testUser.toString());
        insertObjectToDb(CRM_USER_TABLE_NAME, testUser);
    }

    public static void createStaticClient(String ucid)
            throws ReflectiveOperationException, SQLException {
        String lowercaseBrand = ucid.split("-")[0];
        String brand = lowercaseBrand.substring(0, 1).toUpperCase() + lowercaseBrand.substring(1);
        int userId = Integer.parseInt(ucid.split("-")[1]);
        CrmTbUserObject testUser = new CrmTbUserObject(userId, ucid, brand, "FCA", "2024-10-23", "Jonathan", "Static", "male", "1975-05-11", "Cyprus", "CY", "CY", "en", "RUS", "DUrksdLPlqZB6byC9vfKk6qm9BpUmsOS", "BjrbbdAHkwhBFLnPclfvbg==", "996", "1", "2FA", "2", "1", "1", 1, 2, 3, "APPROVED", "2018-10-23 16:32:41.885000000", "2018-10-23 16:32:41.885000000", "b6775e65-1785-4852-9f32-4dc3c4b8d124", "nationalityId");
        System.out.println("created user for injection is " + testUser.toString());
        insertObjectToDb(CRM_USER_TABLE_NAME, testUser);
    }

    public static void createStaticAccount(String ucid, String status, int index)
            throws ReflectiveOperationException, SQLException {
        String lowercaseBrand = ucid.split("-")[0];
        String brand = lowercaseBrand.substring(0, 1).toUpperCase() + lowercaseBrand.substring(1);
        int userId = Integer.parseInt(ucid.split("-")[1]);
        CrmTbAccountObject testAccount = new CrmTbAccountObject(10, 5, brand, "VFSC2", userId, ucid, "abc9b513-46d1-4f89-8b4a-f882f3d632c1", userId * 100 + index, 18, "ST", 1, "Standard", "M_SVS_0000_USD", "MT4", "2018-10-23 16:32:41.885000000", "2018-10-23 16:32:41.885000000", "2022-11-16", "2022-11-16", status, "2018-10-23 16:32:41.885000000", "2018-10-23 16:32:41.885000000", "2018-10-23 16:32:41.885000000", "2018-10-23 16:32:41.885000000", 1.00, "USD", 1.00, 2.00, 3.00, 4.00, 4, 5.00, 0, 0, 943_793, 943_793, 0, 0, 0, 1, 0, 0, 0, "2018-10-23 16:32:41.885000000", "Static Account");
        System.out.println(testAccount.toString());
        insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, testAccount);
    }

    public static void deleteClient(String ucid) throws SQLException {
        deleteEntryFromDb(CRM_USER_TABLE_NAME, "ucid = '" + ucid + "'");


    }

}
