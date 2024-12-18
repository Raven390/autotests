package helpers.database;

import businessObjects.db.backofficeDb.alert.Alert;
import businessObjects.db.backofficeDb.client.Client;
import businessObjects.db.backofficeDb.clientFraudTypes.ClientFraudTypes;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import java.sql.SQLException;
import java.util.List;

import static helpers.database.DbHelper.*;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static org.junit.jupiter.api.Assertions.*;
import static utils.Constants.BO_CLIENT_TABLE_NAME;
import static utils.Utils.getCurrentTimestampDbFormat;

public class BoHelper {

    public static void closeAlert(String ucid) throws SQLException {
        executeQueryToDb(
                DbName.BO, String.format("UPDATE bo.bo.alert SET closed_at ='%s', status = '%s' WHERE client_id = (select id from %s where ucid = '%s')", getCurrentTimestampDbFormat(), "CLOSED", BO_CLIENT_TABLE_NAME, ucid
                )
        );
    }

    @Step("delete user from BO")
    public static void deleteUserBO(String ucid) throws Exception {
        Allure.step("delete user from BO");
        try {
            List<Client> client = getObjectsFromDB(DbName.BO, "bo.bo.client", "ucid = '" + ucid + "'", Client.class);
            int boId = client.getFirst().id;
            deleteEntryFromDb(DbName.BO, "bo.bo.clients_fraud_types", "client_id = '" + boId + "'");
            Thread.sleep(100);
            deleteEntryFromDb(DbName.BO, "bo.bo.alert", "client_id = '" + boId + "'");
            Thread.sleep(100);
            deleteEntryFromDb(DbName.BO, "bo.bo.client", "ucid = '" + ucid + "'");
            Thread.sleep(100);
        } catch (Exception NoSuchElementException) {
            System.out.println("No such user");
        }
    }

    public static void cleanUserFraudsDb(String ucid) throws Exception {
        Allure.step("delete user's frauds from BO");
        try {
            List<Client> client = getObjectsFromDB(DbName.BO, "bo.bo.client", "ucid = '" + ucid + "'", Client.class);
            int boId = client.getFirst().id;
            deleteEntryFromDb(DbName.BO, "bo.bo.clients_fraud_types", "client_id = '" + boId + "'");
            Thread.sleep(100);
        } catch (Exception NoSuchElementException) {
            System.out.println("No such user");
        }
    }

    @Step("Check that user have record about fraud in db")
    public static void checkUserFraudDb(String ucid, long expectedFraud) throws Exception {
        Allure.step("Check that user have record about fraud in db");
        Thread.sleep(2000);
        long fraud = 0;

        List<Client> client = getObjectsFromDB(DbName.BO, "bo.bo.client", "ucid = '" + ucid + "'", Client.class);
        int boId = client.getFirst().id;
        System.out.println("CLIENT ID IN BO " + boId);
        List<ClientFraudTypes> clientFraudTypes = getObjectsFromDB(DbName.BO, "bo.bo.clients_fraud_types", "client_id = '" + boId + "' AND fraud_type_id = '" + expectedFraud + "'", ClientFraudTypes.class);
        Thread.sleep(100);

        fraud = clientFraudTypes.getFirst().getFraudTypeId();
        System.out.println("FRAUD ID " + fraud);

        assertEquals(expectedFraud, fraud);
    }

    @Step("Check that user NOT have records about frauds in db")
    public static void checkUserNoFraudDb(String ucid) throws Exception {
        Allure.step("Check that user not have records about frauds in db");
        Thread.sleep(2000);
        long fraud = 0;

        List<Client> client = getObjectsFromDB(DbName.BO, "bo.bo.client", "ucid = '" + ucid + "'", Client.class);
        int boId = client.getFirst().id;
        System.out.println("CLIENT ID IN BO " + boId);
        List<ClientFraudTypes> clientFraudTypes = getObjectsFromDB(DbName.BO, "bo.bo.clients_fraud_types", "client_id = '" + boId + "'", ClientFraudTypes.class);
        Thread.sleep(100);

        assertEquals(clientFraudTypes.size(), 0);

        assertTrue(clientFraudTypes.isEmpty());
    }

    public static void createUserFraudsDb(String ucid, long fraudId) throws Exception {
        Allure.step("create fraud for user in DB");
        Thread.sleep(2000);
        List<Client> client = getObjectsFromDB(DbName.BO, "bo.bo.client", "ucid = '" + ucid + "'", Client.class);
        long boId = client.getFirst().id;
        System.out.println("CLIENT ID IN BO " + boId);
        ClientFraudTypes fraudTypes = new ClientFraudTypes();
        fraudTypes.setFraudTypeId(fraudId);
        fraudTypes.setClientId(boId);
        insertObjectToDb(DbName.BO, "bo.bo.clients_fraud_types", fraudTypes);
        Thread.sleep(100);
    }

    @Step("Check confirmation status of alert in DB")
    public static void checkUserAlertConfirmation(String ucid, boolean expectedConfirmation) throws Exception {
        Allure.step("Check confirmation status of alert in DB");
        Thread.sleep(2000);
        long fraud = 0;

        List<Client> client = getObjectsFromDB(DbName.BO, "bo.bo.client", "ucid = '" + ucid + "'", Client.class);
        int boId = client.getFirst().id;
        System.out.println("CLIENT ID IN BO " + boId);
        List<Alert> alert = getObjectsFromDB(DbName.BO, "bo.bo.alert", "client_id = '" + boId + "'", Alert.class);
        Thread.sleep(100);

        assertEquals(alert.getFirst().confirmed, expectedConfirmation);
    }

}
