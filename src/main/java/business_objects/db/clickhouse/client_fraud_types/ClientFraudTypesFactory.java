package business_objects.db.clickhouse.client_fraud_types;

import io.qameta.allure.Step;

import java.util.List;

import static helpers.database.DbHelper.*;
import static utils.Constants.CLIENT_FRAUD_TYPES_TABLE_NAME;
import static utils.Utils.getCurrentTimestampDbFormat;

public class ClientFraudTypesFactory {
    public static ClientFraudTypes createClientFraudTypeCh(String ucid, String fraudTypeCode) {
        ClientFraudTypes fraud = new ClientFraudTypes();
        fraud.setUcid(ucid);
        fraud.setFraudTypeCode(fraudTypeCode);
        fraud.setSource("VINDEX");
        fraud.setIsDeleted(0);
        fraud.setLastUpdated(getCurrentTimestampDbFormat());
        return fraud;
    }

    private ClientFraudTypesFactory() {
    }

    @Step("Create fraud for user with ucid '{ucid}' in BO")
    public static void createUserFraudsCh(String ucid, String... fraudTypeCode) throws Exception {
        Thread.sleep(2000);
        List<ClientFraudTypes> frauds = new java.util.ArrayList<>(List.of());
        for (String fraudCode : fraudTypeCode) {
            frauds.add(createClientFraudTypeCh(ucid, fraudCode));
        }
        insertObjectsToDb(CLIENT_FRAUD_TYPES_TABLE_NAME, frauds);
        Thread.sleep(100);
    }

    public static void deleteUserFraudsCh(String ucid) throws Exception {
        deleteEntryFromDb(CLIENT_FRAUD_TYPES_TABLE_NAME, "ucid = '" + ucid + "'");
    }
}
