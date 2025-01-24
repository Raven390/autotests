package helpers.data.rules.cpaAbuseRule;

import businessObjects.db.clickhouse.boClientFraudTypes.BoClientFraudTypesObject;
import businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObject;
import businessObjects.db.clickhouse.connectionTable.ConnectionTableEntry;

import businessObjects.kafka.crmEvents.WithdrawalEvent;
import generator.annotations.RuleTestData;
import helpers.data.ClientHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static businessObjects.db.clickhouse.crmTbUserTable.CrmTbUserObjectFactory.generateUserByClient;
import static businessObjects.db.clickhouse.emailTable.EmailTableEntryFactory.getEmailTableEntryByClient;
import static businessObjects.db.clickhouse.mtTbUser.MtTbUserObjectFactory.generateMtTbUserData;
import static helpers.data.ClientFactory.getRandomVantageClientAllFields;
import static helpers.data.ClientFactory.getRandomVantageClientNoCpaIbRef;
import static helpers.database.BoHelper.closeAlert;
import static helpers.database.DbHelper.*;
import static helpers.database.MitigationHelper.cleanUserRestriction;
import static utils.Constants.*;
import static utils.Utils.*;

@RuleTestData("cpa-abuse")
public class CpaAbuseRuleDataFactory {
    private static final ClientHelper cpaAbuseRuleExitEventEnd1_1Client = getRandomVantageClientNoCpaIbRef();
    private static final ClientHelper cpaAbuseRuleExitEventEnd1_2Client = getRandomVantageClientNoCpaIbRef();

    @Step("Create data for Mirror trading rule")
    private static CpaAbuseRuleData getCpaAbuseRuleData(ClientHelper client) {
        CrmTbUserObject userObject = generateUserByClient(client);
        WithdrawalEvent withdrawalEvent = new WithdrawalEvent(
                getRandomUuidString(), Instant.now().toString(), getRandomIntPositive(), client.getUserId(), client.getTradingAccount(), client.getBrand(), "vfsc", "FASAPAY", 1, 1d, 1d, 1d, 1d, "555555**** **6666", 1, Instant.now().toString(), "", "", "", 1, "", 1d, 1, 1, "", 1, 1, 1d, 2, 1d, "withdrawal"
        );

        return new CpaAbuseRuleData(withdrawalEvent, client, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), userObject);
    }

    private static ConnectionTableEntry getConnection(ClientHelper fromClient, ClientHelper toClient) {
        return new ConnectionTableEntry(
                fromClient.getUcid(), toClient.getUcid(), "Same Identity", 1d, "{\"payout\": \"463344**** **5603\"}");
    }

    private static class ConnectionAndConnectedUser {
        public ConnectionTableEntry connectionTableEntry;
        public CrmTbUserObject crmTbUserObject;
        public ClientHelper clientHelper;

        public ConnectionAndConnectedUser(ConnectionTableEntry connectionTableEntry,
                CrmTbUserObject crmTbUserObject, ClientHelper clientHelper) {
            this.connectionTableEntry = connectionTableEntry;
            this.crmTbUserObject = crmTbUserObject;
            this.clientHelper = clientHelper;
        }
    }

    private static ConnectionAndConnectedUser getConnectionAndConnectedUser(ClientHelper fromClient,
            ClientHelper toClient) {
        ConnectionTableEntry connectionTableEntry = new ConnectionTableEntry(
                fromClient.getUcid(), toClient.getUcid(), "Same Identity", 1d, "{\"payoutId\": \"463344**** **5603\"}"
        );
        // Create connected user
        CrmTbUserObject connectedCrmTbUserObject = generateUserByClient(toClient);
        connectedCrmTbUserObject.countryCode = fromClient.getCountryCode();
        connectedCrmTbUserObject.rafReferrerId = 22;
        connectedCrmTbUserObject.ibId = 33;
        return new ConnectionAndConnectedUser(connectionTableEntry, connectedCrmTbUserObject, toClient);
    }

    public static CpaAbuseRuleData getCpaAbuseRuleExitEventEnd1_1Data() {
        Allure.step("Get client data");
        CpaAbuseRuleData cpaAbuseRuleData = getCpaAbuseRuleData(cpaAbuseRuleExitEventEnd1_1Client);
        Allure.step("Create user object");
        CrmTbUserObject crmTbUserObject = cpaAbuseRuleData.crmTbUserObject;
        Allure.step("Client has connection to known abuser");

        // Abuser connected clients
        ClientHelper connectedClientMarketManipulator = getRandomVantageClientAllFields();
        ConnectionAndConnectedUser connectionAndConnectedUserMarketManipulator = getConnectionAndConnectedUser(cpaAbuseRuleExitEventEnd1_1Client, connectedClientMarketManipulator);

        cpaAbuseRuleData.clientFraudTypes.add(new BoClientFraudTypesObject(connectedClientMarketManipulator.getUcid(), 1, "MARKET_MANIPULATION"));
        cpaAbuseRuleData.connectedUsers.add(connectionAndConnectedUserMarketManipulator.crmTbUserObject);
        cpaAbuseRuleData.connections.add(connectionAndConnectedUserMarketManipulator.connectionTableEntry);
        cpaAbuseRuleData.connectedClientHelpers.add(connectedClientMarketManipulator);
        cpaAbuseRuleData.mtTbUserObject = generateMtTbUserData(cpaAbuseRuleData.clientHelper.getUcid(), getRandomIntPositive(), 188);
        System.out.println(cpaAbuseRuleData.withdrawalEvent);
        return cpaAbuseRuleData;
    }

    public static CpaAbuseRuleData getCpaAbuseRuleExitEventEnd1_2Data() {
        Allure.step("Create user");
        CpaAbuseRuleData cpaAbuseRuleData = getCpaAbuseRuleData(getRandomVantageClientAllFields());
        Allure.step("70% of connected users have any CPA value");
        Allure.step("Create 2 clients to connect");
        ClientHelper connectedClient = getRandomVantageClientAllFields();
        ClientHelper connectedClient2 = getRandomVantageClientAllFields();
        CrmTbUserObject userObject = generateUserByClient(connectedClient);
        CrmTbUserObject userObject2 = generateUserByClient(connectedClient2);
        Allure.step("Create connections");
        cpaAbuseRuleData.connections.add(getConnection(cpaAbuseRuleData.clientHelper, connectedClient));
        cpaAbuseRuleData.connections.add(getConnection(cpaAbuseRuleData.clientHelper, connectedClient2));
        return cpaAbuseRuleData;
    }

    public static Map<String, CpaAbuseRuleData> setupCpaAbuseRuleData() throws ReflectiveOperationException,
            SQLException {
        startSshTunnel();
        Map<String, CpaAbuseRuleData> map = new HashMap<>();
        // Put all the db data for setup in a map
        map.put("1_1", getCpaAbuseRuleExitEventEnd1_1Data());

        // Loop through the map with data and insert all the data into the according tables
        for (CpaAbuseRuleData data : map.values()) {
            insertObjectToDb(CRM_USER_TABLE_NAME, data.crmTbUserObject);
            insertObjectToDb(EMAIL_TABLE_NAME, getEmailTableEntryByClient(data.clientHelper));
            data.connectedUsers.forEach(user -> {
                try {
                    insertObjectToDb(CRM_USER_TABLE_NAME, user);
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            if (data.connectedClientHelpers != null) {
                data.connectedClientHelpers.forEach(user -> {
                    try {
                        insertObjectToDb(EMAIL_TABLE_NAME, getEmailTableEntryByClient(user));
                    } catch (SQLException | ReflectiveOperationException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
            data.connections.forEach(connection -> {
                try {
                    insertObjectToDb(CONNECTIONS_TABLE_NAME, connection);
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            data.clientFraudTypes.forEach(fraud -> {
                try {
                    insertObjectToDb(BO_CLIENTS_FRAUD_TYPES_TABLE_NAME, fraud);
                } catch (SQLException | ReflectiveOperationException e) {
                    throw new RuntimeException(e);
                }
            });
            if (data.mtTbUserObject != null) {
                insertObjectToDb(CRM_ACCOUNT_TABLE_NAME, data.mtTbUserObject);
            }
        }
        return map;
    }

    public static void deleteCpaAbuseRuleData(Map<String, CpaAbuseRuleData> map) throws Exception {
        // Loop through the map with data and delete all the previously created data into the according tables
        for (CpaAbuseRuleData data : map.values()) {
            deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("user_id = %s", data.crmTbUserObject.userId));
            deleteEntryFromDb(EMAIL_TABLE_NAME, String.format("user_id = %s", data.crmTbUserObject.userId));
            data.connectedUsers.forEach(user -> {
                try {
                    deleteEntryFromDb(CRM_USER_TABLE_NAME, String.format("user_id = %s", user.userId));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            data.connectedUsers.forEach(user -> {
                try {
                    deleteEntryFromDb(EMAIL_TABLE_NAME, String.format("user_id = %s", user.userId));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            data.connections.forEach(connection -> {
                try {
                    deleteEntryFromDb(CONNECTIONS_TABLE_NAME, String.format("user_from = '%s'", connection.userFrom));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            data.clientFraudTypes.forEach(fraud -> {
                try {
                    deleteEntryFromDb(BO_CLIENTS_FRAUD_TYPES_TABLE_NAME, String.format("ucid = '%s'", fraud.ucid));
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            if (data.mtTbUserObject != null) {
                deleteEntryFromDb(CRM_ACCOUNT_TABLE_NAME, String.format("ucid = '%s'", data.mtTbUserObject.ucid));
            }
            cleanUserRestriction(data.clientHelper.getUcid());
            closeAlert(data.clientHelper.getUcid());
        }
        stopSshTunnel();
    }
}
