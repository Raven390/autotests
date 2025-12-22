package utils.demo_helpers;

import static helpers.database.DbHelper.insertObjectToDb;
import static utils.Constants.CRM_USER_TABLE_NAME;
import static utils.Utils.getCurrentTimestampDbFormat;

import business_objects.db.clickhouse.crm_tb_user_table.CrmTbUserObject;

public class ClickHouse {

    public static void createSimpleClient(String ucid, String firstName, String lastName) {
        String lowerCaseBrand = ucid.split("-")[0];
        String brand = lowerCaseBrand.substring(0, 1).toUpperCase() + lowerCaseBrand.substring(1);
        CrmTbUserObject testUser = new CrmTbUserObject(
                Integer.parseInt(ucid.split("-")[1]),
                ucid,
                brand,
                "FCA",
                "fcaGroup",
                "2024-10-23",
                "2024-10-23",
                firstName,
                lastName,
                "male",
                "1975-05-11",
                "Cyprus",
                "CY",
                "CY",
                "en",
                "RUS",
                "DUrksdLPlqZB6byC9vfKk6qm9BpUmsOS",
                "BjrbbdAHkwhBFLnPclfvbg==",
                "996",
                "1",
                "2FA",
                "2",
                "1",
                "1",
                1,
                2,
                3,
                "APPROVED",
                getCurrentTimestampDbFormat(),
                "2024-10-23 14:56:59",
                "2024-10-23 14:56:59",
                "nationalityId",
                getCurrentTimestampDbFormat());
        insertObjectToDb(CRM_USER_TABLE_NAME, testUser);
    }
}
