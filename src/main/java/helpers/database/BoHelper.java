package helpers.database;

import businessObjects.db.backofficeDb.client.Client;
import businessObjects.db.backofficeDb.clientFraudTypes.ClientFraudTypes;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;

import java.sql.SQLException;
import java.util.List;

import static helpers.database.DbHelper.*;
import static helpers.database.DbHelper.deleteEntryFromDb;
import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Step("Check that user have record about fraud in db")
    public static void checkUserFraudDB(String ucid, long expectedFraud) throws Exception {
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

}
