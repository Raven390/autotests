package helpers.database;

import io.qameta.allure.Step;

import static helpers.database.DbHelper.deleteEntryFromDb;

public class AuditHelper {

    @Step("clean users audit history")
    public static void cleanUserAudit(String ucid) throws Exception {
        deleteEntryFromDb(DbName.AUDIT, "event", "ucid = '" + ucid + "'");

    }
}
