package helpers.database;

import business_objects.db.mitigation_service_db.ClientsRestriction;
import io.qameta.allure.Step;

import java.util.List;

import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.getObjectsFromDB;
import static utils.Constants.*;

public class MitigationHelper {
    @Step("Clean users restriction history for ucid '{ucid}'")
    public static void cleanUserRestriction(String ucid) throws Exception {
        List<ClientsRestriction> restrictionList = getObjectsFromDB(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, "ucid = '" + ucid + "'", ClientsRestriction.class);
        for (ClientsRestriction i : restrictionList) {
            String Id = i.id.toString();
            deleteEntryFromDb(DbName.MITIGATION_POSTGRES, MITIGATION_ACTION, "clients_restriction_id = " + Id);
            Thread.sleep(100);
            deleteEntryFromDb(DbName.MITIGATION_POSTGRES, MITIGATION_KAFKA_REQUEST, "clients_restriction_id = " + Id);
            Thread.sleep(100);
            deleteEntryFromDb(DbName.MITIGATION_POSTGRES, MITIGATION_KAFKA_RESPONSE, "clients_restriction_id = " + Id);
            Thread.sleep(100);
            deleteEntryFromDb(DbName.MITIGATION_POSTGRES, MITIGATION_CLIENTS_RESTRICTION, "id = " + Id);
            Thread.sleep(100);
        }
    }
}
