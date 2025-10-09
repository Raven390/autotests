package helpers.database;

import java.util.Arrays;
import java.util.stream.Collectors;

import static helpers.database.DbHelper.deleteEntryFromDb;
import static utils.Constants.AUDIT_EVENT;

public class AuHelper {
    public static void cleanClientAudit(String... ucid) throws Exception {
        String ucids = Arrays.stream(ucid).map(u -> "'" + u + "'").collect(Collectors.joining(", "));
        String condition = "ucid IN (" + ucids + ")";
        deleteEntryFromDb(DbName.POSTGRES, AUDIT_EVENT, condition);
    }
}
