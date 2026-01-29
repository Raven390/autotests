package helpers.database;

import static business_objects.ui.user.UserFactory.autotestUserOne;
import static helpers.database.DbHelper.*;
import static helpers.database.DbHelper.deleteObjectFromDb;
import static helpers.database.DbName.POSTGRES;
import static org.junit.jupiter.api.Assertions.*;
import static utils.Constants.*;
import static utils.Utils.getCurrentTimestampDbFormat;
import static utils.Utils.writeLog;

import business_objects.db.backoffice_db.Investigation;
import business_objects.db.backoffice_db.InvestigationHistory.InvestigationHistoryObject;
import business_objects.db.backoffice_db.alert.Alert;
import business_objects.db.backoffice_db.backoffice_user.BackofficeUser;
import business_objects.db.backoffice_db.client.Client;
import business_objects.db.backoffice_db.clients_fraud_types.ClientsFraudTypes;
import business_objects.ui.user.User;
import helpers.data.ClientHelper;
import helpers.data.enums.AlertType;
import helpers.data.enums.InvestigationStatus;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import java.util.List;
import java.util.stream.Collectors;

public class BoHelper {

    private static final String NO_SUCH_CLIENT_IN_BO = "No such client in BO";
    private static final String CLIENT_ID_IN_BO = "Client ID in BO ";

    @Step("Close alerts for user {ucid}")
    public static void closeAlert(String ucid) {
        try {
            executeQueryToDb(
                    POSTGRES,
                    String.format(
                            "UPDATE %s SET closed_at ='%s', status = '%s', alert_resolution = 'CONFIRMED' WHERE client_ucid = '%s'",
                            BO_ALERT_TABLE_NAME, getCurrentTimestampDbFormat(), "CLOSED", ucid));
            String userId = getUserIdByUser(autotestUserOne());
            executeQueryToDb(
                    POSTGRES,
                    String.format(
                            "UPDATE %s SET assigned_user_id ='%s', completed_by_user_id = '%s', started_at = '%s', completed_at = '%s', status = 'COMPLETED' WHERE client_ucid = '%s'",
                            BO_INVESTIGATION_TABLE_NAME,
                            userId,
                            userId,
                            getCurrentTimestampDbFormat(),
                            getCurrentTimestampDbFormat(),
                            ucid));
        } catch (Exception e) {
            writeLog("Error closing alert");
        }
    }

    @Step("Close alerts for client")
    public static void closeAlert(ClientHelper client) {
        closeAlert(client.getUcid());
    }

    @Step("Wait for alerts to close for user {ucid}")
    public static void waitForAlertsToClose(String ucid) throws Exception {
        List<Alert> openAlertList = getObjectsFromDB(
                POSTGRES,
                BO_ALERT_TABLE_NAME,
                String.format("client_ucid ='%s' and status = 'OPEN'", ucid),
                Alert.class);
        for (int i = 0; i < 5; i++) {
            if (openAlertList.isEmpty()) {
                break;
            } else {
                Thread.sleep(1000);
                openAlertList = getObjectsFromDB(
                        POSTGRES,
                        BO_ALERT_TABLE_NAME,
                        String.format("client_ucid ='%s' and status = 'OPEN'", ucid),
                        Alert.class);
            }
        }
    }

    @Step("Delete user from BO")
    public static void deleteUserBO(String ucid) {
        try {
            Allure.step("delete user from BO");
            List<Investigation> investigations = getObjectsFromDB(
                    POSTGRES, BO_INVESTIGATION_TABLE_NAME, "client_ucid = '" + ucid + "'", Investigation.class);
            if (!investigations.isEmpty()) {
                String investigationIds = investigations.stream()
                        .map(inv -> String.valueOf(inv.getId()))
                        .collect(Collectors.joining(","));
                deleteObjectFromDb(
                        POSTGRES,
                        BO_INVESTIGATION_HISTORY_TABLE_NAME,
                        "investigation_id IN (" + investigationIds + ")");
            }
            deleteObjectFromDb(POSTGRES, BO_INVESTIGATION_TABLE_NAME, "client_ucid = '" + ucid + "'");
            deleteObjectFromDb(POSTGRES, BO_ALERT_TABLE_NAME, "client_ucid = '" + ucid + "'");
            deleteObjectFromDb(POSTGRES, BO_CLIENT_TABLE_NAME, "ucid = '" + ucid + "'");
        } catch (Exception e) {
            writeLog(NO_SUCH_CLIENT_IN_BO);
        }
    }

    public static void cleanUserAR(String ucid) throws Exception {
        ArHelper.deleteUserFromAbuseRegistry(ucid);
    }

    @Step("Delete user's frauds from BO")
    public static void cleanUserFraudsBo(String ucid) throws Exception {

        deleteObjectFromDb(POSTGRES, BO_CLIENTS_FRAUD_TYPES_TABLE_NAME, "client_ucid = '" + ucid + "'");
    }

    @Step("Check that user have record about fraud in db")
    public static void checkUserFraudBo(String ucid, long expectedFraud) throws Exception {
        Allure.step("Check that user have record about fraud in db");
        long fraud;
        List<Client> client = getObjectsFromDB(POSTGRES, BO_CLIENT_TABLE_NAME, "ucid = '" + ucid + "'", Client.class);
        int boId = client.getFirst().id;
        writeLog(CLIENT_ID_IN_BO + boId);
        List<ClientsFraudTypes> clientsFraudTypes = getObjectsFromDB(
                POSTGRES,
                BO_CLIENTS_FRAUD_TYPES_TABLE_NAME,
                "client_ucid = '" + ucid + "' AND fraud_type_id = '" + expectedFraud + "'",
                ClientsFraudTypes.class);
        fraud = clientsFraudTypes.getFirst().getFraudTypeId();
        writeLog("FRAUD ID " + fraud);
    }

    @Step("Check that user NOT have records about frauds in db")
    public static void checkUserNoFraudBo(String ucid) throws Exception {
        Allure.step("Check that user not have records about frauds in db");
        List<Client> client = getObjectsFromDB(POSTGRES, BO_CLIENT_TABLE_NAME, "ucid = '" + ucid + "'", Client.class);
        int boId = client.getFirst().id;
        writeLog(CLIENT_ID_IN_BO + boId);
        List<ClientsFraudTypes> clientsFraudTypes = getObjectsFromDB(
                POSTGRES, BO_CLIENTS_FRAUD_TYPES_TABLE_NAME, "client_ucid = '" + ucid + "'", ClientsFraudTypes.class);
        assertEquals(clientsFraudTypes.size(), 0);
        assertTrue(clientsFraudTypes.isEmpty());
    }

    @Step("Create fraud for user with ucid '{ucid}' in BO")
    public static void createUserFraudsBo(String ucid, long... fraudIds) throws Exception {
        Thread.sleep(2000);
        for (long fraudId : fraudIds) {
            insertObjectToDb(POSTGRES, BO_CLIENTS_FRAUD_TYPES_TABLE_NAME, new ClientsFraudTypes(fraudId, ucid));
        }
        Thread.sleep(100);
    }

    @Step("Check confirmation status of alert in DB")
    public static void checkUserAlertConfirmation(String ucid, String expectedConfirmation) throws Exception {
        Allure.step("Check confirmation status of alert in DB");
        Thread.sleep(2000);

        List<Client> client = getObjectsFromDB(POSTGRES, BO_CLIENT_TABLE_NAME, "ucid = '" + ucid + "'", Client.class);
        int boId = client.getFirst().id;
        writeLog(CLIENT_ID_IN_BO + boId);
        List<Alert> alert = getObjectsFromDB(POSTGRES, BO_ALERT_TABLE_NAME, "client_id = '" + boId + "'", Alert.class);
        Thread.sleep(100);

        assertEquals(expectedConfirmation, alert.getFirst().getAlertResolution());
    }

    @Step("Get user_id from bo db by user")
    public static String getUserIdByUser(User user) throws Exception {
        return getObjectsFromDB(
                        POSTGRES,
                        BO_BACKOFFICE_USER_TABLE_NAME,
                        String.format(
                                "first_name = '%s' and last_name = '%s'", user.getFirstName(), user.getLastName()),
                        BackofficeUser.class)
                .getFirst()
                .id;
    }

    public static List<Investigation> getClientsInvestigationsDb(String ucid) throws Exception {
        return getObjectsFromDB(
                POSTGRES, BO_INVESTIGATION_TABLE_NAME, "client_ucid = '" + ucid + "'", Investigation.class);
    }

    public static List<Investigation> getClientsInvestigationsDb(String ucid, AlertType type) throws Exception {
        return getObjectsFromDB(
                POSTGRES,
                BO_INVESTIGATION_TABLE_NAME,
                "client_ucid = '" + ucid + "' and type ='" + type + "'",
                Investigation.class);
    }

    public static List<Alert> getClientsAlertsDb(String ucid) throws Exception {
        return getObjectsFromDB(POSTGRES, BO_ALERT_TABLE_NAME, "client_ucid = '" + ucid + "'", Alert.class);
    }

    public static List<Alert> getClientsAlertsDb(String ucid, AlertType type) throws Exception {
        return getObjectsFromDB(
                POSTGRES, BO_ALERT_TABLE_NAME, "client_ucid = '" + ucid + "' and type ='" + type + "'", Alert.class);
    }

    public static Long countInvestigationsDb(AlertType type, InvestigationStatus status) throws Exception {
        List<Investigation> investigations = getObjectsFromDB(
                POSTGRES,
                BO_INVESTIGATION_TABLE_NAME,
                "status = '" + status + "'  and type = '" + type + "'",
                Investigation.class);
        return investigations.stream()
                .map(Investigation::getClientUcid)
                .distinct()
                .count();
    }

    public static Long countUsersInvestigationsDb(AlertType type, String boUserId) throws Exception {
        List<Investigation> investigations = getObjectsFromDB(
                POSTGRES,
                BO_INVESTIGATION_TABLE_NAME,
                "status = 'ACTIVE'  and type = '" + type + "' and assigned_user_id ='" + boUserId + "'",
                Investigation.class);
        return investigations.stream()
                .map(Investigation::getClientUcid)
                .distinct()
                .count();
    }

    public static InvestigationHistoryObject getClientsInvestigationsHistoryLastDb(int investigationId)
            throws Exception {
        return getObjectsFromDB(
                        POSTGRES,
                        BO_INVESTIGATION_HISTORY_TABLE_NAME,
                        "investigation_id = " + investigationId + " ORDER BY happened_at DESC LIMIT 1",
                        InvestigationHistoryObject.class)
                .getFirst();
    }
}
