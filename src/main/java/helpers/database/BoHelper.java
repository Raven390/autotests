package helpers.database;

import business_objects.db.backoffice_db.alert.Alert;
import business_objects.db.backoffice_db.backoffice_user.BackofficeUser;
import business_objects.db.backoffice_db.client.Client;
import business_objects.db.backoffice_db.clients_fraud_types.ClientsFraudTypes;
import business_objects.ui.user.User;
import helpers.data.ClientHelper;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

import static helpers.database.DbHelper.*;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static org.junit.jupiter.api.Assertions.*;
import static utils.Constants.*;
import static utils.Utils.getCurrentTimestampDbFormat;

public class BoHelper {

    @Step("Close alerts for user {ucid}")
    public static void closeAlert(String ucid) throws SQLException {
        executeQueryToDb(
                DbName.BO, String.format("UPDATE %s SET closed_at ='%s', status = '%s', alert_resolution = 'CONFIRMED' WHERE client_id = (select id from %s where ucid = '%s')", BO_ALERT_TABLE_NAME, getCurrentTimestampDbFormat(), "CLOSED", BO_CLIENT_TABLE_NAME, ucid
                )
        );
    }

    @Step("Close alerts for client")
    public static void closeAlert(ClientHelper client) throws SQLException {
        executeQueryToDb(
                DbName.BO, String.format("UPDATE %s SET closed_at ='%s', status = '%s', alert_resolution = 'CONFIRMED' WHERE client_id = (select id from %s where ucid = '%s')", BO_ALERT_TABLE_NAME, getCurrentTimestampDbFormat(), "CLOSED", BO_CLIENT_TABLE_NAME, client.getUcid()
                )
        );
    }

    @Step("Delete user from BO")
    public static void deleteUserBO(String ucid) throws Exception {
        try {
            Allure.step("delete user from BO");
            List<Client> client = getObjectsFromDB(DbName.BO, BO_CLIENT_TABLE_NAME, "ucid = '" + ucid + "'", Client.class);
            int boId = client.getFirst().id;
            deleteEntryFromDb(DbName.BO, BO_ALERT_TABLE_NAME, "client_id = '" + boId + "'");
            Thread.sleep(100);
            deleteEntryFromDb(DbName.BO, BO_CLIENT_TABLE_NAME, "ucid = '" + ucid + "'");
            Thread.sleep(100);
        } catch (NoSuchElementException e) {
            System.out.println("no such client in BO");
        }
    }

    public static void cleanUserAR(String ucid) throws Exception {
        ArHelper.deleteUserFromAbuseRegistry(ucid);
    }

    @Step("Delete user's frauds from BO")
    public static void cleanUserFraudsBo(String ucid) throws Exception {

        List<Client> client = getObjectsFromDB(DbName.BO, BO_CLIENT_TABLE_NAME, "ucid = '" + ucid + "'", Client.class);
        int boId = client.getFirst().id;
        deleteEntryFromDb(DbName.BO, BO_CLIENTS_FRAUD_TYPES_TABLE_NAME, "client_ucid = '" + ucid + "'");
        Thread.sleep(100);
    }

    @Step("Check that user have record about fraud in db")
    public static void checkUserFraudBo(String ucid, long expectedFraud) throws Exception {
        Allure.step("Check that user have record about fraud in db");
        Thread.sleep(2000);
        long fraud = 0;

        List<Client> client = getObjectsFromDB(DbName.BO, BO_CLIENT_TABLE_NAME, "ucid = '" + ucid + "'", Client.class);
        int boId = client.getFirst().id;
        System.out.println("CLIENT ID IN BO " + boId);
        List<ClientsFraudTypes> clientsFraudTypes = getObjectsFromDB(DbName.BO, BO_CLIENTS_FRAUD_TYPES_TABLE_NAME, "client_ucid = '" + ucid + "' AND fraud_type_id = '" + expectedFraud + "'", ClientsFraudTypes.class);
        Thread.sleep(100);

        fraud = clientsFraudTypes.getFirst().getFraudTypeId();
        System.out.println("FRAUD ID " + fraud);

        assertEquals(expectedFraud, fraud);
    }

    @Step("Check that user NOT have records about frauds in db")
    public static void checkUserNoFraudBo(String ucid) throws Exception {
        Allure.step("Check that user not have records about frauds in db");
        Thread.sleep(2000);

        List<Client> client = getObjectsFromDB(DbName.BO, BO_CLIENT_TABLE_NAME, "ucid = '" + ucid + "'", Client.class);
        int boId = client.getFirst().id;
        System.out.println("CLIENT ID IN BO " + boId);
        List<ClientsFraudTypes> clientsFraudTypes = getObjectsFromDB(DbName.BO, BO_CLIENTS_FRAUD_TYPES_TABLE_NAME, "client_ucid = '" + ucid + "'", ClientsFraudTypes.class);
        Thread.sleep(100);

        assertEquals(clientsFraudTypes.size(), 0);

        assertTrue(clientsFraudTypes.isEmpty());
    }

    @Step("Create fraud for user with ucid '{ucid}' in BO")
    public static void createUserFraudsBo(String ucid, long... fraudIds) throws Exception {
        Thread.sleep(2000);
        for (long fraudId : fraudIds) {
            insertObjectToDb(DbName.BO, BO_CLIENTS_FRAUD_TYPES_TABLE_NAME, new ClientsFraudTypes(fraudId, ucid));
        }
        Thread.sleep(100);
    }


    @Step("Check confirmation status of alert in DB")
    public static void checkUserAlertConfirmation(String ucid, String expectedConfirmation) throws Exception {
        Allure.step("Check confirmation status of alert in DB");
        Thread.sleep(2000);

        List<Client> client = getObjectsFromDB(DbName.BO, BO_CLIENT_TABLE_NAME, "ucid = '" + ucid + "'", Client.class);
        int boId = client.getFirst().id;
        System.out.println("CLIENT ID IN BO " + boId);
        List<Alert> alert = getObjectsFromDB(DbName.BO, BO_ALERT_TABLE_NAME, "client_id = '" + boId + "'", Alert.class);
        Thread.sleep(100);

        assertEquals(expectedConfirmation, alert.getFirst().getAlertResolution());
    }

    @Step("Get user_id from bo db by user")
    public static String getUserIdByUser(User user) throws Exception {
        return getObjectsFromDB(
                DbName.BO, BO_BACKOFFICE_USER_TABLE_NAME, String.format("first_name = '%s' and last_name = '%s'", user.getFirstName(), user.getLastName()), BackofficeUser.class
        ).getFirst().id;
    }

}
