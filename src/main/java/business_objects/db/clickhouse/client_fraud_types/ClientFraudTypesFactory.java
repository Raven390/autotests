package business_objects.db.clickhouse.client_fraud_types;

import static helpers.database.DbHelper.*;
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

    private ClientFraudTypesFactory() {}
}
