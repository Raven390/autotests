package utils.demo_helpers;

import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;


import static helpers.database.DbHelper.deleteEntryFromDb;
import static helpers.database.DbHelper.insertObjectToDb;
import static utils.Utils.getCurrentTimestampDbFormat;
import static utils.Utils.getRandomUuidString;

public class ClickHouse {

    public static void createSimpleClient(String ucid, String firstName, String lastName) {
        String lovercaseBrand = ucid.split("-")[0];
        String brand = lovercaseBrand.substring(0, 1).toUpperCase() + lovercaseBrand.substring(1);
        CrmTbUserObject testUser = new CrmTbUserObject(Integer.parseInt(ucid.split("-")[1]), ucid, brand, "FCA", "2024-10-23", "2024-10-23", firstName, lastName, "male", "1975-05-11", "Cyprus", "CY", "CY", "en", "RUS", "DUrksdLPlqZB6byC9vfKk6qm9BpUmsOS", "BjrbbdAHkwhBFLnPclfvbg==", "996", "1", "2FA", "2", "1", "1", 1, 2, 3, "APPROVED", getCurrentTimestampDbFormat(), "2024-10-23 14:56:59", "2024-10-23 14:56:59", getRandomUuidString(), "nationalityId", getCurrentTimestampDbFormat());
        System.out.println(testUser.toString());
        insertObjectToDb("vindex_test.crm___tb_user", testUser);
    }

    public static void deleteClient(String ucid) {
        deleteEntryFromDb("vindex_test.crm___tb_user", "ucid = '" + ucid + "'");
    }
}
