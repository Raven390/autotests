package helpers.database;

import businessObjects.db.mitigationServiceDb.ClientsRestriction;
import io.qameta.allure.Step;

import java.util.List;

import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.getObjectsFromDB;

public class MitigationHelper {
    @Step("clean users restriction history")
    public static void cleanUserRestriction(String ucid) throws Exception {
        List<ClientsRestriction> restrictionList = getObjectsFromDB(DbName.MITIGATION_POSTGRES, "clients_restriction", "ucid = '"+ ucid+"'", ClientsRestriction.class);
        for (ClientsRestriction i : restrictionList) {
            String Id = i.id.toString();
            deleteEntryFromDb(DbName.MITIGATION_POSTGRES, "mi.mi.action", "clients_restriction_id = " + Id);
            Thread.sleep(100);
            deleteEntryFromDb(DbName.MITIGATION_POSTGRES, "mi.mi.kafka_request", "clients_restriction_id = " + Id);
            Thread.sleep(100);
            deleteEntryFromDb(DbName.MITIGATION_POSTGRES, "mi.mi.kafka_response", "clients_restriction_id = " + Id);
            Thread.sleep(100);
            deleteEntryFromDb(DbName.MITIGATION_POSTGRES, "mi.mi.clients_restriction", "id = " + Id);
            Thread.sleep(100);
        }
    }
}
